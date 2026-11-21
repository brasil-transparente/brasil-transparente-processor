package com.brasil.transparente.processor.service;

import com.brasil.transparente.processor.entity.Poder;
import com.brasil.transparente.processor.repository.DespesaSimplificadaRepository;
import com.brasil.transparente.processor.repository.ElementoDespesaRepository;
import com.brasil.transparente.processor.repository.MinisterioRepository;
import com.brasil.transparente.processor.repository.OrgaoRepository;
import com.brasil.transparente.processor.repository.PoderRepository;
import com.brasil.transparente.processor.repository.UnidadeGestoraRepository;
import com.brasil.transparente.processor.service.creation.CreateEntityService;
import com.brasil.transparente.processor.service.creation.ProcessExpensesService;
import com.brasil.transparente.processor.service.simplificada.SimplificadaService;
import com.brasil.transparente.processor.service.executivo.ExecutivoService;
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
public class ProcessorService {

    private final ExecutivoService executivoService;
    private final JudiciarioService judiciarioService;
    private final LegislativoService legislativoGeneratorService;
    private final SimplificadaService orchestrationService;
    private final OrgaosAutonomosService orgaosAutonomosGeneratorService;
    private final CreateEntityService createEntityService;
    private final NameCorrectorService nameCorrectorService;
    private final PoderRepository poderRepository;
    private final MinisterioRepository ministerioRepository;
    private final OrgaoRepository orgaoRepository;
    private final UnidadeGestoraRepository unidadeGestoraRepository;
    private final ElementoDespesaRepository elementoDespesaRepository;
    private final DespesaSimplificadaRepository despesaSimplificadaRepository;
    private final ProcessExpensesService processExpensesService;

    public void generateCompleteReportService(String year) {
        log.info("[INICIANDO]");
        clearProcessedData();
        generateUniaoReport(year);
        log.info("[FINALIZADO]");
    }

    private void clearProcessedData() {
        log.info("Removendo dados do processamento anterior");
        despesaSimplificadaRepository.deleteAllInBatch();
        elementoDespesaRepository.deleteAllInBatch();
        unidadeGestoraRepository.deleteAllInBatch();
        orgaoRepository.deleteAllInBatch();
        ministerioRepository.deleteAllInBatch();
        poderRepository.deleteAllInBatch();
    }

    private void generateUniaoReport(String year) {
        log.info("Iniciando - União");
        List<Poder> poderList = generateUniaoBranches(year);
        double totalExpenses = cleanUpDataForDatabase(poderList);
        log.info("Salvando no banco de dados - União");
        poderRepository.saveAll(poderList);
        orchestrationService.generateSimplifiedReportUniao(totalExpenses);
        log.info("Finalizado - União");
    }

    private List<Poder> generateUniaoBranches(String year) {
        List<Poder> poderList = new ArrayList<>();
        poderList.add(executivoService.generateExecutivoBranch(year));
        poderList.add(judiciarioService.generateJudiciarioBranch(year));
        poderList.add(legislativoGeneratorService.generateLegislativoBranch(year));
        poderList.add(orgaosAutonomosGeneratorService.generateOrgaoIndependentesBranch(year));
        return poderList;
    }

    private double cleanUpDataForDatabase(List<Poder> poderList) {
        createEntityService.setRelationships(poderList);
        double gastoTotalValue = processExpensesService.aggregateTotalExpense(poderList);
        processExpensesService.removeNegativeOrZeroExpenses(poderList);
        processExpensesService.setTotalPercentages(poderList, gastoTotalValue);
        nameCorrectorService.refactorNames(poderList);
        return gastoTotalValue;
    }

}
