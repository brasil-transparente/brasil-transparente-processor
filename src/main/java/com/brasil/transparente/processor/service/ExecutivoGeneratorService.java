package com.brasil.transparente.processor.service;

import com.brasil.transparente.processor.entity.*;
import com.brasil.transparente.processor.util.CurrencyConverter;
import com.brasil.transparente.processor.util.constants.Constants;
import com.brasil.transparente.processor.util.constants.EmbaixadasConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExecutivoGeneratorService {

    private final CreatorService creatorService;
    private final CurrencyConverter currencyConverter;
    private final LoggingService loggingService;
    private final DespesasProcessingService despesasProcessingService;

    @Value("${CSV_PATH}")
    private String csvPath;

    private static final String EXECUTIVO = "Poder Executivo";
    private static final String PRECATORIOS_RPVS = "Precatórios e Requisições de Pequeno Valor";
    private static final String SUPREMO_TRIBUNAL_FEDERAL = "Supremo Tribunal Federal";
    private static final String SENTENCAS_JUDICIAIS = "Sentenças Judiciais";
    private static final String PREC_RPV = "RPV";
    Poder poder = new Poder(EXECUTIVO);

    public Poder generateExecutiveBranch(String year) {
        log.info("Poder Executivo - Iniciando");
        readAndCreateExpenseStructure(year);
        peekOnJudiciaryAndComplementStructure(year);
        despesasProcessingService.aggregateAllPowerSpending(poder);
        log.info("Poder Executivo - Finalizado");
        return poder;
    }

    private void readAndCreateExpenseStructure(String year) {
        int month = 1;
        while (month <= 12) {
            String yearString = String.valueOf(year);
            String monthString = String.format("%02d", month);
            String documentNumber = yearString + monthString;
            String relativePath = "/Executivo/despesas" + year + "/" + documentNumber + ".csv";
            String filePath = Paths.get(csvPath, relativePath).toString();
            String delimiter = ";";
            createExpensesStructure(filePath, delimiter, year, month);
            month++;
        }
    }

    private void createExpensesStructure(String filePath, String delimiter, String year, int month) {
        log.info("Lendo arquivos do Poder Executivo e criando estrutura de despesas. Ano = {}, Mês = {}", year, month);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.ISO_8859_1))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] rawList = line.split(delimiter);
                if (rawList.length == 47) {
                    List<String> refinedList = new ArrayList<>();
                    for (String column : rawList) {
                        refinedList.add(column.replace("\"", "").trim());
                    }

                    String nameMinisterio = refinedList.get(2);
                    String nameOrgao = refinedList.get(4);
                    String nameUnidadeGestora = refinedList.get(6);
                    String nameUnidadeOrcamentaria = refinedList.get(10);
                    String nameGrupoDeDespesa = refinedList.get(36);
                    String nameElementoDespesa = refinedList.get(38);
                    String valorPagoString = refinedList.get(43).replace(",", ".");
                    String valorRestosAPagarString = refinedList.get(46).replace(",", ".");

                    double valorPago = Double.parseDouble(valorPagoString) + Double.parseDouble(valorRestosAPagarString);

                    if (Objects.equals(valorPago, Constants.ZERO_DOUBLE)
                            || Objects.equals(nameElementoDespesa, Constants.REPASSES)
                            || Objects.equals(nameGrupoDeDespesa, Constants.AMORTIZACAO_DIVIDA)) {
                        continue;
                    }

                    if (Objects.equals(nameMinisterio,Constants.SEM_INFORMACAO)) {
                        nameMinisterio = replaceMinisteroName(nameUnidadeOrcamentaria);
                        nameOrgao = Constants.SEM_INFORMACAO;
                        nameUnidadeGestora = Constants.SEM_INFORMACAO;
                    }

                    if (Objects.equals(nameMinisterio, EmbaixadasConstants.MINISTERIO_RELACOES_EXTERIORES_ORIGINAL)) {
                        valorPago = currencyConverter.convertCurrency(nameUnidadeGestora, valorPago);
                        nameUnidadeGestora = mergeEmbassyNames(nameUnidadeGestora);
                    }

                    Ministerio ministerioReceived = creatorService.findOrCreateMinisterio(nameMinisterio, poder);
                    Orgao orgaoReceived = creatorService.findOrCreateOrgao(nameOrgao, ministerioReceived);
                    UnidadeGestora unidadeGestoraReceived = creatorService.findOrCreateUnidadeGestora(nameUnidadeGestora, orgaoReceived);
                    ElementoDespesa elementoDespesaReceived = creatorService.findOrCreateNewElementoDespesa(nameElementoDespesa, unidadeGestoraReceived);

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

    private void peekOnJudiciaryAndComplementStructure(String year) {
        peekOnStfAndComplementStructure();
        peekOnFederalJusticeAndComplementStructure(year);
    }

    private void peekOnStfAndComplementStructure() {
        String relativePath = "/Judiciario/Supremo Tribunal Federal/STF.csv";
        String filePath = Paths.get(csvPath, relativePath).toString();
        String delimiter = ",";
        log.info("STF - Lendo arquivos de despesas e criando estrutura de despesa.");
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

                    if (!Objects.equals(planoOrcamentario,SENTENCAS_JUDICIAIS) || Objects.equals(valor, Constants.ZERO_DOUBLE)) {
                        continue;
                    }

                    Ministerio ministerioReceived = creatorService.findOrCreateMinisterio(PRECATORIOS_RPVS, poder);
                    Orgao orgaoReceived = creatorService.findOrCreateOrgao(SUPREMO_TRIBUNAL_FEDERAL, ministerioReceived);
                    UnidadeGestora unidadeGestoraReceived = creatorService.findOrCreateUnidadeGestora(SUPREMO_TRIBUNAL_FEDERAL, orgaoReceived);
                    ElementoDespesa elementoDespesaReceived = creatorService.findOrCreateNewElementoDespesa(planoOrcamentario, unidadeGestoraReceived);

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

    private void peekOnFederalJusticeAndComplementStructure(String year) {
        int month = 1;
        while (month <= 12) {
            String yearString = String.valueOf(year);
            String monthString = String.format("%02d", month);
            String documentNumber = yearString + monthString;
            String relativePath = "/Judiciario/Justiça Federal/despesas" + year + "/";
            String filePath = csvPath + relativePath + documentNumber + ".csv";
            String delimiter = "\t";
            log.info("Justiça Federal - Lendo arquivos de despesas e criando estrutura de despesa. Mês = {}", month);
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
                        String unidadeOrcamentaria = refinedList.get(4);
                        String unidadeGestora = refinedList.get(5);
                        String elementoDespesa = refinedList.get(6);
                        String valorString = refinedList.get(8);
                        valorString = valorString.replace(",", ".");
                        double valor = Double.parseDouble(valorString);

                        if (!unidadeGestora.contains(PREC_RPV) || Objects.equals(valor, Constants.ZERO_DOUBLE)) {
                            continue;
                        }

                        Ministerio ministerioReceived = creatorService.findOrCreateMinisterio(PRECATORIOS_RPVS, poder);
                        Orgao orgaoReceived = creatorService.findOrCreateOrgao(unidadeOrcamentaria, ministerioReceived);
                        UnidadeGestora unidadeGestoraReceived = creatorService.findOrCreateUnidadeGestora(unidadeGestora, orgaoReceived);
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
            month++;
        }

    }

    private String mergeEmbassyNames(String unidadeGestora) {
        return switch (unidadeGestora) {
            case EmbaixadasConstants.EMBAIXADA_CHINA_PEQUIM_USD, EmbaixadasConstants.EMBAIXADA_CHINA_PEQUIM_LOCAL -> EmbaixadasConstants.EMBAIXADA_CHINA_PEQUIM;
            case EmbaixadasConstants.EMBAIXADA_CHINA_XANGAI_USD, EmbaixadasConstants.EMBAIXADA_CHINA_XANGAI_LOCAL -> EmbaixadasConstants.EMBAIXADA_CHINA_XANGAI;
            case EmbaixadasConstants.EMBAIXADA_CHINA_CANTAO_USD, EmbaixadasConstants.EMBAIXADA_CHINA_CANTAO_LOCAL -> EmbaixadasConstants.EMBAIXADA_CHINA_CANTAO;
            case EmbaixadasConstants.EMBAIXADA_BOLIVIA_USD, EmbaixadasConstants.EMBAIXADA_BOLIVIA_LOCAL -> EmbaixadasConstants.EMBAIXADA_BOLIVIA;
            case EmbaixadasConstants.EMBAIXADA_TAILANDIA_USD, EmbaixadasConstants.EMBAIXADA_TAILANDIA_LOCAL -> EmbaixadasConstants.EMBAIXADA_TAILANDIA;
            case EmbaixadasConstants.EMBAIXADA_POLONIA_USD, EmbaixadasConstants.EMBAIXADA_POLONIA_LOCAL -> EmbaixadasConstants.EMBAIXADA_POLONIA;
            case EmbaixadasConstants.EMBAIXADA_NIGERIA_USD, EmbaixadasConstants.EMBAIXADA_NIGERIA_LOCAL -> EmbaixadasConstants.EMBAIXADA_NIGERIA;
            case EmbaixadasConstants.EMBAIXADA_BEIRUTE_USD, EmbaixadasConstants.EMBAIXADA_BEIRUTE_LOCAL -> EmbaixadasConstants.EMBAIXADA_BEIRUTE;
            case EmbaixadasConstants.EMBAIXADA_PASO_LOS_LIBRES_LOCAL, EmbaixadasConstants.EMBAIXADA_PASO_LOS_LIBRES_USD -> EmbaixadasConstants.EMBAIXADA_PASO_LOS_LIBRES;
            case EmbaixadasConstants.EMBAIXADA_ASSUNCAO_USD -> EmbaixadasConstants.EMBAIXADA_ASSUNCAO;
            case EmbaixadasConstants.EMBAIXADA_ISTAMBUL_USD -> EmbaixadasConstants.EMBAIXADA_ISTAMBUL;
            case EmbaixadasConstants.EMBAIXADA_RAMALA_USD -> EmbaixadasConstants.EMBAIXADA_RAMALA;
            case EmbaixadasConstants.EMBAIXADA_PASO_TEL_AVIV_USD -> EmbaixadasConstants.EMBAIXADA_PASO_TEL_AVIV;
            case EmbaixadasConstants.EMBAIXADA_LA_PAZ_USD -> EmbaixadasConstants.EMBAIXADA_LA_PAZ;
            case EmbaixadasConstants.EMBAIXADA_GENEBRA_EURO -> EmbaixadasConstants.EMBAIXADA_GENEBRA;
            default -> unidadeGestora;
        };
    }

    private String replaceMinisteroName(String nameMinisterio) {
        return switch (nameMinisterio) {
            case EmbaixadasConstants.MINISTERIO_AGRICULTURA_CAPS -> EmbaixadasConstants.MINISTERIO_AGRICULTURA_ORIGINAL;
            case EmbaixadasConstants.MINISTERIO_TRABALHO_CAPS -> EmbaixadasConstants.MINISTERIO_TRABALHO_ORIGINAL;
            case EmbaixadasConstants.SECRETARIA_EMPREENDEDOR_CAPS -> EmbaixadasConstants.SECRETARIA_EMPREENDEDOR_ORIGINAL;
            case EmbaixadasConstants.MINISTERIO_ESPORTE_CAPS -> EmbaixadasConstants.MINISTERIO_ESPORTE_ORIGINAL;
            case EmbaixadasConstants.MINISTERIO_GESTAO_CAPS -> EmbaixadasConstants.MINISTERIO_GESTAO_ORIGINAL;
            case EmbaixadasConstants.MINISTERIO_ASSISTENCIA_SOCIAL_CAPS -> EmbaixadasConstants.MINISTERIO_ASSISTENCIA_SOCIAL_ORIGINAL;
            case EmbaixadasConstants.MINISTERIO_INDUSTRIA_CAPS -> EmbaixadasConstants.MINISTERIO_INDUSTRIA_ORIGINAL;
            case EmbaixadasConstants.PRESIDENCIA_REPUBLICA_CAPS -> EmbaixadasConstants.PRESIDENCIA_REPUBLICA_ORIGINAL;
            case EmbaixadasConstants.MINISTERIO_DESENV_AGRARIO_CAPS -> EmbaixadasConstants.MINISTERIO_DESENV_AGRARIO_ORIGINAL;
            case EmbaixadasConstants.MINISTERIO_EXTERIORES_CAPS -> EmbaixadasConstants.MINISTERIO_RELACOES_EXTERIORES_ORIGINAL;
            default -> nameMinisterio;
        };
    }

}
