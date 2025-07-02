package com.brasil.transparente.processor.service.judiciario.generator;

import com.brasil.transparente.processor.entity.*;
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
public class SupremoTribunalFederalGeneratorService extends ExpenseGenerator {

    public SupremoTribunalFederalGeneratorService(CreateEntityService createEntityService, ProcessExpensesService processExpensesService) {
        super(createEntityService, processExpensesService);
    }

    private static final String SENTENCAS_JUDICIAIS = "Sentenças Judiciais";
    private static final String SUPREMO_TRIBUNAL_FEDERAL = "Supremo Tribunal Federal";

    @Override
    protected void processLine(List<Poder> poderList, List<String> refinedLine) {
        String planoOrcamentario = refinedLine.get(4);
        String valorString = refinedLine.get(7);
        valorString = valorString.replace("R$", "");
        double valorPagoFinal = Double.parseDouble(valorString);

        if (Objects.equals(planoOrcamentario, SENTENCAS_JUDICIAIS) || Objects.equals(valorPagoFinal, Constants.ZERO_DOUBLE)) {
            return;
        }

        createEntitiesAndUpdateValues(poderList.getFirst(), SUPREMO_TRIBUNAL_FEDERAL, SUPREMO_TRIBUNAL_FEDERAL, SUPREMO_TRIBUNAL_FEDERAL, planoOrcamentario, valorPagoFinal);
    }
}
