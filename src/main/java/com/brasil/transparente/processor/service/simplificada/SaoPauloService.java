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
public class SaoPauloService {

    private final CalculationService calculationService;
    private final ElementoDespesaRepository elementoDespesaRepository;
    private final DespesaSimplificadaRepository despesaSimplificadaRepository;

    public void calculateAndSaveSP1(double totalExpenses) {
        double specificExpense = elementoDespesaRepository.findByUnidadeFederativaAndTermsLikeAndListNameOrgao(
                UnidadesFederativasConstants.SP_ID_LONG,
                DespesaSimplificadaConstants.TERMO_APOSENTADORIA1,
                DespesaSimplificadaConstants.TERMO_APOSENTADORIA2,
                DespesaSimplificadaConstants.TERMO_APOSENTADORIA3,
                DespesaSimplificadaConstants.FUNDOS_PREVIDENCIA_SAO_PAULO
        );
        double percentageOfTotal = calculationService.getPercentageOfTotal(specificExpense, totalExpenses);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                DespesaSimplificadaConstants.APOSENTADORIAS_PENSOES,
                specificExpense,
                percentageOfTotal,
                UnidadesFederativasConstants.SP_ID_LONG
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    public void calculateAndSaveSP2(double totalExpenses) {
        double specificExpense = elementoDespesaRepository.findbyUnidadeFederativaIdAndMinisterioNameList(
                UnidadesFederativasConstants.SP_ID_LONG,
                DespesaSimplificadaConstants.SECRETARIA_EDUCACAO
        );
        double percentageOfTotal = calculationService.getPercentageOfTotal(specificExpense, totalExpenses);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                DespesaSimplificadaConstants.EDUCACAO,
                specificExpense,
                percentageOfTotal,
                UnidadesFederativasConstants.SP_ID_LONG
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    public void calculateAndSaveSP3(double totalExpenses) {
        double specificExpense = elementoDespesaRepository.findbyUnidadeFederativaIdAndMinisterioNameList(
                UnidadesFederativasConstants.SP_ID_LONG,
                DespesaSimplificadaConstants.SECRETARIA_SAUDE
        );
        double percentageOfTotal = calculationService.getPercentageOfTotal(specificExpense, totalExpenses);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                DespesaSimplificadaConstants.SAUDE,
                specificExpense,
                percentageOfTotal,
                UnidadesFederativasConstants.SP_ID_LONG
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    public void calculateAndSaveSP4(double totalExpenses) {
        double specificExpense = elementoDespesaRepository.findbyUnidadeFederativaIdAndMinisterioNameList(
                UnidadesFederativasConstants.SP_ID_LONG,
                DespesaSimplificadaConstants.SECRETARIAS_SEGURANCA
        );
        double percentageOfTotal = calculationService.getPercentageOfTotal(specificExpense, totalExpenses);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                DespesaSimplificadaConstants.SEGURANCA,
                specificExpense,
                percentageOfTotal,
                UnidadesFederativasConstants.SP_ID_LONG
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    public void calculateAndSaveSP5(double totalExpenses) {
        double specificExpense = elementoDespesaRepository.findByUnidadeFederativaIdAndNamePoderAndTermsNotLike(
                UnidadesFederativasConstants.SP_ID_LONG,
                DespesaSimplificadaConstants.PODER_JUDICIARIO,
                DespesaSimplificadaConstants.TERMO_APOSENTADORIA1,
                DespesaSimplificadaConstants.TERMO_APOSENTADORIA2,
                DespesaSimplificadaConstants.TERMO_APOSENTADORIA3
        );
        double percentageOfTotal = calculationService.getPercentageOfTotal(specificExpense, totalExpenses);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                DespesaSimplificadaConstants.PODER_JUDICIARIO_ESTADUAL,
                specificExpense,
                percentageOfTotal,
                UnidadesFederativasConstants.SP_ID_LONG
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    public void calculateAndSaveSP6(double totalExpenses) {
        double specificExpense = elementoDespesaRepository.findByUnidadeFederativaIdAndNameMinisterioAndElementoDespesaNameList(
                UnidadesFederativasConstants.SP_ID_LONG,
                DespesaSimplificadaConstants.ADMIN_GERAL_ESTADO,
                DespesaSimplificadaConstants.DESPESAS_DIVIDA_PUBLICA
        );
        double percentageOfTotal = calculationService.getPercentageOfTotal(specificExpense, totalExpenses);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                DespesaSimplificadaConstants.DIVIDA_PUBLICA,
                specificExpense,
                percentageOfTotal,
                UnidadesFederativasConstants.SP_ID_LONG
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    public void calculateAndSaveSP7(double totalExpenses) {
        double specificExpense = elementoDespesaRepository.findbyUnidadeFederativaIdAndMinisterioNameList(
                UnidadesFederativasConstants.SP_ID_LONG,
                DespesaSimplificadaConstants.SECRETARIA_TRANSPORTE
        );
        double percentageOfTotal = calculationService.getPercentageOfTotal(specificExpense, totalExpenses);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                DespesaSimplificadaConstants.TRANSPORTE,
                specificExpense,
                percentageOfTotal,
                UnidadesFederativasConstants.SP_ID_LONG
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    public void calculateAndSaveSPOutros(double totalExpenses) {
        double simplifiedExpenses = despesaSimplificadaRepository.sumTotalValueByUnidadeFederativa(UnidadesFederativasConstants.SP_ID_LONG);
        double restExpenses = totalExpenses - simplifiedExpenses;
        double percentageOfTotal = calculationService.getPercentageOfTotal(restExpenses, totalExpenses);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                DespesaSimplificadaConstants.OUTROS,
                restExpenses,
                percentageOfTotal,
                UnidadesFederativasConstants.SP_ID_LONG
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

}
