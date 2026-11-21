package com.brasil.transparente.processor.service.simplificada;

import com.brasil.transparente.processor.entity.DespesaSimplificada;
import com.brasil.transparente.processor.repository.DespesaSimplificadaRepository;
import com.brasil.transparente.processor.repository.ElementoDespesaRepository;
import com.brasil.transparente.processor.util.CalculationService;
import com.brasil.transparente.processor.util.constants.DespesaSimplificadaConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UniaoSimplificadaService {

    private final CalculationService calculationService;
    private final ElementoDespesaRepository elementoDespesaRepository;
    private final DespesaSimplificadaRepository despesaSimplificadaRepository;

    public void calculateAndSaveUniao1(double totalExpense) {
        save(DespesaSimplificadaConstants.APOSENTADORIAS_PENSOES,
                elementoDespesaRepository.findByElementoDespesaNameListOrNameOrgao(
                        DespesaSimplificadaConstants.DESPESAS_APOSENTADORIAS, DespesaSimplificadaConstants.FRGPS), totalExpense);
    }

    public void calculateAndSaveUniao2(double totalExpense) {
        save(DespesaSimplificadaConstants.JUROS_DIVIDA_PUBLICA,
                elementoDespesaRepository.findByMinisterioAndElementoDespesaNameList(
                        DespesaSimplificadaConstants.MINISTERIO_FAZENDA, DespesaSimplificadaConstants.DESPESAS_DIVIDA_PUBLICA), totalExpense);
    }

    public void calculateAndSaveUniao3(double totalExpense) {
        save(DespesaSimplificadaConstants.SAUDE,
                elementoDespesaRepository.findByMinisterioAndNotInElementoDespesaNameList(
                        DespesaSimplificadaConstants.MINISTERIO_SAUDE, DespesaSimplificadaConstants.DESPESAS_APOSENTADORIAS), totalExpense);
    }

    public void calculateAndSaveUniao4(double totalExpense) {
        save(DespesaSimplificadaConstants.TRANSFERENCIA_RENDA,
                elementoDespesaRepository.findByMinisterioAndNotInElementoDespesaNameListAndInElementoDespesaNameList(
                        DespesaSimplificadaConstants.MINISTERIO_ASSISTENCIA_SOCIAL,
                        DespesaSimplificadaConstants.DESPESAS_APOSENTADORIAS,
                        DespesaSimplificadaConstants.AUXILIOS), totalExpense);
    }

    public void calculateAndSaveUniao5(double totalExpense) {
        save(DespesaSimplificadaConstants.EDUCACAO,
                elementoDespesaRepository.findByMinisterioAndNotInElementoDespesaNameList(
                        DespesaSimplificadaConstants.MINISTERIO_EDUCACAO, DespesaSimplificadaConstants.DESPESAS_APOSENTADORIAS), totalExpense);
    }

    public void calculateAndSaveUniao6(double totalExpense) {
        save(DespesaSimplificadaConstants.PRECATORIOS_RPVS,
                elementoDespesaRepository.findByMinisterio(DespesaSimplificadaConstants.PRECATORIOS_RPVS), totalExpense);
    }

    public void calculateAndSaveUniao7(double totalExpense) {
        save(DespesaSimplificadaConstants.DEFESA,
                elementoDespesaRepository.findByMinisterioAndNotInElementoDespesaNameList(
                        DespesaSimplificadaConstants.MINISTERIO_DEFESA, DespesaSimplificadaConstants.DESPESAS_APOSENTADORIAS), totalExpense);
    }

    public void calculateAndSaveUniao8(double totalExpense) {
        save(DespesaSimplificadaConstants.SEGURO_DESEMPREGO_ABONO_SALARIAL,
                elementoDespesaRepository.findByNameElementoDespesaAndMinisterio(
                        DespesaSimplificadaConstants.SEGURO_DESEMPREGO_ABONO_SALARIAL,
                        DespesaSimplificadaConstants.MINISTERIO_TRABALHO_EMPREGO), totalExpense);
    }

    public void calculateAndSaveUniao9(double totalExpense) {
        save(DespesaSimplificadaConstants.PODER_JUDICIARIO_FEDERAL,
                elementoDespesaRepository.findByPoderAndNotInElementoDespesaNameList(
                        DespesaSimplificadaConstants.PODER_JUDICIARIO, DespesaSimplificadaConstants.DESPESAS_APOSENTADORIAS), totalExpense);
    }

    public void calculateAndSaveUniaoOutros(double totalExpense) {
        save(DespesaSimplificadaConstants.OUTROS, totalExpense - despesaSimplificadaRepository.sumTotalValue(), totalExpense);
    }

    private void save(String name, double value, double totalExpense) {
        despesaSimplificadaRepository.save(new DespesaSimplificada(
                name, value, calculationService.getPercentageOfTotal(value, totalExpense)));
    }
}
