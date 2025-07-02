package com.brasil.transparente.processor.service.orgaos.generator;

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
public class MinisterioPublicoDFGeneratorService extends ExpenseGenerator {

    private static final String MINISTERIO_PUBLICO_UNIAO = "Ministério Público da União";
    private static final String MINISTERIO_PUBLICO_DF_TERRITORIOS = "Ministério Público do Distrito Federal e Territórios";

    public MinisterioPublicoDFGeneratorService(CreateEntityService createEntityService, ProcessExpensesService processExpensesService) {
        super(createEntityService, processExpensesService);
    }

    @Override
    protected void processLine(List<Poder> poderList, List<String> refinedLine) {
        String elementoDespesa = refinedLine.get(0);
        String valorString = refinedLine.get(1);
        valorString = valorString.replace(",", "");
        double valorPagoFinal = Double.parseDouble(valorString);

        if (Objects.equals(valorPagoFinal, Constants.ZERO_DOUBLE)) {
            return;
        }

        createEntitiesAndUpdateValues(poderList.getFirst(), MINISTERIO_PUBLICO_UNIAO, MINISTERIO_PUBLICO_DF_TERRITORIOS, MINISTERIO_PUBLICO_DF_TERRITORIOS, elementoDespesa, valorPagoFinal);
    }

}
