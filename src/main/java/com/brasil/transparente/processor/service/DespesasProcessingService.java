package com.brasil.transparente.processor.service;

import com.brasil.transparente.processor.entity.*;
import com.brasil.transparente.processor.util.OrdererService;
import com.brasil.transparente.processor.util.constants.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DespesasProcessingService {

    private final OrdererService ordererService;

    public double aggregateTotalExpense(UnidadeFederativa unidadeFederativa) {
        log.info("Calculando gasto total");
        double gastoTotalValue = 0;
        for (Poder poder : unidadeFederativa.getListPoder()) {
            gastoTotalValue += poder.getTotalValueSpent();
        }
        unidadeFederativa.setTotalValueSpent(gastoTotalValue);
        return gastoTotalValue;
    }

    public void aggregateAllPowerSpending(Poder poder) {
        log.info("{} - Agregando todas as despesas", poder.getNamePoder());
        double despesaTotalPoder = 0;
        for (Ministerio ministerio : poder.getListMinisterio()) {
            despesaTotalPoder += ministerio.getTotalValueSpent();
        }
        poder.setTotalValueSpent(despesaTotalPoder);
    }

    public void updateTotalValueSpent(Ministerio ministerioReceived, Orgao orgaoReceived, UnidadeGestora unidadeGestoraReceived, ElementoDespesa elementoDespesaReceived, double valorPago) {
        ministerioReceived.setTotalValueSpent(ministerioReceived.getTotalValueSpent() + valorPago);
        orgaoReceived.setTotalValueSpent(orgaoReceived.getTotalValueSpent() + valorPago);
        unidadeGestoraReceived.setTotalValueSpent(unidadeGestoraReceived.getTotalValueSpent() + valorPago);
        elementoDespesaReceived.setTotalValueSpent(elementoDespesaReceived.getTotalValueSpent() + valorPago);
    }

    public void removeNegativeOrZeroExpenses(List<Poder> poderList) {
        log.info("Removendo despesas negativas e despesas zeradas");
        for (Poder poder : poderList) {
            for (Ministerio ministerio : poder.getListMinisterio()) {
                for (Orgao orgao : ministerio.getListOrgao()) {
                    for (UnidadeGestora unidadeGestora : orgao.getListUnidadeGestora()) {
                        removeInvalidExpenses(unidadeGestora, orgao, ministerio, poder);
                    }
                }
            }
        }
    }

    private void removeInvalidExpenses(UnidadeGestora ug, Orgao orgao, Ministerio ministerio, Poder poder) {
        Iterator<ElementoDespesa> iterator = ug.getListElementoDespesa().iterator();
        while (iterator.hasNext()) {
            ElementoDespesa despesa = iterator.next();
            if (despesa.getTotalValueSpent() <= Constants.LESS_THAN_ONE_CENT) {
                double value = despesa.getTotalValueSpent();
                ug.setTotalValueSpent(ug.getTotalValueSpent() + value);
                orgao.setTotalValueSpent(orgao.getTotalValueSpent() + value);
                ministerio.setTotalValueSpent(ministerio.getTotalValueSpent() + value);
                poder.setTotalValueSpent(poder.getTotalValueSpent() + value);
                iterator.remove();
            }
        }
    }

    public void setTotalPercentages(List<Poder> poderList, double gastoTotalValue) {
        log.info("Calculando porcentagens finais");

        poderList = ordererService.orderBySpending(poderList);
        for (Poder poder : poderList) {
            setPercentage(poder, gastoTotalValue);

            List<Ministerio> ministerios = ordererService.orderBySpending(poder.getListMinisterio());
            for (Ministerio ministerio : ministerios) {
                setPercentage(ministerio, poder.getTotalValueSpent());

                List<Orgao> orgaos = ordererService.orderBySpending(ministerio.getListOrgao());
                for (Orgao orgao : orgaos) {
                    setPercentage(orgao, ministerio.getTotalValueSpent());

                    List<UnidadeGestora> unidadeGestoras = ordererService.orderBySpending(orgao.getListUnidadeGestora());
                    for (UnidadeGestora ug : unidadeGestoras) {
                        setPercentage(ug, orgao.getTotalValueSpent());

                        List<ElementoDespesa> despesas = ordererService.orderBySpending(ug.getListElementoDespesa());
                        for (ElementoDespesa despesa : despesas) {
                            setPercentage(despesa, ug.getTotalValueSpent());
                        }
                    }
                }
            }
        }
    }

    private void setPercentage(UnidadeOrcamentaria unidade, double totalPai) {
        double percentageOfTotal = calculatePercentage(unidade.getTotalValueSpent(), totalPai);
        unidade.setPercentageOfTotal(percentageOfTotal);
    }

    private double calculatePercentage(double numerator, double denominator) {
        return denominator == 0 ? 0 : (numerator * 100) / denominator;
    }
}
