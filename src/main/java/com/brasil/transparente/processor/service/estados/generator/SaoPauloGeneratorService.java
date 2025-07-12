package com.brasil.transparente.processor.service.estados.generator;

import com.brasil.transparente.processor.entity.Poder;
import com.brasil.transparente.processor.service.ExpenseGenerator;
import com.brasil.transparente.processor.service.creation.CreateEntityService;
import com.brasil.transparente.processor.service.creation.ProcessExpensesService;
import com.brasil.transparente.processor.util.constants.Constants;
import com.brasil.transparente.processor.util.constants.estados.SPConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class SaoPauloGeneratorService extends ExpenseGenerator {

    protected SaoPauloGeneratorService(CreateEntityService createEntityService,
                                             ProcessExpensesService processExpensesService) {
        super(createEntityService, processExpensesService);
    }

    @Override
    protected void processLine(List<Poder> poderList, List<String> refinedLine) {
        String poderString = removerCodigoInicial(refinedLine.get(0));
        String ministerio = removerCodigoInicial(refinedLine.get(0));
        String orgao = removerCodigoInicial(refinedLine.get(1));
        String unidadeGestora = removerCodigoInicial(refinedLine.get(2));
        String elementoDespesa = removerCodigoInicial(refinedLine.get(8)).toUpperCase();
        String valorPago = refinedLine.get(9).replace(".", "").replace(",", ".");
        String valorPagoRestosPagar = refinedLine.get(10).replace(".", "").replace(",", ".");
        double valorPagoFinal = Double.parseDouble(valorPago) + Double.parseDouble(valorPagoRestosPagar);

        if (Objects.equals(valorPagoFinal, Constants.ZERO_DOUBLE) || SPConstants.TRANSFERENCIAS_MUNICIPIOS.contains(elementoDespesa)) {
            return;
        }

        Poder poder = definePoder(poderString, poderList);
        createEntitiesAndUpdateValues(poder, ministerio, orgao, unidadeGestora, elementoDespesa, valorPagoFinal);
    }

    public String removerCodigoInicial(String entrada) {
        return entrada.replaceFirst("^\\d+\\s*-\\s*", "").trim();
    }

    public Poder definePoder(String poderString, List<Poder> poderList) {
        return switch (poderString) {
            case SPConstants.ASSEMBLEIA_LEGISLATIVA -> poderList.get(1);
            case SPConstants.TRIBUNAL_DE_JUSTICA, SPConstants.TRIBUNAL_DE_JUSTICA_MILITAR -> poderList.get(2);
            case SPConstants.TRIBUNAL_DE_CONTAS_DO_ESTADO, SPConstants.DEFENSORIA_PUBLICA_DO_ESTADO,
                 SPConstants.MINISTERIO_PUBLICO -> poderList.get(3);
            default -> poderList.getFirst();
        };
    }

}
