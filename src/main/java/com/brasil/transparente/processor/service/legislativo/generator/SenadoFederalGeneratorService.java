package com.brasil.transparente.processor.service.legislativo.generator;

import com.brasil.transparente.processor.entity.Poder;
import com.brasil.transparente.processor.service.creation.CreateEntityService;
import com.brasil.transparente.processor.service.creation.ProcessExpensesService;
import com.brasil.transparente.processor.service.ExpenseGenerator;
import com.brasil.transparente.processor.util.constants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class SenadoFederalGeneratorService extends ExpenseGenerator {

    private static final String SENADO_FEDERAL = "Senado Federal";

    public SenadoFederalGeneratorService(CreateEntityService createEntityService, ProcessExpensesService processExpensesService) {
        super(createEntityService, processExpensesService);
    }

    @Override
    protected void processLine(Poder poder, List<String> refinedLine) {
        String elementoDespesa = refinedLine.get(0);
        String valorPagoString = refinedLine.get(1);
        String valorRestorPagarString = refinedLine.get(2);
        valorPagoString = valorPagoString.replace(",", "");
        valorRestorPagarString = valorRestorPagarString.replace(",", "");
        double valorPago = Double.parseDouble(valorPagoString);
        double valorRestosPagar = Double.parseDouble(valorRestorPagarString);
        double valorPagoFinal = valorPago + valorRestosPagar;

        if (Objects.equals(valorPagoFinal, Constants.ZERO_DOUBLE)) {
            return;
        }

        createEntitiesAndUpdateValues(poder, SENADO_FEDERAL, SENADO_FEDERAL, SENADO_FEDERAL, elementoDespesa, valorPagoFinal);
    }

}
