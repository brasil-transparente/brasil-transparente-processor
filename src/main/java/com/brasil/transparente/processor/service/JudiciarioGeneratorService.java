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
public class JudiciarioGeneratorService {

    private final GeneralGeneratorService generalGeneratorService;

    @Value("${CSV_PATH}")
    private String csvPath;

    private static final String JUDICIARIO = "Poder Judiciário";
    Poder poder = new Poder(JUDICIARIO);
    private static final String SUPREMO_TRIBUNAL_FEDERAL = "Supremo Tribunal Federal";
    private static final String SUPERIOR_TRIBUNAL_JUSTICA = "Superior Tribunal de Justiça";
    private static final String JUSTICA_MILITAR = "Justiça Militar";
    private static final String JUSTICA_FEDERAL = "Justiça Federal";
    private static final String JUSTICA_ELEITORAL = "Justiça Eleitoral";
    private static final String JUSTICA_TRABALHO = "Justiça do Trabalho";
    private static final String JUSTICA_DF_TERRITORIOS = "Justiça do Distrito Federal e Territórios";
    private static final String CONSELHO_NACIONAL_JUSTICA = "Conselho Nacional de Justiça";
    private static final String SENTENCAS_JUDICIAIS = "Sentenças Judiciais";
    private static final String PREC_RPV = "RPV";

    public Poder generateJudiciaryBranch(String year) {
        log.info("Poder Judiciário - Iniciando");
        generateSuperiorTribunalFederal();
        generateStandardJustice(year, SUPERIOR_TRIBUNAL_JUSTICA);
        generateStandardJustice(year, JUSTICA_MILITAR);
        generateStandardJustice(year, JUSTICA_FEDERAL);
        generateStandardJustice(year, JUSTICA_ELEITORAL);
        generateStandardJustice(year, JUSTICA_TRABALHO);
        generateStandardJustice(year, JUSTICA_DF_TERRITORIOS);
        generateStandardJustice(year, CONSELHO_NACIONAL_JUSTICA);
        generalGeneratorService.aggregateAllPowerSpending(poder);
        log.info("Poder Judiciário - Finalizado");
        return poder;
    }

    private void generateSuperiorTribunalFederal() {
        String relativePath = "/Judiciario/Supremo Tribunal Federal/STF.csv";
        String filePath = Paths.get(csvPath, relativePath).toString();
        String delimiter = ",";
        createExpenseStructureStf(filePath, delimiter);
    }

    private void generateStandardJustice(String year, String tribunal) {
        int month = 1;
        while (month <= 12) {
            String yearString = String.valueOf(year);
            String monthString = String.format("%02d", month);
            String documentNumber = yearString + monthString;
            String relativePath = "/Judiciario/" + tribunal + "/" +"despesas" + year + "/" + documentNumber + ".csv";
            String filePath = Paths.get(csvPath, relativePath).toString();
            String delimiter = "\t";
            createStandardJusticeExpenseStrucutre(filePath, delimiter, month, tribunal);
            month++;
        }
    }

    private void createExpenseStructureStf(String filePath, String delimiter) {
        log.info("Lendo arquivo do STF e criando estrutura de despesas");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] rawList = line.split(delimiter);
                if (rawList.length == 8) {
                    List<String> refinedList = new ArrayList<>();
                    for (String column : rawList) {
                        refinedList.add(column.replace("\"", "").trim());
                    }
                    String planoOrcamentario = refinedList.get(4);
                    String valorString = refinedList.get(7);
                    valorString = valorString.replace("R$", "");
                    double valor = Double.parseDouble(valorString);

                    if (Objects.equals(planoOrcamentario,SENTENCAS_JUDICIAIS) || Objects.equals(valor, Constants.ZERO_DOUBLE)) {
                        continue;
                    }

                    Ministerio ministerioReceived = generalGeneratorService.findOrCreateMinisterio(SUPREMO_TRIBUNAL_FEDERAL, poder);
                    Orgao orgaoReceived = generalGeneratorService.findOrCreateOrgao(SUPREMO_TRIBUNAL_FEDERAL, ministerioReceived);
                    UnidadeGestora unidadeGestoraReceived = generalGeneratorService.findOrCreateUnidadeGestora(SUPREMO_TRIBUNAL_FEDERAL, orgaoReceived);
                    ElementoDespesa elementoDespesaReceived = generalGeneratorService.findOrCreateNewElementoDespesa(planoOrcamentario, unidadeGestoraReceived);

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

    private void createStandardJusticeExpenseStrucutre(String filePath, String delimiter, int month, String tribunal) {
        log.info("{} - Lendo arquivos e criando estrutura de despesas. Mês = {}", tribunal, month);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] rawList = line.split(delimiter);
                if (rawList.length == 9) {
                    List<String> refinedList = new ArrayList<>();
                    for (String column : rawList) {
                        refinedList.add(column.replace("\"", "").trim());
                    }
                    String orgaoSuperior = refinedList.get(3);
                    String unidadeOrcamentaria = refinedList.get(4);
                    String unidadeGestora = refinedList.get(5);
                    String elementoDespesa = refinedList.get(6);
                    String valorString = refinedList.get(8);
                    valorString = valorString.replace(",", ".");
                    double valor = Double.parseDouble(valorString);

                    if (unidadeGestora.contains(PREC_RPV) || Objects.equals(valor, Constants.ZERO_DOUBLE)) {
                        continue;
                    }

                    Ministerio ministerioReceived = generalGeneratorService.findOrCreateMinisterio(orgaoSuperior, poder);
                    Orgao orgaoReceived = generalGeneratorService.findOrCreateOrgao(unidadeOrcamentaria, ministerioReceived);
                    UnidadeGestora unidadeGestoraReceived = generalGeneratorService.findOrCreateUnidadeGestora(unidadeGestora, orgaoReceived);
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

}
