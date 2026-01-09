package com.brasil.transparente.processor.receita;

import com.brasil.transparente.processor.entity.Receita;
import com.brasil.transparente.processor.repository.ReceitaRepository;
import com.brasil.transparente.processor.repository.RepasseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Slf4j
@Service
public class ProcessorReceitaService {

    private final RepasseRepository repasseRepository;
    private final ReceitaRepository receitaRepository;

    private static final int IDX_TRIBUTO = 9;
    private static final int IDX_VALOR = 12;
    private static final String REPASSE_ID = "1";

    private static final Map<ReceitaTipo, Double> REPASSE_PERCENTUAL = Map.of(
            ReceitaTipo.IRRF, 0.50,
            ReceitaTipo.IRPJ, 0.50,
            ReceitaTipo.IRPF, 0.50,
            ReceitaTipo.IPI,  0.49,
            ReceitaTipo.ITR,  0.50,
            ReceitaTipo.CIDE, 0.29,
            ReceitaTipo.SALARIO_EDUCACAO, 0.60
    );

    @Transactional
    public void processReceitaByAno(String ano) {
        Path csvPath = Path.of(System.getProperty("user.home"), "Downloads", ano + "_Receitas.csv");

        Map<ReceitaTipo, Double> rawReceitas = aggregateCsvData(csvPath);
        Map<ReceitaTipo, Double> netReceitas = applyConstitutionalAdjustments(rawReceitas);

        saveReceitaList(netReceitas);
    }

    private Map<ReceitaTipo, Double> aggregateCsvData(Path path) {
        try (Stream<String> lines = Files.lines(path, StandardCharsets.ISO_8859_1)) {
            return lines.skip(1)
                    .map(line -> line.split(";", -1))
                    .filter(cols -> cols.length > IDX_VALOR)
                    .filter(cols -> isEligible(cols[IDX_TRIBUTO]))
                    .collect(Collectors.toMap(
                            cols -> classify(cols[IDX_TRIBUTO]),
                            cols -> parseValue(cols[IDX_VALOR]),
                            Double::sum,
                            () -> new EnumMap<>(ReceitaTipo.class)
                    ));
        } catch (IOException e) {
            throw new RuntimeException("Failed to process CSV file", e);
        }
    }

    private Map<ReceitaTipo, Double> applyConstitutionalAdjustments(Map<ReceitaTipo, Double> receitas) {
        double theoreticalTotal = receitas.entrySet().stream()
                .filter(e -> REPASSE_PERCENTUAL.containsKey(e.getKey()))
                .mapToDouble(e -> e.getValue() * REPASSE_PERCENTUAL.get(e.getKey()))
                .sum();

        if (theoreticalTotal <= 0) return receitas;

        double actualRepasse = repasseRepository.findById(REPASSE_ID)
                .orElseThrow(() -> new RuntimeException("Repasse record not found"))
                .getValorTotal().doubleValue();

        double adjustmentFactor = actualRepasse / theoreticalTotal;

        REPASSE_PERCENTUAL.forEach((tipo, percent) -> {
            if (receitas.containsKey(tipo)) {
                double deduction = (receitas.get(tipo) * percent * adjustmentFactor) * -1;
                receitas.merge(tipo, deduction, Double::sum);
            }
        });

        return receitas;
    }

    private void saveReceitaList(Map<ReceitaTipo, Double> netReceitas) {
        double totalSum = netReceitas.values().stream()
                .filter(v -> v > 0)
                .mapToDouble(Double::doubleValue)
                .sum();

        if (totalSum <= 0) return;

        List<Receita> entities = netReceitas.entrySet().stream()
                .filter(e -> e.getValue() > 0)
                .map(e -> buildEntity(e.getKey(), e.getValue(), totalSum))
                .toList();

        receitaRepository.saveAll(entities);
        log.info("Successfully persisted {} revenue records using Double precision.", entities.size());
    }

    private Receita buildEntity(ReceitaTipo tipo, Double value, Double total) {
        double percentage = (value * 100.0) / total;

        return Receita.builder()
                .nameReceita(tipo.getDescricao())
                .totalValueSpent(value)
                .percentageOfTotal(percentage)
                .build();
    }

    private boolean isEligible(String rawTributo) {
        ReceitaTipo tipo = classify(rawTributo);
        return tipo != null && tipo != ReceitaTipo.TITULOS_DIVIDA;
    }

    private ReceitaTipo classify(String rawTributo) {
        return (rawTributo == null || rawTributo.isBlank())
                ? null
                : ReceitaClassifier.classify(rawTributo.trim());
    }

    private Double parseValue(String rawValue) {
        if (rawValue == null || rawValue.isBlank()) return 0.0;
        String clean = rawValue.replace("\"", "").replace(".", "").replace(",", ".");
        return Double.parseDouble(clean);
    }
}