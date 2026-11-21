package com.brasil.transparente.processor.service.legislativo;

import com.brasil.transparente.processor.entity.*;
import com.brasil.transparente.processor.service.creation.ProcessExpensesService;
import com.brasil.transparente.processor.service.legislativo.generator.CamaraDeputadosGeneratorService;
import com.brasil.transparente.processor.service.legislativo.generator.SenadoFederalGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Slf4j
@Service
public class LegislativoService {

    private final ProcessExpensesService processExpensesService;
    private final CamaraDeputadosGeneratorService camaraDeputadosGeneratorService;
    private final SenadoFederalGeneratorService senadoFederalGeneratorService;

    private static final String LEGISLATIVO = "Poder Legislativo";

    public Poder generateLegislativoBranch(String year) {
        log.info("Iniciando - Poder Legislativo");
        Poder poder = new Poder(LEGISLATIVO);
        camaraDeputadosGeneratorService.generateExpenses(poder, StandardCharsets.UTF_8, "/Legislativo/" + year + "/Camara Deputados.csv",2, ";");
        senadoFederalGeneratorService.generateExpenses(poder, StandardCharsets.ISO_8859_1, "/Legislativo/" + year + "/Senado Federal.csv",3, ";");
        processExpensesService.aggregateAllPowerSpending(poder);
        log.info("Finalizado - Poder Legislativo");
        return poder;
    }

}
