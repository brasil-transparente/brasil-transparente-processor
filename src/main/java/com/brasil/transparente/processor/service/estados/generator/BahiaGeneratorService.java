package com.brasil.transparente.processor.service.estados.generator;

import com.brasil.transparente.processor.entity.Poder;
import com.brasil.transparente.processor.service.ExpenseGenerator;
import com.brasil.transparente.processor.service.creation.CreateEntityService;
import com.brasil.transparente.processor.service.creation.ProcessExpensesService;
import com.brasil.transparente.processor.util.PoderResolverService;
import com.brasil.transparente.processor.util.constants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class BahiaGeneratorService extends ExpenseGenerator {

    private final PoderResolverService poderResolverService;

    protected BahiaGeneratorService(CreateEntityService createEntityService,
                                    ProcessExpensesService processExpensesService,
                                    PoderResolverService poderResolverService) {
        super(createEntityService, processExpensesService);
        this.poderResolverService = poderResolverService;
    }

    @Override
    protected void processLine(List<Poder> poderList, List<String> refinedLine) {
        String poderString = refinedLine.get(5);
        String ministerio = refinedLine.get(7);
        String orgao = refinedLine.get(11);
        String unidadeGestora = refinedLine.get(12);
        String elementoDespesa = refinedLine.get(40);
        String valorString = refinedLine.get(59);
        valorString = valorString.replace(",", ".");
        double valorPagoFinal = Double.parseDouble(valorString);

        if (Objects.equals(valorPagoFinal, Constants.ZERO_DOUBLE)
                || Constants.PRINCIPAL_DIVIDA_AMORTIZACAO.contains(elementoDespesa)) {
            return;
        }

        Poder poder = poderResolverService.definePoder(poderString, poderList);
        createEntitiesAndUpdateValues(poder, ministerio, orgao, unidadeGestora, elementoDespesa, valorPagoFinal);
    }

}
