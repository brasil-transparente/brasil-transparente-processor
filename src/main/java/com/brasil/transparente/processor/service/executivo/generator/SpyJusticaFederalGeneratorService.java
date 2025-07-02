package com.brasil.transparente.processor.service.executivo.generator;

import com.brasil.transparente.processor.entity.Poder;
import com.brasil.transparente.processor.service.ExpenseGenerator;
import com.brasil.transparente.processor.service.creation.CreateEntityService;
import com.brasil.transparente.processor.service.creation.ProcessExpensesService;
import com.brasil.transparente.processor.util.constants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class SpyJusticaFederalGeneratorService extends ExpenseGenerator {

    private static final String PREC_RPV = "RPV";
    private static final String PRECATORIOS_RPVS = "Precatórios e Requisições de Pequeno Valor";

    public SpyJusticaFederalGeneratorService(CreateEntityService createEntityService, ProcessExpensesService processExpensesService) {
        super(createEntityService, processExpensesService);
    }

    @Override
    protected void processLine(List<Poder> poderList, List<String> refinedLine) {
        String unidadeOrcamentaria = refinedLine.get(4);
        String unidadeGestora = refinedLine.get(5);
        String elementoDespesa = refinedLine.get(6);
        String valorString = refinedLine.get(8);
        valorString = valorString.replace(",", ".");
        double valorPagoFinal = Double.parseDouble(valorString);

        if (!unidadeGestora.contains(PREC_RPV) || Objects.equals(valorPagoFinal, Constants.ZERO_DOUBLE)) {
            return;
        }

        createEntitiesAndUpdateValues(poderList.getFirst(), PRECATORIOS_RPVS, unidadeOrcamentaria, unidadeGestora, elementoDespesa, valorPagoFinal);
    }

}
