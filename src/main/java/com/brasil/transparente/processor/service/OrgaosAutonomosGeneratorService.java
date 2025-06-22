package com.brasil.transparente.processor.service;

import com.brasil.transparente.processor.entity.*;
import com.brasil.transparente.processor.util.constants.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@RequiredArgsConstructor
@Slf4j
@Service
public class OrgaosAutonomosGeneratorService {

    private final CreatorService creatorService;
    private final LoggingService loggingService;
    private final DespesasProcessingService despesasProcessingService;

    @Value("${CSV_PATH}")
    private String csvPath;

    private static final String ORGAOS_AUTONOMOS = "Órgãos Autônomos";
    Poder poder = new Poder(ORGAOS_AUTONOMOS);
    private static final String TRIBUNAL_CONTAS_UNIAO = "Tribunal de Contas da União";
    private static final String DEFENSORIA_PUBLICA_UNIAO = "Defensoria Pública da União";
    private static final String MINISTERIO_PUBLICO_UNIAO = "Ministério Público da União";
    private static final String MINISTERIO_PUBLICO_FEDERAL = "Ministério Público Federal";
    private static final String MINISTERIO_PUBLICO_TRABALHO = "Ministério Público do Trabalho";
    private static final String MINISTERIO_PUBLICO_MILITAR = "Ministério Público Militar";
    private static final String MINISTERIO_PUBLICO_DF_TERRITORIOS = "Ministério Público do Distrito Federal e Territórios";
    private static final String ESCOLA_SUPERIOR_MINISTERIO_PUBLICO_UNIAO = "Escola Superior do Ministério Público da União";

    public Poder generateOrgaosAutonomos(String year) {
        log.info("Orgãos Autônomos - Iniciando");
        generateTribunalContasUniao(year);
        generateDefensoriaPublicaUniao();
        generateMinisterioPublico(MINISTERIO_PUBLICO_FEDERAL);
        generateMinisterioPublico(MINISTERIO_PUBLICO_TRABALHO);
        generateMinisterioPublico(MINISTERIO_PUBLICO_MILITAR);
        generateMinisterioPublico(MINISTERIO_PUBLICO_DF_TERRITORIOS);
        generateMinisterioPublico(ESCOLA_SUPERIOR_MINISTERIO_PUBLICO_UNIAO);
        despesasProcessingService.aggregateAllPowerSpending(poder);
        log.info("Órgãos Autônomos - Finalizado");
        return poder;
    }

    private void generateTribunalContasUniao(String year) {
        int month = 1;
        while (month <= 12) {
            String monthString = String.format("%02d", month);
            String documentNumber = year + monthString;
            String relativePath = "/Orgaos Autonomos/Tribunal de Contas da Uniao/";
            String fileName = documentNumber + ".csv";
            String filePath = Paths.get(csvPath, relativePath, fileName).toString();
            String delimiter = ";";
            createTribunalContasUniaoStructure(filePath, delimiter, month);
            month++;
        }
    }

    private void generateDefensoriaPublicaUniao() {
        String relativePath = "/Orgaos Autonomos/Defensoria Publica da Uniao/2024.csv";
        String filePath = Paths.get(csvPath, relativePath).toString();
        String delimiter = ";";
        createDefensoriaPublicaUniaoStructure(filePath, delimiter);
    }

    private void generateMinisterioPublico(String nameOrgao) {
        String relativePath = "/Orgaos Autonomos/Ministerio Publico/" + nameOrgao + "/2024.csv";
        String filePath = Paths.get(csvPath, relativePath).toString();
        String delimiter = ";";
        createMinisterioPublicoUniaoStructure(filePath, delimiter, nameOrgao);
    }

