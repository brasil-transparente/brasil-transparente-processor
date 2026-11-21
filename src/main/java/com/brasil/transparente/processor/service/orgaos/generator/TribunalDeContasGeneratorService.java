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
public class TribunalDeContasGeneratorService extends ExpenseGenerator {

    private static final String TRIBUNAL_CONTAS_UNIAO = "Tribunal de Contas da União";

    public TribunalDeContasGeneratorService(CreateEntityService createEntityService, ProcessExpensesService processExpensesService) {
        super(createEntityService, processExpensesService);
    }

    @Override
    protected void processLine(Poder poder, List<String> refinedLine) {
        String unidadeGestora = refinedLine.get(1);
        String valorString = refinedLine.get(5);
        String elementoDespesa = refinedLine.get(12);

        if (Objects.equals(valorString, "-")) {
            return;
        }

        valorString = valorString.replace(".", "");
        valorString = valorString.replace(",", ".");
        valorString = valorString.replace("R$", "");
        double valorPagoFinal = Double.parseDouble(valorString);

        if (Objects.equals(valorPagoFinal, Constants.ZERO_DOUBLE)) {
            return;
        }

        createEntitiesAndUpdateValues(poder, TRIBUNAL_CONTAS_UNIAO, TRIBUNAL_CONTAS_UNIAO, unidadeGestora, elementoDespesa, valorPagoFinal);
    }

}
