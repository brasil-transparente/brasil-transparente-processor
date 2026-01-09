package com.brasil.transparente.processor.service.executivo;

import com.brasil.transparente.processor.entity.*;
import com.brasil.transparente.processor.repository.RepasseRepository;
import com.brasil.transparente.processor.service.creation.ProcessExpensesService;
import com.brasil.transparente.processor.service.executivo.generator.ExecutivoGeneratorService;
import com.brasil.transparente.processor.service.executivo.generator.RepasseGeneratorService;
import com.brasil.transparente.processor.service.executivo.generator.SpyJusticaFederalGeneratorService;
import com.brasil.transparente.processor.service.executivo.generator.SpySupremoTribunalFederalGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExecutivoService {

    private final ProcessExpensesService processExpensesService;
    private final ExecutivoGeneratorService executivoGeneratorService;
    private final SpySupremoTribunalFederalGeneratorService spySupremoTribunalFederalGeneratorService;
    private final SpyJusticaFederalGeneratorService spyJusticaFederalGeneratorService;
    private final RepasseGeneratorService repasseGeneratorService;
    private final RepasseRepository repasseRepository;

    private static final String EXECUTIVO = "Poder Executivo";
    private static final String REPASSE = "Repasse";
    private static final String SUPREMO_TRIBUNAL_FEDERAL = "Supremo Tribunal Federal";
    private static final String JUSTICA_FEDERAL = "Justiça Federal";
    List<Poder> poderList = List.of(new Poder(EXECUTIVO));
    List<Poder> poderRepasse = List.of(new Poder(REPASSE));

    public Poder generateExecutiveBranch(String year) {
        log.info("Iniciando - Poder Executivo");
        executivoGeneratorService.generateExpensesByMonth(poderList, StandardCharsets.ISO_8859_1, "/Executivo/despesas" + year + "/", 47, ";", year);
        spySupremoTribunalFederalGeneratorService.generateExpenses(poderList, StandardCharsets.UTF_8, "/Judiciario/" + SUPREMO_TRIBUNAL_FEDERAL + "/despesas" + year + "/STF.csv",8, ",");
        spyJusticaFederalGeneratorService.generateExpensesByMonth(poderList, StandardCharsets.UTF_8, "/Judiciario/" + JUSTICA_FEDERAL + "/despesas" + year + "/", 9, "\t", year);
        processExpensesService.aggregateAllPowerSpending(poderList.getFirst());
        generateValorRepasse(year);
        log.info("Finalizado - Poder Executivo");
        return poderList.getFirst();
    }

    private void generateValorRepasse(String year) {
        log.info("Gerando valor de repasse - Poder Executivo");
        repasseGeneratorService.generateExpensesByMonth(poderRepasse, StandardCharsets.ISO_8859_1, "/Executivo/despesas" + year + "/", 47, ";", year);
        processExpensesService.aggregateAllPowerSpending(poderRepasse.getFirst());
        BigDecimal valorTotalRepasse = BigDecimal.valueOf(poderRepasse.getFirst().getTotalValueSpent());
        Repasse repasse = Repasse.builder()
                .ano(year)
                .valorTotal(valorTotalRepasse)
                .build();
        repasseRepository.save(repasse);
    }

}
