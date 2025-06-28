package com.brasil.transparente.processor.service.legislativo;

import com.brasil.transparente.processor.entity.*;
import com.brasil.transparente.processor.service.creation.ProcessExpensesService;
import com.brasil.transparente.processor.service.legislativo.generator.CamaraDeputadosGeneratorService;
import com.brasil.transparente.processor.service.legislativo.generator.SenadoFederalGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Slf4j
@Service
public class LegislativoService {

    private final ProcessExpensesService processExpensesService;
    private final CamaraDeputadosGeneratorService camaraDeputadosGeneratorService;
    private final SenadoFederalGeneratorService senadoFederalGeneratorService;

    @Value("${CSV_PATH}")
    private String csvPath;

    private static final String LEGISLATIVO = "Poder Legislativo";
    Poder poder = new Poder(LEGISLATIVO);

    public Poder generateLegislativeBranch() {
        log.info("Poder Legislativo - Iniciando");
        camaraDeputadosGeneratorService.generateExpenses(poder, StandardCharsets.UTF_8, "/Legislativo/Camara Deputados.csv",2, "\t");
        senadoFederalGeneratorService.generateExpenses(poder, StandardCharsets.UTF_8, "/Legislativo/Senado Federal.csv",3, "\t");
        processExpensesService.aggregateAllPowerSpending(poder);
        log.info("Poder Legislativo - Finalizado");
        return poder;
    }

}
