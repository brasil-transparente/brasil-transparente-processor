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
public class RepasseGeneratorService extends ExpenseGenerator {

    private static final String REPASSE = "Repasse";

    public RepasseGeneratorService(CreateEntityService createEntityService, ProcessExpensesService processExpensesService) {
        super(createEntityService, processExpensesService);
    }

    @Override
    protected void processLine(List<Poder> poderList, List<String> refinedLine) {
        String nameElementoDespesa = refinedLine.get(38);
        String valorPagoString = refinedLine.get(43).replace(",", ".");
        String valorRestosAPagarString = refinedLine.get(46).replace(",", ".");

        double valorPagoFinal = Double.parseDouble(valorPagoString) + Double.parseDouble(valorRestosAPagarString);

        if (Objects.equals(nameElementoDespesa, Constants.REPASSES) && !Objects.equals(valorPagoFinal, Constants.ZERO_DOUBLE)) {
            createEntitiesAndUpdateValues(poderList.getFirst(), REPASSE, REPASSE, REPASSE, REPASSE, valorPagoFinal);
        }
    }

}
