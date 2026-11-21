package com.brasil.transparente.processor.service.executivo;

import com.brasil.transparente.processor.entity.*;
import com.brasil.transparente.processor.service.creation.ProcessExpensesService;
import com.brasil.transparente.processor.service.executivo.generator.ExecutivoGeneratorService;
import com.brasil.transparente.processor.service.executivo.generator.SpyJusticaFederalGeneratorService;
import com.brasil.transparente.processor.service.executivo.generator.SpySupremoTribunalFederalGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private static final String EXECUTIVO = "Poder Executivo";
    private static final String SUPREMO_TRIBUNAL_FEDERAL = "Supremo Tribunal Federal";
    private static final String JUSTICA_FEDERAL = "Justiça Federal";

    public Poder generateExecutivoBranch(String year) {
        log.info("Iniciando - Poder Executivo");
        Poder poder = new Poder(EXECUTIVO);
        executivoGeneratorService.generateExpensesByMonth(poder, StandardCharsets.ISO_8859_1, "/Executivo/" + year + "/", 47, ";", year);
        spySupremoTribunalFederalGeneratorService.generateExpenses(poder, StandardCharsets.UTF_8, "/Judiciario/" + SUPREMO_TRIBUNAL_FEDERAL + "/" + year + "/STF.csv",8, ";");
        spyJusticaFederalGeneratorService.generateExpensesByMonth(poder, StandardCharsets.UTF_8, "/Judiciario/" + JUSTICA_FEDERAL + "/" + year + "/", 9, "\t", year);
        processExpensesService.aggregateAllPowerSpending(poder);
        log.info("Finalizado - Poder Executivo");
        return poder;
    }

}
