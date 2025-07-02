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
public class SpySupremoTribunalFederalGeneratorService extends ExpenseGenerator {

    private static final String SENTENCAS_JUDICIAIS = "Sentenças Judiciais";
    private static final String PRECATORIOS_RPVS = "Precatórios e Requisições de Pequeno Valor";
    private static final String SUPREMO_TRIBUNAL_FEDERAL = "Supremo Tribunal Federal";

    public SpySupremoTribunalFederalGeneratorService(CreateEntityService createEntityService, ProcessExpensesService processExpensesService) {
        super(createEntityService, processExpensesService);
    }

    @Override
    protected void processLine(List<Poder> poderList, List<String> refinedLine) {
        String planoOrcamentario = refinedLine.get(4);
        String valorString = refinedLine.get(7);
        valorString = valorString.replace("R$", "");
        double valorPagoFinal = Double.parseDouble(valorString);

        if (!Objects.equals(planoOrcamentario,SENTENCAS_JUDICIAIS) || Objects.equals(valorPagoFinal, Constants.ZERO_DOUBLE)) {
            return;
        }

        createEntitiesAndUpdateValues(poderList.getFirst(), PRECATORIOS_RPVS, SUPREMO_TRIBUNAL_FEDERAL, SUPREMO_TRIBUNAL_FEDERAL, planoOrcamentario, valorPagoFinal);
    }

}
