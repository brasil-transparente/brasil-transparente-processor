package com.brasil.transparente.processor.service.judiciario;

import com.brasil.transparente.processor.entity.*;
import com.brasil.transparente.processor.service.creation.ProcessExpensesService;
import com.brasil.transparente.processor.service.judiciario.generator.JusticaPadraoGeneratorService;
import com.brasil.transparente.processor.service.judiciario.generator.SupremoTribunalFederalGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Slf4j
@Service
public class JudiciarioService {

    private final ProcessExpensesService processExpensesService;
    private final SupremoTribunalFederalGeneratorService supremoTribunalFederalGeneratorService;
    private final JusticaPadraoGeneratorService justicaPadraoGeneratorService;

    @Value("${CSV_PATH}")
    private String csvPath;

    private static final String JUDICIARIO = "Poder Judiciário";
    Poder poder = new Poder(JUDICIARIO);
    private static final String SUPREMO_TRIBUNAL_FEDERAL = "Supremo Tribunal Federal";
    private static final String SUPERIOR_TRIBUNAL_JUSTICA = "Superior Tribunal de Justiça";
    private static final String JUSTICA_MILITAR = "Justiça Militar";
    private static final String JUSTICA_FEDERAL = "Justiça Federal";
    private static final String JUSTICA_ELEITORAL = "Justiça Eleitoral";
    private static final String JUSTICA_TRABALHO = "Justiça do Trabalho";
    private static final String JUSTICA_DF_TERRITORIOS = "Justiça do Distrito Federal e Territórios";
    private static final String CONSELHO_NACIONAL_JUSTICA = "Conselho Nacional de Justiça";
    private static final String JUDICIARIO_PATH = "/Judiciario/";
    private static final String DESPESA_PATH = "/despesas";

    public Poder generateJudiciaryBranch(String year) {
        log.info("Poder Judiciário - Iniciando");
        supremoTribunalFederalGeneratorService.generateExpenses(poder, StandardCharsets.UTF_8, JUDICIARIO_PATH + SUPREMO_TRIBUNAL_FEDERAL + DESPESA_PATH + year + "/STF.csv",8, ",");
        justicaPadraoGeneratorService.generateExpensesByMonth(poder, StandardCharsets.UTF_8, JUDICIARIO_PATH + SUPERIOR_TRIBUNAL_JUSTICA + DESPESA_PATH + year + "/", 9, "\t", year);
        justicaPadraoGeneratorService.generateExpensesByMonth(poder, StandardCharsets.UTF_8, JUDICIARIO_PATH + JUSTICA_MILITAR + DESPESA_PATH + year + "/", 9, "\t", year);
        justicaPadraoGeneratorService.generateExpensesByMonth(poder, StandardCharsets.UTF_8, JUDICIARIO_PATH + JUSTICA_FEDERAL + DESPESA_PATH + year + "/", 9, "\t", year);
        justicaPadraoGeneratorService.generateExpensesByMonth(poder, StandardCharsets.UTF_8, JUDICIARIO_PATH + JUSTICA_ELEITORAL + DESPESA_PATH + year + "/", 9, "\t", year);
        justicaPadraoGeneratorService.generateExpensesByMonth(poder, StandardCharsets.UTF_8, JUDICIARIO_PATH + JUSTICA_TRABALHO + DESPESA_PATH + year + "/", 9, "\t", year);
        justicaPadraoGeneratorService.generateExpensesByMonth(poder, StandardCharsets.UTF_8, JUDICIARIO_PATH + JUSTICA_DF_TERRITORIOS + DESPESA_PATH + year + "/", 9, "\t", year);
        justicaPadraoGeneratorService.generateExpensesByMonth(poder, StandardCharsets.UTF_8, JUDICIARIO_PATH + CONSELHO_NACIONAL_JUSTICA + DESPESA_PATH + year + "/", 9, "\t", year);
        processExpensesService.aggregateAllPowerSpending(poder);
        log.info("Poder Judiciário - Finalizado");
        return poder;
    }

}
