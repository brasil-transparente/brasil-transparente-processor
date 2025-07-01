package com.brasil.transparente.processor.service;

import com.brasil.transparente.processor.entity.Poder;
import com.brasil.transparente.processor.entity.UnidadeFederativa;
import com.brasil.transparente.processor.repository.UnidadeFederativaRepository;
import com.brasil.transparente.processor.service.creation.CreateEntityService;
import com.brasil.transparente.processor.service.creation.ProcessExpensesService;
import com.brasil.transparente.processor.service.simplificada.OrchestrationService;
import com.brasil.transparente.processor.service.executive.ExecutivoService;
import com.brasil.transparente.processor.service.judiciario.JudiciarioService;
import com.brasil.transparente.processor.service.legislativo.LegislativoService;
import com.brasil.transparente.processor.service.orgaos.OrgaosAutonomosService;
import com.brasil.transparente.processor.util.NameCorrectorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class ProcessorOrchestrationService {

    private final ExecutivoService executivoService;
    private final JudiciarioService judiciarioService;
    private final LegislativoService legislativoGeneratorService;
    private final OrchestrationService orchestrationService;
    private final OrgaosAutonomosService orgaosAutonomosGeneratorService;
    private final CreateEntityService createEntityService;
    private final NameCorrectorService nameCorrectorService;
    private final EstadoGeneratorService estadoGeneratorService;
    private final UnidadeFederativaRepository unidadeFederativaRepository;
    private final ProcessExpensesService processExpensesService;

    private static final String UNIAO_FEDERAL = "União Federal";
    List<Poder> poderList = new ArrayList<>();

    public void generateCompleteReportService(String year) {
        log.info("[INICIANDO]");
        generateUniaoReport(year);
        generateStatesReport(year);
        log.info("[FINALIZADO]");
    }

    private void generateUniaoReport(String year) {
        log.info("Iniciando - União");
        UnidadeFederativa unidadeFederativa = new UnidadeFederativa(UNIAO_FEDERAL);
        generateUniaoBranches(year);
        cleanUpDataForDatabase(unidadeFederativa);
        log.info("Salvando no banco de dados - União");
        unidadeFederativaRepository.save(unidadeFederativa);
        orchestrationService.generateSimplifiedReportUniao();
        log.info("Finalizado - União");
    }

    private void generateUniaoBranches(String year) {
        poderList.add(executivoService.generateExecutiveBranch(year));
        poderList.add(judiciarioService.generateJudiciaryBranch(year));
        poderList.add(legislativoGeneratorService.generateLegislativeBranch());
        poderList.add(orgaosAutonomosGeneratorService.generateIndependentOrgansBranch(year));
    }

    private void cleanUpDataForDatabase(UnidadeFederativa unidadeFederativa) {
        unidadeFederativa.setListPoder(poderList);
        createEntityService.setRelationships(unidadeFederativa);
        double gastoTotalValue = processExpensesService.aggregateTotalExpense(unidadeFederativa);
        processExpensesService.removeNegativeOrZeroExpenses(unidadeFederativa.getListPoder());
        processExpensesService.setTotalPercentages(unidadeFederativa.getListPoder(), gastoTotalValue);
        nameCorrectorService.refactorNames(unidadeFederativa.getListPoder());
    }

    private void generateStatesReport(String year) {
        log.info("Iniciando - Estados");
        estadoGeneratorService.generateStateExpensesRS(year, "RS");
        estadoGeneratorService.generateStateExpensesBA(year, "BA");
        log.info("Finalizado - Estados");
    }

}
