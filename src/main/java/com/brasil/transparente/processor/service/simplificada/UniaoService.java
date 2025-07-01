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
public class UniaoService {

    private final CalculationService calculationService;
    private final ElementoDespesaRepository elementoDespesaRepository;
    private final DespesaSimplificadaRepository despesaSimplificadaRepository;

    public void calculateAndSaveUniao1(double totalExpense) {
        double specificExpense = elementoDespesaRepository.findByElementoDespesaNameListOrNameOrgaoAndUnidadeFederativa
                (DespesaSimplificadaConstants.DESPESAS_APOSENTADORIAS,
                        DespesaSimplificadaConstants.FRGPS,
                        UnidadesFederativasConstants.UN_ID_LONG);
        double percentageOfTotal = calculationService.getPercentageOfTotal(specificExpense, totalExpense);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                DespesaSimplificadaConstants.APOSENTADORIAS_PENSOES,
                specificExpense,
                percentageOfTotal,
                UnidadesFederativasConstants.UN_ID_LONG
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    public void calculateAndSaveUniao2(double totalExpense) {
        double specificExpense = elementoDespesaRepository.findByMinisterioAndElementoDespesaElementoDespesaNameListAndUnidadeFederativa(
                DespesaSimplificadaConstants.MINISTERIO_FAZENDA,
                DespesaSimplificadaConstants.DESPESAS_DIVIDA_PUBLICA,
                UnidadesFederativasConstants.UN_ID_LONG);
        double percentageOfTotal = calculationService.getPercentageOfTotal(specificExpense, totalExpense);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                DespesaSimplificadaConstants.JUROS_DIVIDA_PUBLICA,
                specificExpense,
                percentageOfTotal,
                UnidadesFederativasConstants.UN_ID_LONG
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    public void calculateAndSaveUniao3(double totalExpense) {
        double specificExpense = elementoDespesaRepository.findByMinisterioAndNotInElementoDespesaElementoDespesaNameListAndUnidadeFederativa(
                DespesaSimplificadaConstants.MINISTERIO_SAUDE,
                DespesaSimplificadaConstants.DESPESAS_APOSENTADORIAS,
                UnidadesFederativasConstants.UN_ID_LONG
        );
        double percentageOfTotal = calculationService.getPercentageOfTotal(specificExpense, totalExpense);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                DespesaSimplificadaConstants.SAUDE,
                specificExpense,
                percentageOfTotal,
                UnidadesFederativasConstants.UN_ID_LONG
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    public void calculateAndSaveUniao4(double totalExpense) {
        double specificExpense = elementoDespesaRepository.findByMinisterioAndNotInElementoDespesaElementoDespesaNameListAndInElementoDespesaNameListAndUnidadeFederativa(
                DespesaSimplificadaConstants.MINISTERIO_ASSISTENCIA_SOCIAL,
                DespesaSimplificadaConstants.DESPESAS_APOSENTADORIAS,
                DespesaSimplificadaConstants.AUXILIOS,
                UnidadesFederativasConstants.UN_ID_LONG
        );
        double percentageOfTotal = calculationService.getPercentageOfTotal(specificExpense, totalExpense);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                DespesaSimplificadaConstants.TRANSFERENCIA_RENDA,
                specificExpense,
                percentageOfTotal,
                UnidadesFederativasConstants.UN_ID_LONG
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    public void calculateAndSaveUniao5(double totalExpense) {
        double specificExpense = elementoDespesaRepository.findByMinisterioAndNotInElementoDespesaElementoDespesaNameListAndUnidadeFederativa(
                DespesaSimplificadaConstants.MINISTERIO_EDUCACAO,
                DespesaSimplificadaConstants.DESPESAS_APOSENTADORIAS,
                UnidadesFederativasConstants.UN_ID_LONG
        );
        double percentageOfTotal = calculationService.getPercentageOfTotal(specificExpense, totalExpense);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                DespesaSimplificadaConstants.EDUCACAO,
                specificExpense,
                percentageOfTotal,
                UnidadesFederativasConstants.UN_ID_LONG
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    public void calculateAndSaveUniao6(double totalExpense) {
        double specificExpense = elementoDespesaRepository.findByMinisterioAndUnidadeFederativa(
                DespesaSimplificadaConstants.PRECATORIOS_RPVS,
                UnidadesFederativasConstants.UN_ID_LONG);
        double percentageOfTotal = calculationService.getPercentageOfTotal(specificExpense, totalExpense);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                DespesaSimplificadaConstants.PRECATORIOS_RPVS,
                specificExpense,
                percentageOfTotal,
                UnidadesFederativasConstants.UN_ID_LONG
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    public void calculateAndSaveUniao7(double totalExpense) {
        double specificExpense = elementoDespesaRepository.findByMinisterioAndNotInElementoDespesaElementoDespesaNameListAndUnidadeFederativa(
                DespesaSimplificadaConstants.MINISTERIO_DEFESA,
                DespesaSimplificadaConstants.DESPESAS_APOSENTADORIAS,
                UnidadesFederativasConstants.UN_ID_LONG);
        double percentageOfTotal = calculationService.getPercentageOfTotal(specificExpense, totalExpense);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                DespesaSimplificadaConstants.DEFESA,
                specificExpense,
                percentageOfTotal,
                UnidadesFederativasConstants.UN_ID_LONG
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    public void calculateAndSaveUniao8(double totalExpense) {
        double specificExpense = elementoDespesaRepository.findByNameElementoDespesaAndUnidadeFederativa(
                DespesaSimplificadaConstants.SEGURO_DESEMPREGO_ABONO_SALARIAL,
                UnidadesFederativasConstants.UN_ID_LONG);
        double percentageOfTotal = calculationService.getPercentageOfTotal(specificExpense, totalExpense);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                DespesaSimplificadaConstants.BENEFICIOS_TRABALHISTAS,
                specificExpense,
                percentageOfTotal,
                UnidadesFederativasConstants.UN_ID_LONG
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    public void calculateAndSaveUniao9(double totalExpense) {
        double specificExpense = elementoDespesaRepository.findByPoderAndNotInElementoDespesaElementoDespesaNameListAndUnidadeFederativa(
                DespesaSimplificadaConstants.PODER_JUDICIARIO,
                DespesaSimplificadaConstants.DESPESAS_APOSENTADORIAS,
                UnidadesFederativasConstants.UN_ID_LONG);
        double percentageOfTotal = calculationService.getPercentageOfTotal(specificExpense, totalExpense);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                DespesaSimplificadaConstants.PODER_JUDICIARIO_FEDERAL,
                specificExpense,
                percentageOfTotal,
                UnidadesFederativasConstants.UN_ID_LONG
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

    public void calculateAndSaveUniaoOutros(double totalExpense) {
        double simplifiedExpenses = despesaSimplificadaRepository.sumTotalValueByUnidadeFederativa(UnidadesFederativasConstants.UN_ID_LONG);
        double restExpenses = totalExpense - simplifiedExpenses;
        double percentageOfTotal = calculationService.getPercentageOfTotal(restExpenses, totalExpense);
        DespesaSimplificada despesaSimplificada = new DespesaSimplificada(
                DespesaSimplificadaConstants.OUTROS,
                restExpenses,
                percentageOfTotal,
                UnidadesFederativasConstants.UN_ID_LONG
        );
        despesaSimplificadaRepository.save(despesaSimplificada);
    }

}
