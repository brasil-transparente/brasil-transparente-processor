package com.brasil.transparente.processor.service;

import com.brasil.transparente.processor.entity.*;
import com.brasil.transparente.processor.repository.UnidadeFederativaRepository;
import com.brasil.transparente.processor.util.constants.Constants;
import com.brasil.transparente.processor.util.OrdererService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
@Service
public class GeneralGeneratorService {

    private final OrdererService ordererService;
    private final UnidadeFederativaRepository unidadeFederativaRepository;

    public Ministerio findOrCreateMinisterio(String nameMinisterio, Poder poder) {
        for (Ministerio ministerio : poder.getListMinisterio()) {
            if (Objects.equals(nameMinisterio, ministerio.getNameMinisterio())) {
                return ministerio;
            }
        }
        return createMinisterio(nameMinisterio, poder);
    }

    private Ministerio createMinisterio(String nameMinisterioLine, Poder poder) {
        Ministerio newMinisterio = new Ministerio(nameMinisterioLine);
        poder.getListMinisterio().add(newMinisterio);
        return newMinisterio;
    }

    public Orgao findOrCreateOrgao(String nameOrgao, Ministerio ministerio) {
        if (!ministerio.getListOrgao().isEmpty()) {
            for (Orgao orgao : ministerio.getListOrgao()) {
                if (Objects.equals(nameOrgao, orgao.getNameOrgao())) {
                    return orgao;
                }
            }
        }
        return createOrgao(nameOrgao, ministerio);
    }

    private Orgao createOrgao(String nameOrgao, Ministerio ministerio) {
        Orgao newOrgao = new Orgao(nameOrgao);
        ministerio.getListOrgao().add(newOrgao);
        return newOrgao;
    }

    public UnidadeGestora findOrCreateUnidadeGestora(String nameUnidadeGestora, Orgao orgao) {
        if (!orgao.getListUnidadeGestora().isEmpty()) {
            for (UnidadeGestora unidadeGestora : orgao.getListUnidadeGestora()) {
                if (Objects.equals(nameUnidadeGestora, unidadeGestora.getNameUnidadeGestora())) {
                    return unidadeGestora;
                }
            }
        }
        return createUnidadeGestora(nameUnidadeGestora, orgao);
    }

    private UnidadeGestora createUnidadeGestora(String nameUnidadeGestora, Orgao orgao) {
        UnidadeGestora newUnidadeGestora = new UnidadeGestora(nameUnidadeGestora);
        orgao.getListUnidadeGestora().add(newUnidadeGestora);
        return newUnidadeGestora;
    }

    public ElementoDespesa findOrCreateNewElementoDespesa(String nameElementoDespesaLine, UnidadeGestora unidadeGestora) {
        if (!unidadeGestora.getListElementoDespesa().isEmpty()) {
            for (ElementoDespesa elementoDespesa : unidadeGestora.getListElementoDespesa()) {
                if (Objects.equals(nameElementoDespesaLine, elementoDespesa.getNameElementoDespesa())) {
                    return elementoDespesa;
                }
            }
        }
        return createElementoDespesa(nameElementoDespesaLine, unidadeGestora);
    }

    private ElementoDespesa createElementoDespesa(String nameElementoDespesaLine, UnidadeGestora unidadeGestora) {
        ElementoDespesa elementoDespesa = new ElementoDespesa(nameElementoDespesaLine);
        unidadeGestora.getListElementoDespesa().add(elementoDespesa);
        return elementoDespesa;
    }

    public void updateTotalValueSpent(Ministerio ministerioReceived, Orgao orgaoReceived, UnidadeGestora unidadeGestoraReceived, ElementoDespesa elementoDespesaReceived, double valorPago) {
        ministerioReceived.setTotalValueSpent(ministerioReceived.getTotalValueSpent() + valorPago);
        orgaoReceived.setTotalValueSpent(orgaoReceived.getTotalValueSpent() + valorPago);
        unidadeGestoraReceived.setTotalValueSpent(unidadeGestoraReceived.getTotalValueSpent() + valorPago);
        elementoDespesaReceived.setTotalValueSpent(elementoDespesaReceived.getTotalValueSpent() + valorPago);
    }

