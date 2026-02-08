package com.brasil.transparente.processor.service.legislativo;

import com.brasil.transparente.processor.entity.*;
import com.brasil.transparente.processor.service.creation.ProcessExpensesService;
import com.brasil.transparente.processor.service.legislativo.generator.CamaraDeputadosGeneratorService;
import com.brasil.transparente.processor.service.legislativo.generator.SenadoFederalGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class LegislativoService {

    private final ProcessExpensesService processExpensesService;
    private final CamaraDeputadosGeneratorService camaraDeputadosGeneratorService;
    private final SenadoFederalGeneratorService senadoFederalGeneratorService;

    private static final String LEGISLATIVO = "Poder Legislativo";
    List<Poder> listPoder = List.of(new Poder(LEGISLATIVO));

    public Poder generateLegislativoBranch(String year) {
        log.info("Iniciando - Poder Legislativo");
        camaraDeputadosGeneratorService.generateExpenses(listPoder, StandardCharsets.UTF_8, "/Legislativo/" + year + "/Camara Deputados.csv",2, ";"); // 2025 alterou delimitador de "\t" para ";"
        senadoFederalGeneratorService.generateExpenses(listPoder, StandardCharsets.ISO_8859_1, "/Legislativo/" + year + "/Senado Federal.csv",3, ";"); // 2025 alterou delimitador de "," para ";"
        processExpensesService.aggregateAllPowerSpending(listPoder.getFirst());
        log.info("Finalizado - Poder Legislativo");
        return listPoder.getFirst();
    }

}
