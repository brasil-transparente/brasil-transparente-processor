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
public class EscolaMinisterioPublicoGeneratorService extends ExpenseGenerator {

    private static final String MINISTERIO_PUBLICO_UNIAO = "Ministério Público da União";
    private static final String ESCOLA_SUPERIOR_MINISTERIO_PUBLICO_UNIAO = "Escola Superior do Ministério Público da União";

    public EscolaMinisterioPublicoGeneratorService(CreateEntityService createEntityService, ProcessExpensesService processExpensesService) {
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

        createEntitiesAndUpdateValues(poderList.getFirst(), MINISTERIO_PUBLICO_UNIAO, ESCOLA_SUPERIOR_MINISTERIO_PUBLICO_UNIAO, ESCOLA_SUPERIOR_MINISTERIO_PUBLICO_UNIAO, elementoDespesa, valorPagoFinal);
    }

}
