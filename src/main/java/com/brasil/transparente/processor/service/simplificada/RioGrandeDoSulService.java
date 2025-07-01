package com.brasil.transparente.processor.service.simplificada;

import com.brasil.transparente.processor.entity.DespesaSimplificada;
import com.brasil.transparente.processor.repository.DespesaSimplificadaRepository;
import com.brasil.transparente.processor.repository.ElementoDespesaRepository;
import com.brasil.transparente.processor.util.CalculationService;
import com.brasil.transparente.processor.util.constants.DespesaSimplificadaConstants;
import com.brasil.transparente.processor.util.constants.UnidadesFederativasConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RioGrandeDoSulService {

    private final CalculationService calculationService;
    private final ElementoDespesaRepository elementoDespesaRepository;
    private final DespesaSimplificadaRepository despesaSimplificadaRepository;

    public void calculateAndSaveRS1(double totalExpense) {
        double specificExpense = elementoDespesaRepository.findByUnidadeFederativaAndTermsLikeAndListNameOrgao(
                UnidadesFederativasConstants.RS_ID_LONG,
                DespesaSimplificadaConstants.TERMO_APOSENTADORIA1,
                DespesaSimplificadaConstants.TERMO_APOSENTADORIA2,
                DespesaSimplificadaConstants.TERMO_APOSENTADORIA3,
                DespesaSimplificadaConstants.INSTITUTO_PREVIDENCIA_RS
        );
        double percentageOfTotal = calculationService.getPercentageOfTotal(specificExpense, totalExpense);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                DespesaSimplificadaConstants.APOSENTADORIAS_PENSOES,
                specificExpense,
                percentageOfTotal,
                UnidadesFederativasConstants.RS_ID_LONG
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    public void calculateAndSaveRS2(double totalExpense) {
        double specificExpense = elementoDespesaRepository.findbyUnidadeFederativaIdAndMinisterioNameList(
                UnidadesFederativasConstants.RS_ID_LONG,
                DespesaSimplificadaConstants.SECRETARIAS_SEGURANCA
        );
        double percentageOfTotal = calculationService.getPercentageOfTotal(specificExpense, totalExpense);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                DespesaSimplificadaConstants.SEGURANCA,
                specificExpense,
                percentageOfTotal,
                UnidadesFederativasConstants.RS_ID_LONG
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    public void calculateAndSaveRS3(double totalExpense) {
        double specificExpense = elementoDespesaRepository.findbyUnidadeFederativaIdAndMinisterioNameList(
                UnidadesFederativasConstants.RS_ID_LONG,
                DespesaSimplificadaConstants.SECRETARIA_SAUDE
        );
        double percentageOfTotal = calculationService.getPercentageOfTotal(specificExpense, totalExpense);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                DespesaSimplificadaConstants.SAUDE,
                specificExpense,
                percentageOfTotal,
                UnidadesFederativasConstants.RS_ID_LONG
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    public void calculateAndSaveRS4(double totalExpense) {
        double specificExpense = elementoDespesaRepository.findbyUnidadeFederativaIdAndMinisterioNameList(
                UnidadesFederativasConstants.RS_ID_LONG,
                DespesaSimplificadaConstants.SECRETARIA_EDUCACAO
        );
        double percentageOfTotal = calculationService.getPercentageOfTotal(specificExpense, totalExpense);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                DespesaSimplificadaConstants.EDUCACAO,
                specificExpense,
                percentageOfTotal,
                UnidadesFederativasConstants.RS_ID_LONG
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    public void calculateAndSaveRS5(double totalExpense) {
        double specificExpense = elementoDespesaRepository.findByUnidadeFederativaIdAndNamePoderAndTermsNotLike(
                UnidadesFederativasConstants.RS_ID_LONG,
                DespesaSimplificadaConstants.PODER_JUDICIARIO,
                DespesaSimplificadaConstants.TERMO_APOSENTADORIA1,
                DespesaSimplificadaConstants.TERMO_APOSENTADORIA2,
                DespesaSimplificadaConstants.TERMO_APOSENTADORIA3
        );
        double percentageOfTotal = calculationService.getPercentageOfTotal(specificExpense, totalExpense);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                DespesaSimplificadaConstants.PODER_JUDICIARIO_ESTADUAL,
                specificExpense,
                percentageOfTotal,
                UnidadesFederativasConstants.RS_ID_LONG
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    public void calculateAndSaveRSOutros(double totalExpense) {
        double simplifiedExpenses = despesaSimplificadaRepository.sumTotalValueByUnidadeFederativa(UnidadesFederativasConstants.RS_ID_LONG);
        double restExpenses = totalExpense - simplifiedExpenses;
        double percentageOfTotal = calculationService.getPercentageOfTotal(restExpenses, totalExpense);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                DespesaSimplificadaConstants.OUTROS,
                restExpenses,
                percentageOfTotal,
                UnidadesFederativasConstants.RS_ID_LONG
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

}
