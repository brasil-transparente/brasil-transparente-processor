package com.brasil.transparente.processor.service;

import com.brasil.transparente.processor.entity.*;
import com.brasil.transparente.processor.service.creation.CreateEntityService;
import com.brasil.transparente.processor.service.creation.ProcessExpensesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class ExpenseGenerator {

    @Value("${CSV_PATH}")
    private String csvPath;
    protected final CreateEntityService createEntityService;
    protected final ProcessExpensesService processExpensesService;

    protected ExpenseGenerator(CreateEntityService createEntityService, ProcessExpensesService processExpensesService) {
        this.createEntityService = createEntityService;
        this.processExpensesService = processExpensesService;
    }

    protected abstract void processLine(Poder poder, List<String> refinedLine);

    public final void generateExpenses(Poder poder, Charset charset, String filePath, int lineLength, String delimiter) {
        BufferedReader bufferedReader = createBufferedReader(charset, filePath);
        try (bufferedReader) {
            iterateThroughSingleFile(poder, bufferedReader, lineLength, delimiter);
        } catch (IOException e) {
            log.error("Erro ao iterar o arquivo", e);
            throw new RuntimeException(e);
        }
    }

    public final void generateExpensesByMonth(Poder poder, Charset charset, String filePath, int lineLength, String delimiter, String year) {
        for (int month = 1; month <= 12; month++) {
            String yearString = String.valueOf(year);
            String monthString = String.format("%02d", month);
            String documentNumber = yearString + monthString + ".csv";
            String documentPath = filePath + documentNumber;
            generateExpenses(poder, charset, documentPath, lineLength, delimiter);
        }
    }

    protected BufferedReader createBufferedReader(Charset charset, String filePath) {
        try {
            String completePath = Paths.get(csvPath, filePath).toString();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(completePath), charset));
            // Lê e descarta a primeira linha (cabeçalho)
            bufferedReader.readLine();
            return bufferedReader;
        } catch (FileNotFoundException e) {
            log.error("Arquivo não encontrado = {} ", String.valueOf(e));
            throw new RuntimeException(e);
        } catch (IOException e) {
            log.error("Erro ao ler o arquivo = {}", String.valueOf(e));
            throw new RuntimeException(e);
        }
    }

    protected void iterateThroughSingleFile(Poder poder, BufferedReader bufferedReader, int lineLength, String delimiter) {
        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] rawLine = line.split(delimiter);
                if (rawLine.length == lineLength) {
                    List<String> refinedLine = refineLine(rawLine);
                    processLine(poder, refinedLine);
                } else {
                    log.error("Linha inválida: esperados {} campos, encontrados {}", lineLength, rawLine.length);
                }
            }
        } catch (IOException e) {
            log.error("Erro na leitura do arquivo", e);
            throw new RuntimeException(e);
        }
    }

    protected List<String> refineLine(String[] rawLine) {
        List<String> refinedLine = new ArrayList<>();
        for (String column : rawLine) {
            refinedLine.add(column.replace("\"", "").trim());
        }
        return refinedLine;
    }

    protected void createEntitiesAndUpdateValues(Poder poder, String ministerio, String orgao, String unidadeGestora, String elementoDespesa, double valor) {
        Ministerio ministerioReceived = createEntityService.findOrCreateMinisterio(ministerio, poder);
        Orgao orgaoReceived = createEntityService.findOrCreateOrgao(orgao, ministerioReceived);
        UnidadeGestora unidadeGestoraReceived = createEntityService.findOrCreateUnidadeGestora(unidadeGestora, orgaoReceived);
        ElementoDespesa elementoDespesaReceived = createEntityService.findOrCreateNewElementoDespesa(elementoDespesa, unidadeGestoraReceived);
        processExpensesService.updateTotalValueSpent(ministerioReceived, orgaoReceived, unidadeGestoraReceived, elementoDespesaReceived, valor);
    }

}
