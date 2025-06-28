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
public class CamaraDeputadosGeneratorService extends ExpenseGenerator {

    private static final String CAMARA_DEPUTADOS = "Câmara dos Deputados";

    public CamaraDeputadosGeneratorService(CreateEntityService createEntityService, ProcessExpensesService processExpensesService) {
        super(createEntityService, processExpensesService);
    }

    @Override
    protected void processLine(Poder poder, List<String> refinedLine) {
        String elementoDespesa = refinedLine.get(0);
        String valorString = refinedLine.get(1);
        valorString = valorString.replace(",", "");
        double valorPagoFinal = Double.parseDouble(valorString);

        if (Objects.equals(valorPagoFinal, Constants.ZERO_DOUBLE)) {
            return;
        }

        createEntitiesAndUpdateValues(poder, CAMARA_DEPUTADOS, CAMARA_DEPUTADOS, CAMARA_DEPUTADOS, elementoDespesa, valorPagoFinal);
    }

}
