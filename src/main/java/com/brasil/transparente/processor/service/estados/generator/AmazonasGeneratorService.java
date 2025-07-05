package com.brasil.transparente.processor.service.estados.generator;

import com.brasil.transparente.processor.entity.Poder;
import com.brasil.transparente.processor.service.ExpenseGenerator;
import com.brasil.transparente.processor.service.creation.CreateEntityService;
import com.brasil.transparente.processor.service.creation.ProcessExpensesService;
import com.brasil.transparente.processor.util.PoderResolverService;
import com.brasil.transparente.processor.util.constants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class AmazonasGeneratorService extends ExpenseGenerator {

    private final PoderResolverService poderResolverService;

    protected AmazonasGeneratorService(CreateEntityService createEntityService,
                                       ProcessExpensesService processExpensesService,
                                       PoderResolverService poderResolverService) {
        super(createEntityService, processExpensesService);
        this.poderResolverService = poderResolverService;
    }

    @Override
    protected void processLine(List<Poder> poderList, List<String> refinedLine) {
        String poderString = refinedLine.get(8);
        String ministerio = refinedLine.get(4);
        String orgao = refinedLine.get(4);
        String unidadeGestora = refinedLine.get(4);
        String elementoDespesa = refinedLine.get(31);
        String valorPago = refinedLine.get(38).replace(",", ".");
        String valorPagoRestosPagar = refinedLine.get(40).replace(",", ".");
        double valorPagoFinal = Double.parseDouble(valorPago) + Double.parseDouble(valorPagoRestosPagar);

        if (Objects.equals(valorPagoFinal, Constants.ZERO_DOUBLE)) {
            return;
        }

        String ministerioRevisado = resolveMinisterio(ministerio);
        if (Objects.nonNull(ministerioRevisado)) {
            orgao = ministerio;
            ministerio = ministerioRevisado;
        }

        Poder poder = poderResolverService.definePoder(poderString, poderList);
        createEntitiesAndUpdateValues(poder, ministerio, orgao, unidadeGestora, elementoDespesa, valorPagoFinal);
    }

}
