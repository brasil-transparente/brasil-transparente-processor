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
public class ParaibaSimplificadaService {

    private final CalculationService calculationService;
    private final ElementoDespesaRepository elementoDespesaRepository;
    private final DespesaSimplificadaRepository despesaSimplificadaRepository;

    public void calculateAndSavePB1(double totalExpenses) {
        double specificExpense = elementoDespesaRepository.findByUnidadeFederativaAndTermsLikeAndListNameOrgao(
                UnidadesFederativasConstants.PB_ID_LONG,
                DespesaSimplificadaConstants.TERMO_APOSENTADORIA1,
                DespesaSimplificadaConstants.TERMO_APOSENTADORIA2,
                DespesaSimplificadaConstants.TERMO_APOSENTADORIA3,
                DespesaSimplificadaConstants.FUNDO_PREVIDENCIA_PARAIBA
        );

        double percentageOfTotal = calculationService.getPercentageOfTotal(specificExpense, totalExpenses);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                DespesaSimplificadaConstants.APOSENTADORIAS_PENSOES,
                specificExpense,
                percentageOfTotal,
                UnidadesFederativasConstants.PB_ID_LONG
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    public void calculateAndSavePB2(double totalExpenses) {
        double specificExpense = elementoDespesaRepository.findbyUnidadeFederativaIdAndMinisterioNameList(
                UnidadesFederativasConstants.PB_ID_LONG,
                DespesaSimplificadaConstants.SECRETARIA_EDUCACAO
        );

        double percentageOfTotal = calculationService.getPercentageOfTotal(specificExpense, totalExpenses);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                DespesaSimplificadaConstants.EDUCACAO,
                specificExpense,
                percentageOfTotal,
                UnidadesFederativasConstants.PB_ID_LONG
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    public void calculateAndSavePB3(double totalExpenses) {
        double specificExpense = elementoDespesaRepository.findbyUnidadeFederativaIdAndMinisterioNameList(
                UnidadesFederativasConstants.PB_ID_LONG,
                DespesaSimplificadaConstants.SECRETARIA_SAUDE
        );

        double percentageOfTotal = calculationService.getPercentageOfTotal(specificExpense, totalExpenses);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                DespesaSimplificadaConstants.SAUDE,
                specificExpense,
                percentageOfTotal,
                UnidadesFederativasConstants.PB_ID_LONG
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    public void calculateAndSavePB4(double totalExpenses) {
        double specificExpense = elementoDespesaRepository.findbyUnidadeFederativaIdAndMinisterioNameList(
                UnidadesFederativasConstants.PB_ID_LONG,
                DespesaSimplificadaConstants.SECRETARIAS_SEGURANCA
        );
        double percentageOfTotal = calculationService.getPercentageOfTotal(specificExpense, totalExpenses);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                DespesaSimplificadaConstants.SEGURANCA,
                specificExpense,
                percentageOfTotal,
                UnidadesFederativasConstants.PB_ID_LONG
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    public void calculateAndSavePB5(double totalExpenses) {
        double specificExpense = elementoDespesaRepository.findbyUnidadeFederativaIdAndMinisterioNameList(
                UnidadesFederativasConstants.PB_ID_LONG,
                DespesaSimplificadaConstants.SECRETARIAS_INFRAESTRUTURA
        );
        double percentageOfTotal = calculationService.getPercentageOfTotal(specificExpense, totalExpenses);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                DespesaSimplificadaConstants.INFRAESTRUTURA,
                specificExpense,
                percentageOfTotal,
                UnidadesFederativasConstants.PB_ID_LONG
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    public void calculateAndSavePB6(double totalExpenses) {
        double specificExpense = elementoDespesaRepository.findByUnidadeFederativaIdAndNamePoderAndTermsNotLike(
                UnidadesFederativasConstants.PB_ID_LONG,
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
                UnidadesFederativasConstants.PB_ID_LONG
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    public void calculateAndSavePBOutros(double totalExpenses) {
        double simplifiedExpenses = despesaSimplificadaRepository.sumTotalValueByUnidadeFederativa(UnidadesFederativasConstants.PB_ID_LONG);
        double restExpenses = totalExpenses - simplifiedExpenses;
        double percentageOfTotal = calculationService.getPercentageOfTotal(restExpenses, totalExpenses);

        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                DespesaSimplificadaConstants.OUTROS,
                restExpenses,
                percentageOfTotal,
                UnidadesFederativasConstants.PB_ID_LONG
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }
}