    public void aggregateAllPowerSpending(Poder poder) {
        log.info("{} - Agregando todas as despesas", poder.getNamePoder());
        double despesaTotalPoder = 0;
        for (Ministerio ministerio : poder.getListMinisterio()) {
            despesaTotalPoder += ministerio.getTotalValueSpent();
        }
        poder.setTotalValueSpent(despesaTotalPoder);
    }

    public double aggregateTotalExpense(UnidadeFederativa unidadeFederativa) {
        log.info("Calculando gasto total");
        double gastoTotalValue = 0;
        for (Poder poder : unidadeFederativa.getListPoder()) {
            gastoTotalValue += poder.getTotalValueSpent();
        }
        unidadeFederativa.setTotalValueSpent(gastoTotalValue);
        return gastoTotalValue;
    }

    public void setRelationships(UnidadeFederativa unidadeFederativa) {
        log.info("{} - Criando relacionamentos entre as entidades", unidadeFederativa.getNameUnidadeFederativa());
        for (Poder poder : unidadeFederativa.getListPoder()) {
            for (Ministerio ministerio : poder.getListMinisterio()) {
                for (Orgao orgao : ministerio.getListOrgao()) {
                    for (UnidadeGestora unidadeGestora : orgao.getListUnidadeGestora()) {
                        for (ElementoDespesa elementoDespesa : unidadeGestora.getListElementoDespesa()) {
                            elementoDespesa.setUnidadeGestora(unidadeGestora);
                        }
                        unidadeGestora.setOrgao(orgao);
                    }
                    orgao.setMinisterio(ministerio);
                }
                ministerio.setPoder(poder);
            }
            poder.setUnidadeFederativa(unidadeFederativa);
        }
    }

    public void removeNegativeOrZeroExpenses(List<Poder> poderList) {
        log.info("Removendo despesas negativas e despesas zeradas");
        double discountedValue;
        for (Poder poder : poderList) {
            for (Ministerio ministerio : poder.getListMinisterio()) {
                for (Orgao orgao : ministerio.getListOrgao()) {
                    for (UnidadeGestora unidadeGestora : orgao.getListUnidadeGestora()) {
                        Iterator<ElementoDespesa> iterator = unidadeGestora.getListElementoDespesa().iterator();
                        while (iterator.hasNext()) {
                            ElementoDespesa elementoDespesa = iterator.next();
                            if (elementoDespesa.getTotalValueSpent() <= Constants.LESS_THAN_ONE_CENT) {
                                discountedValue = elementoDespesa.getTotalValueSpent();
                                double newUGExpense = unidadeGestora.getTotalValueSpent() + discountedValue;
                                double newOrgaoExpense = orgao.getTotalValueSpent() + discountedValue;
                                double newMinisterioExpense = ministerio.getTotalValueSpent() + discountedValue;
                                double newPoderExpense = poder.getTotalValueSpent() + discountedValue;
                                iterator.remove();
                                unidadeGestora.setTotalValueSpent(newUGExpense);
                                orgao.setTotalValueSpent(newOrgaoExpense);
                                ministerio.setTotalValueSpent(newMinisterioExpense);
                                poder.setTotalValueSpent(newPoderExpense);
                            }
                        }
                    }
                }
            }
        }
    }

