package com.brasil.transparente.processor.service.estados;

import com.brasil.transparente.processor.entity.Poder;
import com.brasil.transparente.processor.entity.UnidadeFederativa;
import com.brasil.transparente.processor.repository.UnidadeFederativaRepository;
import com.brasil.transparente.processor.service.creation.CreateEntityService;
import com.brasil.transparente.processor.service.creation.ProcessExpensesService;
import com.brasil.transparente.processor.service.estados.generator.AmazonasGeneratorService;
import com.brasil.transparente.processor.service.estados.generator.BahiaGeneratorService;
import com.brasil.transparente.processor.service.estados.generator.RioGrandeDoSulGeneratorService;
import com.brasil.transparente.processor.service.simplificada.OrchestrationService;
import com.brasil.transparente.processor.util.NameCorrectorService;
import com.brasil.transparente.processor.util.PoderFactory;
import com.brasil.transparente.processor.util.constants.UnidadesFederativasConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EstadosService {

    private final ProcessExpensesService processExpensesService;
    private final CreateEntityService createEntityService;
    private final NameCorrectorService nameCorrectorService;
    private final UnidadeFederativaRepository unidadeFederativaRepository;
    private final OrchestrationService orchestrationService;
    private final RioGrandeDoSulGeneratorService rioGrandeDoSulGeneratorService;
    private final BahiaGeneratorService bahiaGeneratorService;
    private final AmazonasGeneratorService amazonasGeneratorService;

    public void generateStates(String year) {
        processRioGrandeDoSul(year);
        processBahia(year);
        processAmazonas(year);
    }

    private void processState(UnidadeFederativa unidadeFederativa, List<Poder> poderList) {
        unidadeFederativa.setListPoder(poderList);
        for (Poder poder : poderList) {
            processExpensesService.aggregateAllPowerSpending(poder);
            createEntityService.setRelationships(unidadeFederativa);
        }
        double gastoTotalValue = processExpensesService.aggregateTotalExpense(unidadeFederativa);
        processExpensesService.removeNegativeOrZeroExpenses(unidadeFederativa.getListPoder());
        processExpensesService.setTotalPercentages(unidadeFederativa.getListPoder(), gastoTotalValue);
        nameCorrectorService.refactorNames(unidadeFederativa.getListPoder());
        log.info("Salvando no banco de dados");
        unidadeFederativaRepository.save(unidadeFederativa);
    }

    private void processRioGrandeDoSul(String year) {
        List<Poder> poderList = PoderFactory.criarListaPoderes();
        UnidadeFederativa unidadeFederativa = new UnidadeFederativa(UnidadesFederativasConstants.RS_NAME);
        rioGrandeDoSulGeneratorService.generateExpensesByMonth(poderList, StandardCharsets.ISO_8859_1, "/Estados/" + UnidadesFederativasConstants.RS_SIGLA + "/", 45, ";", year);
        processState(unidadeFederativa, poderList);
        orchestrationService.generateSimplifiedReportRS();
    }

    private void processBahia(String year) {
        List<Poder> poderList = PoderFactory.criarListaPoderes();
        UnidadeFederativa unidadeFederativa = new UnidadeFederativa(UnidadesFederativasConstants.BA_NAME);
        bahiaGeneratorService.generateExpenses(poderList, StandardCharsets.UTF_8, "/Estados/" + UnidadesFederativasConstants.BA_SIGLA + "/" + year +".csv", 63, ";");
        processState(unidadeFederativa, poderList);
        orchestrationService.generateSimplifiedReportBA();
    }

    private void processAmazonas(String year) {
        List<Poder> poderList = PoderFactory.criarListaPoderes();
        UnidadeFederativa unidadeFederativa = new UnidadeFederativa(UnidadesFederativasConstants.AM_NAME);
        amazonasGeneratorService.generateExpenses(poderList, StandardCharsets.ISO_8859_1, "/Estados/" + UnidadesFederativasConstants.AM_SIGLA + "/" + year +".csv", 40, ";");
        processState(unidadeFederativa, poderList);
        orchestrationService.generateSimplifiedReportAM();
    }

}
