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
public class AmazonasService {

    private final CalculationService calculationService;
    private final ElementoDespesaRepository elementoDespesaRepository;
    private final DespesaSimplificadaRepository despesaSimplificadaRepository;

    public void calculateAndSaveAM1(double totalExpenses) {
        double specificExpense = elementoDespesaRepository.findByUnidadeFederativaAndTermsLikeAndListNameOrgao(
                UnidadesFederativasConstants.AM_ID_LONG,
                DespesaSimplificadaConstants.TERMO_APOSENTADORIA1,
                DespesaSimplificadaConstants.TERMO_APOSENTADORIA2,
                DespesaSimplificadaConstants.TERMO_APOSENTADORIA3,
                DespesaSimplificadaConstants.FUNDO_PREVIDENCIA_AMAZONAS
        );
        double percentageOfTotal = calculationService.getPercentageOfTotal(specificExpense, totalExpenses);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                DespesaSimplificadaConstants.APOSENTADORIAS_PENSOES,
                specificExpense,
                percentageOfTotal,
                UnidadesFederativasConstants.AM_ID_LONG
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    public void calculateAndSaveAM2(double totalExpenses) {
        double specificExpense = elementoDespesaRepository.findbyUnidadeFederativaIdAndMinisterioNameList(
                UnidadesFederativasConstants.AM_ID_LONG,
                DespesaSimplificadaConstants.SECRETARIA_EDUCACAO
        );
        double percentageOfTotal = calculationService.getPercentageOfTotal(specificExpense, totalExpenses);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                DespesaSimplificadaConstants.EDUCACAO,
                specificExpense,
                percentageOfTotal,
                UnidadesFederativasConstants.AM_ID_LONG
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    public void calculateAndSaveAM3(double totalExpenses) {
        double specificExpense = elementoDespesaRepository.findbyUnidadeFederativaIdAndMinisterioNameList(
                UnidadesFederativasConstants.AM_ID_LONG,
                DespesaSimplificadaConstants.SECRETARIA_SAUDE
        );
        double percentageOfTotal = calculationService.getPercentageOfTotal(specificExpense, totalExpenses);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                DespesaSimplificadaConstants.SAUDE,
                specificExpense,
                percentageOfTotal,
                UnidadesFederativasConstants.AM_ID_LONG
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    public void calculateAndSaveAM4(double totalExpenses) {
        double specificExpense = elementoDespesaRepository.findbyUnidadeFederativaIdAndMinisterioNameList(
                UnidadesFederativasConstants.AM_ID_LONG,
                DespesaSimplificadaConstants.SECRETARIAS_SEGURANCA
        );
        double percentageOfTotal = calculationService.getPercentageOfTotal(specificExpense, totalExpenses);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                DespesaSimplificadaConstants.SEGURANCA,
                specificExpense,
                percentageOfTotal,
                UnidadesFederativasConstants.AM_ID_LONG
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    public void calculateAndSaveAM5(double totalExpenses) {
        double specificExpense = elementoDespesaRepository.findByUnidadeFederativaIdAndNamePoderAndTermsNotLike(
                UnidadesFederativasConstants.AM_ID_LONG,
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
                UnidadesFederativasConstants.AM_ID_LONG
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    public void calculateAndSaveAM6(double totalExpenses) {
        double specificExpense = elementoDespesaRepository.findByUnidadeFederativaIdAndNameMinisterioAndElementoDespesaNameList(
                UnidadesFederativasConstants.AM_ID_LONG,
                DespesaSimplificadaConstants.SECRETARIA_ESTADO_FAZENDA,
                DespesaSimplificadaConstants.DESPESAS_DIVIDA_PUBLICA
        );
        double percentageOfTotal = calculationService.getPercentageOfTotal(specificExpense, totalExpenses);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                DespesaSimplificadaConstants.DIVIDA_PUBLICA,
                specificExpense,
                percentageOfTotal,
                UnidadesFederativasConstants.AM_ID_LONG
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    public void calculateAndSaveAMOutros(double totalExpenses) {
        double simplifiedExpenses = despesaSimplificadaRepository.sumTotalValueByUnidadeFederativa(UnidadesFederativasConstants.AM_ID_LONG);
        double restExpenses = totalExpenses - simplifiedExpenses;
        double percentageOfTotal = calculationService.getPercentageOfTotal(restExpenses, totalExpenses);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                DespesaSimplificadaConstants.OUTROS,
                restExpenses,
                percentageOfTotal,
                UnidadesFederativasConstants.AM_ID_LONG
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

}
