package com.brasil.transparente.processor.service.executive;

import com.brasil.transparente.processor.entity.*;
import com.brasil.transparente.processor.service.creation.ProcessExpensesService;
import com.brasil.transparente.processor.service.executive.generator.ExecutivoGeneratorService;
import com.brasil.transparente.processor.service.executive.generator.SpyJusticaFederalGeneratorService;
import com.brasil.transparente.processor.service.executive.generator.SpySupremoTribunalFederalGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExecutivoService {

    private final ProcessExpensesService processExpensesService;
    private final ExecutivoGeneratorService executivoGeneratorService;
    private final SpySupremoTribunalFederalGeneratorService spySupremoTribunalFederalGeneratorService;
    private final SpyJusticaFederalGeneratorService spyJusticaFederalGeneratorService;

    @Value("${CSV_PATH}")
    private String csvPath;

    private static final String EXECUTIVO = "Poder Executivo";
    private static final String SUPREMO_TRIBUNAL_FEDERAL = "Supremo Tribunal Federal";
    private static final String JUSTICA_FEDERAL = "Justiça Federal";
    Poder poder = new Poder(EXECUTIVO);

    public Poder generateExecutiveBranch(String year) {
        log.info("Poder Executivo - Iniciando");
        executivoGeneratorService.generateExpensesByMonth(poder, StandardCharsets.ISO_8859_1, "/Executivo/despesas" + year + "/", 47, ";", year);
        spySupremoTribunalFederalGeneratorService.generateExpenses(poder, StandardCharsets.UTF_8, "/Judiciario/" + SUPREMO_TRIBUNAL_FEDERAL + "/despesas" + year + "/STF.csv",8, ",");
        spyJusticaFederalGeneratorService.generateExpensesByMonth(poder, StandardCharsets.UTF_8, "/Judiciario/" + JUSTICA_FEDERAL + "/despesas" + year + "/", 9, "\t", year);
        processExpensesService.aggregateAllPowerSpending(poder);
        log.info("Poder Executivo - Finalizado");
        return poder;
    }

}
