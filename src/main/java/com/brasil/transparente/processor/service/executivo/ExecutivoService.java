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
import java.util.List;

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
    List<Poder> poderList = List.of(new Poder(EXECUTIVO));

    public Poder generateExecutivoBranch(String year) {
        log.info("Iniciando - Poder Executivo");
        executivoGeneratorService.generateExpensesByMonth(poderList, StandardCharsets.ISO_8859_1, "/Executivo/" + year + "/", 47, ";", year);
        spySupremoTribunalFederalGeneratorService.generateExpenses(poderList, StandardCharsets.UTF_8, "/Judiciario/" + SUPREMO_TRIBUNAL_FEDERAL + "/" + year + "/STF.csv",8, ";"); // 2025 alterou delimitador de "," para ";"
        spyJusticaFederalGeneratorService.generateExpensesByMonth(poderList, StandardCharsets.UTF_8, "/Judiciario/" + JUSTICA_FEDERAL + "/" + year + "/", 9, "\t", year);
        processExpensesService.aggregateAllPowerSpending(poderList.getFirst());
        log.info("Finalizado - Poder Executivo");
        return poderList.getFirst();
    }

}