    public void setTotalPercentages(List<Poder> poderList, double gastoTotalValue) {
        log.info("Calculando porcentagens finais");
        poderList = ordererService.orderBySpending(poderList);
        Iterator<Poder> poderIterator = poderList.iterator();
        while (poderIterator.hasNext()) {
            Poder poder = poderIterator.next();
            if (Objects.equals(poder.getTotalValueSpent(), Constants.ZERO_DOUBLE)) {
                poderIterator.remove();
                continue;
            } else {
                double poderPercentage = (poder.getTotalValueSpent() * 100) / gastoTotalValue;
                poder.setPercentageOfTotal(poderPercentage);
            }

            List<Ministerio> ministerioList = ordererService.orderBySpending(poder.getListMinisterio());
            Iterator<Ministerio> ministerioIterator = ministerioList.iterator();
            while (ministerioIterator.hasNext()) {
                Ministerio ministerio = ministerioIterator.next();
                if (Objects.equals(ministerio.getTotalValueSpent(), Constants.ZERO_DOUBLE)) {
                    ministerioIterator.remove();
                    continue;
                } else {
                    double ministerioPercentage = (ministerio.getTotalValueSpent() * 100) / poder.getTotalValueSpent();
                    ministerio.setPercentageOfTotal(ministerioPercentage);
                }

                List<Orgao> orderedListOrgao = ordererService.orderBySpending(ministerio.getListOrgao());
                Iterator<Orgao> orgaoIterator = orderedListOrgao.iterator();
                while (orgaoIterator.hasNext()) {
                    Orgao orgao = orgaoIterator.next();
                    if (Objects.equals(orgao.getTotalValueSpent(), Constants.ZERO_DOUBLE)) {
                        orgaoIterator.remove();
                        continue;
                    } else {
                        double orgaoPercentage = (orgao.getTotalValueSpent() * 100) / ministerio.getTotalValueSpent();
                        orgao.setPercentageOfTotal(orgaoPercentage);
                    }

                    List<UnidadeGestora> orderedListUnidadeGestora = ordererService.orderBySpending(orgao.getListUnidadeGestora());
                    Iterator<UnidadeGestora> unidadeGestoraIterator = orderedListUnidadeGestora.iterator();
                    while (unidadeGestoraIterator.hasNext()) {
                        UnidadeGestora unidadeGestora = unidadeGestoraIterator.next();
                        if (Objects.equals(unidadeGestora.getTotalValueSpent(), Constants.ZERO_DOUBLE)) {
                            unidadeGestoraIterator.remove();
                            continue;
                        } else {
                            double unidadeGestoraPercentage = (unidadeGestora.getTotalValueSpent() * 100) / unidadeGestora.getTotalValueSpent();
                            unidadeGestora.setPercentageOfTotal(unidadeGestoraPercentage);
                        }
                        double unidadeGestoraPercentage = (unidadeGestora.getTotalValueSpent() * 100) / orgao.getTotalValueSpent();
                        unidadeGestora.setPercentageOfTotal(unidadeGestoraPercentage);

                        List<ElementoDespesa> orderedListElementoDespesa = ordererService.orderBySpending(unidadeGestora.getListElementoDespesa());
                        Iterator<ElementoDespesa> elementoDespesaIterator = orderedListElementoDespesa.iterator();
                        while (elementoDespesaIterator.hasNext()) {
                            ElementoDespesa elementoDespesa = elementoDespesaIterator.next();
                            if (Objects.equals(elementoDespesa.getTotalValueSpent(), Constants.ZERO_DOUBLE)) {
                                elementoDespesaIterator.remove();
                            } else {
                                double elementoDespesaPercentage = (elementoDespesa.getTotalValueSpent() * 100) / unidadeGestora.getTotalValueSpent();
                                elementoDespesa.setPercentageOfTotal(elementoDespesaPercentage);
                            }
                        }
                    }
                }
            }
        }
    }

    public void saveStructure(UnidadeFederativa unidadeFederativa) {
        log.info("Salvando no banco de dados");
        unidadeFederativaRepository.save(unidadeFederativa);
    }

    public void logInvalidLine(String line) {
        log.error("Linha inválida = {}", line);
    }

    public void logExceptionMainFile(IOException e) {
        log.error("Exceção durante leitura do arquivo principal = {}", String.valueOf(e));
    }

    public void logNumberFormatException(NumberFormatException e) {
        log.error("Formato de número inválido = {} ", String.valueOf(e));
    }

}
