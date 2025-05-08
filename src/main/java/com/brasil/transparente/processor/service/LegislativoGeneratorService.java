package com.brasil.transparente.processor.service;

import com.brasil.transparente.processor.entity.*;
import com.brasil.transparente.processor.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class LegislativoGeneratorService {

    @Autowired
    private GeneralGeneratorService generalGeneratorService;

    @Value("${CSV_PATH}")
    private String csvPath;

    private static final String LEGISLATIVO = "Poder Legislativo";
    Poder poder = new Poder(LEGISLATIVO);
    private static final String CAMARA_DEPUTADOS = "Câmara dos Deputados";
    private static final String SENADO_FEDERAL = "Senado Federal";

    public Poder generateLegislativeBranch() {
        log.info("Poder Legislativo - Iniciando");
        generateCamaraDeputados();
        generateSenado();
        generalGeneratorService.aggregateAllPowerSpending(poder);
        log.info("Poder Legislativo - Finalizado");
        return poder;
    }

    private void generateCamaraDeputados() {
        String relativePath = "Legislativo/CamaraDeputados.csv";
        String filePath = Paths.get(csvPath, relativePath).toString();
        String delimiter = "\t";
        createCamaraDeputadosStructure(filePath, delimiter);
    }

    private void generateSenado() {
        String relativePath = "Legislativo/Senado.csv";
        String filePath = Paths.get(csvPath, relativePath).toString();
        String delimiter = "\t";
        createSenadoFederalStructure(filePath, delimiter);
    }

    private void createCamaraDeputadosStructure(String filePath, String delimiter) {
        log.info("Camara de Deputados - Lendo arquivos de despesas e criando estrutura de despesa.");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String firstLine = br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] rawList = line.split(delimiter);
                if (rawList.length == 2) {
                    List<String> refinedList = new ArrayList<>();
                    for (String column : rawList) {
                        refinedList.add(column.replace("\"", "").trim());
                    }
                    String elementoDespesa = refinedList.get(0);
                    String valorString = refinedList.get(1);
                    valorString = valorString.replace(",", "");
                    double valor = Double.parseDouble(valorString);

                    if (Objects.equals(valor, Constants.ZERO_DOUBLE)) {
                        continue;
                    }

                    Ministerio ministerioReceived = generalGeneratorService.findOrCreateMinisterio(CAMARA_DEPUTADOS, poder);
                    Orgao orgaoReceived = generalGeneratorService.findOrCreateOrgao(CAMARA_DEPUTADOS, ministerioReceived);
                    UnidadeGestora unidadeGestoraReceived = generalGeneratorService.findOrCreateUnidadeGestora(CAMARA_DEPUTADOS, orgaoReceived);
                    ElementoDespesa elementoDespesaReceived = generalGeneratorService.findOrCreateNewElementoDespesa(elementoDespesa, unidadeGestoraReceived);

                    generalGeneratorService.updateTotalValueSpent(ministerioReceived, orgaoReceived, unidadeGestoraReceived, elementoDespesaReceived, valor);

                } else {
                    generalGeneratorService.logInvalidLine(line);
                }
            }
        } catch (IOException e) {
            generalGeneratorService.logExceptionMainFile(e);
        } catch (NumberFormatException e) {
            generalGeneratorService.logNumberFormatException(e);
        }
    }

    @SuppressWarnings("ExtractMethodRecommender")
    private void createSenadoFederalStructure(String filePath, String delimiter) {
        log.info("Senado Federal - Lendo arquivos de despesas e criando estrutura de despesa.");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String firstLine = br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] rawList = line.split(delimiter);
                if (rawList.length == 3) {
                    List<String> refinedList = new ArrayList<>();
                    for (String column : rawList) {
                        refinedList.add(column.replace("\"", "").trim());
                    }
                    String elementoDespesa = refinedList.get(0);
                    String valorPagoString = refinedList.get(1);
                    String valorRestorPagarString = refinedList.get(2);
                    valorPagoString = valorPagoString.replace(",", "");
                    valorRestorPagarString = valorRestorPagarString.replace(",", "");
                    double valorPago = Double.parseDouble(valorPagoString);
                    double valorRestosPagar = Double.parseDouble(valorRestorPagarString);
                    double valorFinal = valorPago + valorRestosPagar;

                    if (Objects.equals(valorFinal, Constants.ZERO_DOUBLE)) {
                        continue;
                    }

                    Ministerio ministerioReceived = generalGeneratorService.findOrCreateMinisterio(SENADO_FEDERAL, poder);
                    Orgao orgaoReceived = generalGeneratorService.findOrCreateOrgao(SENADO_FEDERAL, ministerioReceived);
                    UnidadeGestora unidadeGestoraReceived = generalGeneratorService.findOrCreateUnidadeGestora(SENADO_FEDERAL, orgaoReceived);
                    ElementoDespesa elementoDespesaReceived = generalGeneratorService.findOrCreateNewElementoDespesa(elementoDespesa, unidadeGestoraReceived);

                    generalGeneratorService.updateTotalValueSpent(ministerioReceived, orgaoReceived, unidadeGestoraReceived, elementoDespesaReceived, valorFinal);

                } else {
                    generalGeneratorService.logInvalidLine(line);
                }
            }
        } catch (IOException e) {
            generalGeneratorService.logExceptionMainFile(e);
        } catch (NumberFormatException e) {
            generalGeneratorService.logNumberFormatException(e);
        }
    }

}
