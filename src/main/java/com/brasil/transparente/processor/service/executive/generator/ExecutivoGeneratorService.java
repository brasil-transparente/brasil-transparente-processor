package com.brasil.transparente.processor.service.executive.generator;

import com.brasil.transparente.processor.entity.Poder;
import com.brasil.transparente.processor.service.creation.CreateEntityService;
import com.brasil.transparente.processor.service.creation.ProcessExpensesService;
import com.brasil.transparente.processor.service.ExpenseGenerator;
import com.brasil.transparente.processor.util.CurrencyConverterService;
import com.brasil.transparente.processor.util.NameCorrectorService;
import com.brasil.transparente.processor.util.constants.Constants;
import com.brasil.transparente.processor.util.constants.EmbaixadasConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class ExecutivoGeneratorService extends ExpenseGenerator {

    private final NameCorrectorService nameCorrectorService;
    private final CurrencyConverterService currencyConverterService;

    public ExecutivoGeneratorService(CreateEntityService createEntityService, ProcessExpensesService processExpensesService,
                                     NameCorrectorService nameCorrectorService, CurrencyConverterService currencyConverterService) {
        super(createEntityService, processExpensesService);
        this.nameCorrectorService = nameCorrectorService;
        this.currencyConverterService = currencyConverterService;
    }

    @Override
    protected void processLine(Poder poder, List<String> refinedLine) {
        String nameMinisterio = refinedLine.get(2);
        String nameOrgao = refinedLine.get(4);
        String nameUnidadeGestora = refinedLine.get(6);
        String nameUnidadeOrcamentaria = refinedLine.get(10);
        String nameGrupoDeDespesa = refinedLine.get(36);
        String nameElementoDespesa = refinedLine.get(38);
        String valorPagoString = refinedLine.get(43).replace(",", ".");
        String valorRestosAPagarString = refinedLine.get(46).replace(",", ".");

        double valorPagoFinal = Double.parseDouble(valorPagoString) + Double.parseDouble(valorRestosAPagarString);

        if (Objects.equals(valorPagoFinal, Constants.ZERO_DOUBLE)
                || Objects.equals(nameElementoDespesa, Constants.REPASSES)
                || Objects.equals(nameGrupoDeDespesa, Constants.AMORTIZACAO_DIVIDA)) {
            return;
        }

        if (Objects.equals(nameMinisterio,Constants.SEM_INFORMACAO)) {
            nameMinisterio = nameCorrectorService.replaceMinisteryName(nameUnidadeOrcamentaria);
            nameOrgao = Constants.SEM_INFORMACAO;
            nameUnidadeGestora = Constants.SEM_INFORMACAO;
        }

        if (Objects.equals(nameMinisterio, EmbaixadasConstants.MINISTERIO_RELACOES_EXTERIORES_ORIGINAL)) {
            valorPagoFinal = currencyConverterService.convertCurrency(nameUnidadeGestora, valorPagoFinal);
            nameUnidadeGestora = nameCorrectorService.mergeEmbassyNames(nameUnidadeGestora);
        }

        createEntitiesAndUpdateValues(poder, nameMinisterio, nameOrgao, nameUnidadeGestora, nameElementoDespesa, valorPagoFinal);
    }

}