    private void createTribunalContasUniaoStructure(String filePath, String delimiter, int month) {
        log.info("{} - Lendo arquivos de despesas e criando estrutura de despesa. Mês = {}", OrgaosAutonomosGeneratorService.TRIBUNAL_CONTAS_UNIAO, month);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] rawList = line.split(delimiter);
                if (rawList.length == 14) {
                    List<String> refinedList = new ArrayList<>();
                    for (String column : rawList) {
                        refinedList.add(column.replace("\"", "").trim());
                    }
                    String unidadeGestora = refinedList.get(1);
                    String valorString = refinedList.get(5);
                    String elementoDespesa = refinedList.get(12);

                    if (Objects.equals(valorString, "-")) {
                        continue;
                    }

                    valorString = valorString.replace(".", "");
                    valorString = valorString.replace(",", ".");
                    valorString = valorString.replace("R$", "");
                    double valorPago = Double.parseDouble(valorString);

                    if (Objects.equals(valorPago, Constants.ZERO_DOUBLE)) {
                        continue;
                    }

                    Ministerio ministerioReceived = creatorService.findOrCreateMinisterio(TRIBUNAL_CONTAS_UNIAO, poder);
                    Orgao orgaoReceived = creatorService.findOrCreateOrgao(TRIBUNAL_CONTAS_UNIAO, ministerioReceived);
                    UnidadeGestora unidadeGestoraReceived = creatorService.findOrCreateUnidadeGestora(unidadeGestora, orgaoReceived);
                    ElementoDespesa elementoDespesaReceived = creatorService.findOrCreateNewElementoDespesa(elementoDespesa, unidadeGestoraReceived);

                    despesasProcessingService.updateTotalValueSpent(ministerioReceived, orgaoReceived, unidadeGestoraReceived, elementoDespesaReceived, valorPago);

                } else {
                    loggingService.logInvalidLine(line);
                }
            }
        } catch (IOException e) {
            loggingService.logExceptionMainFile(e);
        } catch (NumberFormatException e) {
            loggingService.logNumberFormatException(e);
        }
    }

    private void createDefensoriaPublicaUniaoStructure(String filePath, String delimiter) {
        log.info("Lendo Defensoria Publica da Uniao e criando estrutura de dados");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            br.readLine();
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
                    double valor = Double.parseDouble(valorString);

                    if (Objects.equals(valor, Constants.ZERO_DOUBLE)) {
                        continue;
                    }

                    Ministerio ministerioReceived = creatorService.findOrCreateMinisterio(DEFENSORIA_PUBLICA_UNIAO, poder);
                    Orgao orgaoReceived = creatorService.findOrCreateOrgao(DEFENSORIA_PUBLICA_UNIAO, ministerioReceived);
                    UnidadeGestora unidadeGestoraReceived = creatorService.findOrCreateUnidadeGestora(DEFENSORIA_PUBLICA_UNIAO, orgaoReceived);
                    ElementoDespesa elementoDespesaReceived = creatorService.findOrCreateNewElementoDespesa(elementoDespesa, unidadeGestoraReceived);

                    despesasProcessingService.updateTotalValueSpent(ministerioReceived, orgaoReceived, unidadeGestoraReceived, elementoDespesaReceived, valor);

                } else {
                    loggingService.logInvalidLine(line);
                }
            }
        } catch (IOException e) {
            loggingService.logExceptionMainFile(e);
        } catch (NumberFormatException e) {
            loggingService.logNumberFormatException(e);
        }
    }

    private void createMinisterioPublicoUniaoStructure(String filePath, String delimiter, String nameOrgao) {
        log.info("Lendo {} e criando estrutura de dados", nameOrgao);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            br.readLine();
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

                    Ministerio ministerioReceived = creatorService.findOrCreateMinisterio(MINISTERIO_PUBLICO_UNIAO, poder);
                    Orgao orgaoReceived = creatorService.findOrCreateOrgao(nameOrgao, ministerioReceived);
                    UnidadeGestora unidadeGestoraReceived = creatorService.findOrCreateUnidadeGestora(nameOrgao, orgaoReceived);
                    ElementoDespesa elementoDespesaReceived = creatorService.findOrCreateNewElementoDespesa(elementoDespesa, unidadeGestoraReceived);

                    despesasProcessingService.updateTotalValueSpent(ministerioReceived, orgaoReceived, unidadeGestoraReceived, elementoDespesaReceived, valor);

                } else {
                    loggingService.logInvalidLine(line);
                }
            }
        } catch (IOException e) {
            loggingService.logExceptionMainFile(e);
        } catch (NumberFormatException e) {
            loggingService.logNumberFormatException(e);
        }
    }

}
