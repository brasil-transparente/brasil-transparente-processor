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
public class ParaibaGeneratorService extends ExpenseGenerator {

    private final PoderResolverService poderResolverService;

    protected ParaibaGeneratorService(CreateEntityService createEntityService,
                                      ProcessExpensesService processExpensesService,
                                      PoderResolverService poderResolverService) {
        super(createEntityService, processExpensesService);
        this.poderResolverService = poderResolverService;
    }

    @Override
    protected void processLine(List<Poder> poderList, List<String> refinedLine) {
        try {
            String poderString = refinedLine.get(2);
            String orgaoPrincipal = refinedLine.get(3);
            String elementoDespesa = refinedLine.get(7);
            String valorPagoString = refinedLine.get(17);

            valorPagoString = valorPagoString.trim();

            if (valorPagoString.startsWith("'")) {
                valorPagoString = valorPagoString.substring(1);
            }

            if (valorPagoString.startsWith("-")) {
                valorPagoString = valorPagoString.substring(1);
            }

            if (valorPagoString.isEmpty()) {
                return;
            }

            double valorPagoFinal = Double.parseDouble(valorPagoString);

            if (Objects.equals(valorPagoFinal, Constants.ZERO_DOUBLE)) {
                return;
            }

            Poder poder = poderResolverService.definePoder(poderString, poderList);
            String ministerio = orgaoPrincipal;
            String orgao = orgaoPrincipal;
            String unidadeGestora = orgaoPrincipal;

            createEntitiesAndUpdateValues(poder, ministerio, orgao, unidadeGestora, elementoDespesa, valorPagoFinal);

        } catch (NumberFormatException e) {
            log.warn("Não foi possível converter o valor para número. Linha pulada. Conteúdo da linha: {}", refinedLine);
        } catch (IndexOutOfBoundsException e) {
            log.warn("Linha com número inesperado de colunas. Linha pulada. Conteúdo da linha: {}", refinedLine);
        }
    }
}