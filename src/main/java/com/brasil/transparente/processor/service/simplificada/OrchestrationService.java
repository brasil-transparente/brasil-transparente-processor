package com.brasil.transparente.processor.service.simplificada;

import com.brasil.transparente.processor.repository.UnidadeFederativaRepository;
import com.brasil.transparente.processor.util.constants.UnidadesFederativasConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrchestrationService {


    private final UniaoService uniaoService;
    private final RioGrandeDoSulService rioGrandeDoSulService;
    private final BahiaService bahiaService;
    private final UnidadeFederativaRepository unidadeFederativaRepository;
    private double totalExpenses;

    public void generateSimplifiedReportUniao() {
        log.info("Gerando estrutura simplificada - União");
        totalExpenses = unidadeFederativaRepository.findById(UnidadesFederativasConstants.UN_ID_STRING).get().getTotalValueSpent();
        uniaoService.calculateAndSaveUniao1(totalExpenses);
        uniaoService.calculateAndSaveUniao2(totalExpenses);
        uniaoService.calculateAndSaveUniao3(totalExpenses);
        uniaoService.calculateAndSaveUniao4(totalExpenses);
        uniaoService.calculateAndSaveUniao5(totalExpenses);
        uniaoService.calculateAndSaveUniao6(totalExpenses);
        uniaoService.calculateAndSaveUniao7(totalExpenses);
        uniaoService.calculateAndSaveUniao8(totalExpenses);
        uniaoService.calculateAndSaveUniao9(totalExpenses);
        uniaoService.calculateAndSaveUniaoOutros(totalExpenses);
    }

    public void generateSimplifiedReportRS() {
        log.info("Gerando estrutura simplificada - RS");
        totalExpenses = unidadeFederativaRepository.findById(UnidadesFederativasConstants.RS_ID_STRING).get().getTotalValueSpent();
        rioGrandeDoSulService.calculateAndSaveRS1(totalExpenses);
        rioGrandeDoSulService.calculateAndSaveRS2(totalExpenses);
        rioGrandeDoSulService.calculateAndSaveRS3(totalExpenses);
        rioGrandeDoSulService.calculateAndSaveRS4(totalExpenses);
        rioGrandeDoSulService.calculateAndSaveRS5(totalExpenses);
        rioGrandeDoSulService.calculateAndSaveRSOutros(totalExpenses);
    }

    public void generateSimplifiedReportBA() {
        log.info("Gerando estrutura simplificada - BA");
        totalExpenses = unidadeFederativaRepository.findById(UnidadesFederativasConstants.BA_ID_STRING).get().getTotalValueSpent();
        bahiaService.calculateAndSaveBA1(totalExpenses);
        bahiaService.calculateAndSaveBA2(totalExpenses);
        bahiaService.calculateAndSaveBA3(totalExpenses);
        bahiaService.calculateAndSaveBA4(totalExpenses);
        bahiaService.calculateAndSaveBA5(totalExpenses);
        bahiaService.calculateAndSaveBA6(totalExpenses);
        bahiaService.calculateAndSaveBA7(totalExpenses);
        bahiaService.calculateAndSaveBAOutros(totalExpenses);
    }

}
