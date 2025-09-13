package com.brasil.transparente.processor.service.executivo.generator;

import com.brasil.transparente.processor.entity.Poder;
import com.brasil.transparente.processor.entity.RenunciaFiscal;
import com.brasil.transparente.processor.entity.UnidadeFederativa;
import com.brasil.transparente.processor.service.ExpenseGenerator;
import com.brasil.transparente.processor.service.creation.CreateEntityService;
import com.brasil.transparente.processor.service.creation.ProcessExpensesService;
import com.brasil.transparente.processor.util.constants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class RenunciaFiscalGeneratorService extends ExpenseGenerator {

    private UnidadeFederativa unidadeFederativa;

    public RenunciaFiscalGeneratorService(CreateEntityService createEntityService, ProcessExpensesService processExpensesService) {
        super(createEntityService, processExpensesService);
    }

    public void generateRenunciasFiscais(UnidadeFederativa unidadeFederativa, String year) {
        log.info("Processando Renúncias Fiscais da União para o ano de {}...", year);
        this.unidadeFederativa = unidadeFederativa;

        String filePath = "RenunciaFiscal/" + year + ".csv";
        int expectedLineLength = 4;
        String delimiter = ";";

        super.generateExpenses(null, StandardCharsets.UTF_8, filePath, expectedLineLength, delimiter);
        log.info("Finalizado o processamento de Renúncias Fiscais para o ano {}.", year);
    }

    @Override
    protected void processLine(List<Poder> poderList, List<String> refinedLine) {
        try {
            String ano = refinedLine.get(0);
            String cnpj = refinedLine.get(1);
            String beneficiario = refinedLine.get(2);
            String valorRenunciadoStr = refinedLine.get(3);

            double valorRenunciado = Double.parseDouble(valorRenunciadoStr.replace(".", "").replace(",", "."));

            if (Objects.equals(valorRenunciado, Constants.ZERO_DOUBLE)) {
                return;
            }

            RenunciaFiscal renunciaFiscal = new RenunciaFiscal(ano, cnpj, beneficiario, valorRenunciado);
            renunciaFiscal.setUnidadeFederativa(this.unidadeFederativa);
            this.unidadeFederativa.getListRenunciaFiscal().add(renunciaFiscal);

        } catch (NumberFormatException e) {
            log.warn("Não foi possível converter o valor para número. Linha pulada. Conteúdo: {}", refinedLine);
        } catch (IndexOutOfBoundsException e) {
            log.warn("Linha com colunas insuficientes. Linha pulada. Conteúdo: {}", refinedLine);
        }
    }
}