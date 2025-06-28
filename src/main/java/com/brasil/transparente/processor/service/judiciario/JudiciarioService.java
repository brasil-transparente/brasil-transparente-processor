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

    public Poder generateJudiciaryBranch(String year) {
        log.info("Poder Judiciário - Iniciando");
        supremoTribunalFederalGeneratorService.generateExpenses(poder, StandardCharsets.UTF_8, "/Judiciario/" + SUPREMO_TRIBUNAL_FEDERAL + "/despesas" + year + "/STF.csv",8, ",");
        justicaPadraoGeneratorService.generateExpensesByMonth(poder, StandardCharsets.UTF_8, "/Judiciario/" + SUPERIOR_TRIBUNAL_JUSTICA + "/despesas" + year + "/", 9, "\t", year);
        justicaPadraoGeneratorService.generateExpensesByMonth(poder, StandardCharsets.UTF_8, "/Judiciario/" + JUSTICA_MILITAR + "/despesas" + year + "/", 9, "\t", year);
        justicaPadraoGeneratorService.generateExpensesByMonth(poder, StandardCharsets.UTF_8, "/Judiciario/" + JUSTICA_FEDERAL + "/despesas" + year + "/", 9, "\t", year);
        justicaPadraoGeneratorService.generateExpensesByMonth(poder, StandardCharsets.UTF_8, "/Judiciario/" + JUSTICA_ELEITORAL + "/despesas" + year + "/", 9, "\t", year);
        justicaPadraoGeneratorService.generateExpensesByMonth(poder, StandardCharsets.UTF_8, "/Judiciario/" + JUSTICA_TRABALHO + "/despesas" + year + "/", 9, "\t", year);
        justicaPadraoGeneratorService.generateExpensesByMonth(poder, StandardCharsets.UTF_8, "/Judiciario/" + JUSTICA_DF_TERRITORIOS + "/despesas" + year + "/", 9, "\t", year);
        justicaPadraoGeneratorService.generateExpensesByMonth(poder, StandardCharsets.UTF_8, "/Judiciario/" + CONSELHO_NACIONAL_JUSTICA + "/despesas" + year + "/", 9, "\t", year);
        processExpensesService.aggregateAllPowerSpending(poder);
        log.info("Poder Judiciário - Finalizado");
        return poder;
    }

}
