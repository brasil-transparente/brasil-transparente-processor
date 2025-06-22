package com.brasil.transparente.processor.service;

import com.brasil.transparente.processor.entity.Poder;
import com.brasil.transparente.processor.entity.UnidadeFederativa;
import com.brasil.transparente.processor.util.NameCorrector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class GeneratorOrchestrationService {

    private final ExecutivoGeneratorService executivoGeneratorService;
    private final JudiciarioGeneratorService judiciarioGeneratorService;
    private final LegislativoGeneratorService legislativoGeneratorService;
    private final DespesaSimplificadaGeneratorService despesaSimplificadaGeneratorService;
    private final OrgaosAutonomosGeneratorService orgaosAutonomosGeneratorService;
    private final GeneralGeneratorService generalGeneratorService;
    private final NameCorrector nameCorrector;
    private final EstadoGeneratorService estadoGeneratorService;

    private static final String UNIAO_FEDERAL = "União Federal";
    List<Poder> poderList = new ArrayList<>();

    public void generateCompleteReportService(String year) {
        log.info("[INICIANDO]");
        generateUniaoReport(year);
        generateStatesReport(year);
        log.info("[FINALIZADO]");
    }

    private void generateUniaoReport(String year) {
        UnidadeFederativa unidadeFederativa = new UnidadeFederativa(UNIAO_FEDERAL);
        poderList.add(executivoGeneratorService.generateExecutiveBranch(year));
        poderList.add(judiciarioGeneratorService.generateJudiciaryBranch(year));
        poderList.add(legislativoGeneratorService.generateLegislativeBranch());
        poderList.add(orgaosAutonomosGeneratorService.generateOrgaosAutonomos(year));
        unidadeFederativa.setListPoder(poderList);
        generalGeneratorService.setRelationships(unidadeFederativa);
        double gastoTotalValue = generalGeneratorService.aggregateTotalExpense(unidadeFederativa);
        generalGeneratorService.removeNegativeOrZeroExpenses(unidadeFederativa.getListPoder());
        generalGeneratorService.setTotalPercentages(unidadeFederativa.getListPoder(), gastoTotalValue);
        nameCorrector.refactorNames(unidadeFederativa.getListPoder());
        generalGeneratorService.saveStructure(unidadeFederativa);
        despesaSimplificadaGeneratorService.generateSimplifiedReportUniao();
        log.info("Finalizado - União");
    }

    private void generateStatesReport(String year) {
        estadoGeneratorService.generateStateExpensesRS(year, "RS");
        estadoGeneratorService.generateStateExpensesBA(year, "BA");
        log.info("Finalizado - Estados");
    }

}
