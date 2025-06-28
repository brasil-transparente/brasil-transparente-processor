package com.brasil.transparente.processor.service;

import com.brasil.transparente.processor.entity.*;
import com.brasil.transparente.processor.repository.UnidadeFederativaRepository;
import com.brasil.transparente.processor.service.creation.CreateEntityService;
import com.brasil.transparente.processor.service.creation.ProcessExpensesService;
import com.brasil.transparente.processor.util.constants.Constants;
import com.brasil.transparente.processor.util.NameCorrectorService;
import com.brasil.transparente.processor.util.constants.UnidadesFederativasConstants;
import com.brasil.transparente.processor.util.constants.estados.RSConstants;
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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
@Service
public class EstadoGeneratorService {

    private final CreateEntityService createEntityService;
    private final LoggingService loggingService;
    private final NameCorrectorService nameCorrectorService;
    private final DespesaSimplificadaGeneratorService despesaSimplificadaGeneratorService;
    private final UnidadeFederativaRepository unidadeFederativaRepository;
    private final ProcessExpensesService processExpensesService;

    @Value("${CSV_PATH}")
    private String csvPath;

    public void generateStateExpensesRS(String year, String state) {
        log.info("Rio Grande do Sul - Iniciando");
        UnidadeFederativa unidadeFederativa = new UnidadeFederativa(UnidadesFederativasConstants.RS);
        List<Poder> poderList = new ArrayList<>(Arrays.asList(
                new Poder("Poder Executivo"),
                new Poder("Poder Legislativo"),
                new Poder("Poder Judiciário"),
                new Poder("Órgãos Autônomos")
        ));
        int month = 1;
        while (month <= 12) {
            String yearString = String.valueOf(year);
            String monthString = String.format("%02d", month);
            String documentNumber = yearString + monthString;
            String relativePath = "/Estados/" + state + "/" + documentNumber + ".csv";
            String filePath = Paths.get(csvPath, relativePath).toString();
            String delimiter = ";";
            createStateExpanseStructureRS(filePath, delimiter, month, state, poderList);
            month++;
        }

        unidadeFederativa.setListPoder(poderList);
        for (Poder poder : poderList) {
            processExpensesService.aggregateAllPowerSpending(poder);
            createEntityService.setRelationships(unidadeFederativa);
        }
        double gastoTotalValue = processExpensesService.aggregateTotalExpense(unidadeFederativa);
        processExpensesService.removeNegativeOrZeroExpenses(unidadeFederativa.getListPoder());
        processExpensesService.setTotalPercentages(unidadeFederativa.getListPoder(), gastoTotalValue);
        nameCorrectorService.refactorNames(unidadeFederativa.getListPoder());
        log.info("Salvando no banco de dados");
        unidadeFederativaRepository.save(unidadeFederativa);
        despesaSimplificadaGeneratorService.generateSimplifiedReportRS();
        log.info("Rio Grande do Sul - Finalizado");
    }

    public void generateStateExpensesBA(String year, String state) {
        log.info("Bahia - Iniciando");
        UnidadeFederativa unidadeFederativa = new UnidadeFederativa(UnidadesFederativasConstants.BA);
        List<Poder> poderList = new ArrayList<>(Arrays.asList(
                new Poder("Poder Executivo"),
                new Poder("Poder Legislativo"),
                new Poder("Poder Judiciário"),
                new Poder("Órgãos Autônomos")
        ));
        String yearString = String.valueOf(year);
        String relativePath = "/Estados/" + state + "/" + yearString + ".csv";
        String filePath = Paths.get(csvPath, relativePath).toString();
        String delimiter = ";";
        createStateExpanseStructureBA(filePath, delimiter, state, poderList);

        unidadeFederativa.setListPoder(poderList);
        for (Poder poder : poderList) {
            processExpensesService.aggregateAllPowerSpending(poder);
            createEntityService.setRelationships(unidadeFederativa);
        }
        double gastoTotalValue = processExpensesService.aggregateTotalExpense(unidadeFederativa);
        processExpensesService.removeNegativeOrZeroExpenses(unidadeFederativa.getListPoder());
        processExpensesService.setTotalPercentages(unidadeFederativa.getListPoder(), gastoTotalValue);
        nameCorrectorService.refactorNames(unidadeFederativa.getListPoder());
        log.info("Salvando no banco de dados");
        unidadeFederativaRepository.save(unidadeFederativa);
        despesaSimplificadaGeneratorService.generateSimplifiedReportBA();
        log.info("Bahia - Finalizado");
    }

    private void createStateExpanseStructureRS(String filePath, String delimiter, int month, String state, List<Poder> poderList) {
        log.info("{} - Lendo arquivos e criando estrutura de despesas. Mês = {}", state, month);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.ISO_8859_1))) {
            String firstLine = br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] rawList = line.split(delimiter);
                List<String> refinedList = new ArrayList<>();
                for (String column : rawList) {
                    refinedList.add(column.replace("\"", "").trim());
                }
                String tipoGasto = refinedList.get(2);
                String poderString = refinedList.get(12);
                String ministerio = refinedList.get(16);
                String orgao = refinedList.get(18);
                String unidadeGestora = refinedList.get(18);
                String elementoDespesa = refinedList.get(26);
                String valorString = refinedList.get(44);
                valorString = valorString.replace(",", ".");
                double valor = Double.parseDouble(valorString);

                if (!Objects.equals(tipoGasto, Constants.PAGAMENTO)
                        || Objects.equals(valor, Constants.ZERO_DOUBLE)) {
                    continue;
                }

                String ministerioRevisado = resolveMinisterio(ministerio);
                if (Objects.nonNull(ministerioRevisado)) {
                    orgao = ministerio;
                    ministerio = ministerioRevisado;
                }

                Poder poder = definePoder(poderString, poderList);
                Ministerio ministerioReceived = createEntityService.findOrCreateMinisterio(ministerio, poder);
                Orgao orgaoReceived = createEntityService.findOrCreateOrgao(orgao, ministerioReceived);
                UnidadeGestora unidadeGestoraReceived = createEntityService.findOrCreateUnidadeGestora(unidadeGestora, orgaoReceived);
                ElementoDespesa elementoDespesaReceived = createEntityService.findOrCreateNewElementoDespesa(elementoDespesa, unidadeGestoraReceived);

                processExpensesService.updateTotalValueSpent(ministerioReceived, orgaoReceived, unidadeGestoraReceived, elementoDespesaReceived, valor);
            }
        } catch (IOException e) {
            loggingService.logExceptionMainFile(e);
        } catch (NumberFormatException e) {
            loggingService.logNumberFormatException(e);
        }
    }

    private void createStateExpanseStructureBA(String filePath, String delimiter, String state, List<Poder> poderList) {
        log.info("{} - Lendo arquivos e criando estrutura de despesas.", state);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String firstLine = br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] rawList = line.split(delimiter);
                List<String> refinedList = new ArrayList<>();
                for (String column : rawList) {
                    refinedList.add(column.replace("\"", "").trim());
                }

                String poderString = refinedList.get(5);
                String ministerio = refinedList.get(7);
                String orgao = refinedList.get(11);
                String unidadeGestora = refinedList.get(12);
                String elementoDespesa = refinedList.get(40);
                String valorString = refinedList.get(59);
                valorString = valorString.replace(",", ".");
                double valor = Double.parseDouble(valorString);

                if (Objects.equals(valor, Constants.ZERO_DOUBLE)) {
                    continue;
                }

                Poder poder = definePoder(poderString, poderList);
                Ministerio ministerioReceived = createEntityService.findOrCreateMinisterio(ministerio, poder);
                Orgao orgaoReceived = createEntityService.findOrCreateOrgao(orgao, ministerioReceived);
                UnidadeGestora unidadeGestoraReceived = createEntityService.findOrCreateUnidadeGestora(unidadeGestora, orgaoReceived);
                ElementoDespesa elementoDespesaReceived = createEntityService.findOrCreateNewElementoDespesa(elementoDespesa, unidadeGestoraReceived);

                processExpensesService.updateTotalValueSpent(ministerioReceived, orgaoReceived, unidadeGestoraReceived, elementoDespesaReceived, valor);
            }
        } catch (IOException e) {
            loggingService.logExceptionMainFile(e);
        } catch (NumberFormatException e) {
            loggingService.logNumberFormatException(e);
        }
    }

    private Poder definePoder(String poder, List<Poder> poderList) {
        if (poder == null) {
            return poderList.get(3);
        }
        String input = poder.trim().toUpperCase();
        if (Constants.EXECUTIVO.stream().anyMatch(v -> input.equalsIgnoreCase(v.trim().toUpperCase()))) {
            return poderList.getFirst();
        } else if (Constants.LEGISLATIVO.stream().anyMatch(v -> input.equalsIgnoreCase(v.trim().toUpperCase()))) {
            return poderList.get(1);
        } else if (Constants.JUDICIARIO.stream().anyMatch(v -> input.equalsIgnoreCase(v.trim().toUpperCase()))) {
            return poderList.get(2);
        }
        return poderList.get(3);
    }

    private String resolveMinisterio(String ministerio) {
        return switch (ministerio) {
            case RSConstants.INSTITUTO_RIOGRANDENSE_ARROZ -> RSConstants.SECRETARIA_AGRICULTURA;
            case RSConstants.UERGS, RSConstants.FUNDACAO_ESCOLA_LIBERATO, RSConstants.CONSELHO_EDUCACAO ->
                    RSConstants.SECRETARIA_EDUCACAO;
            case RSConstants.FUNDACAO_AMPARO_PESQUISA -> RSConstants.SECRETARIA_INOVACAO;
            case RSConstants.FUNDACAO_PROTECAO_AMBIENTAL_ROESSLER -> RSConstants.SECRETARIA_MEIO_AMBIENTE;
            case RSConstants.ESCRITORIO_DESENVOLVIMENTO_PROJETOS,
                 RSConstants.FUNDACAO_PLANEJAMENTO_METROPOLITANO_REGIONAL -> RSConstants.SECRETARIA_PLANEJAMENTO;
            case RSConstants.FUDACAO_ATENDIMENTO_SOCIO_EDUCATIVO, RSConstants.FUNDACAO_PROTECAO_ESPECIAL,
                 RSConstants.FUNDACAO_GAUCHA_TRABALHO_ACAO_SOCIAL, RSConstants.FUNDACAO_PPD_PPAH ->
                    RSConstants.SECRETARIA_DESENVOLVIMENTO_SOCIAL;
            case RSConstants.FUNDACAO_ORQUESTRA, RSConstants.FUNDACAO_TEATRO -> RSConstants.SECRETARIA_CULTURA;
            case RSConstants.DEPARTAMENTO_ESTADUAL_TRANSITO -> RSConstants.SECRETARIA_SEGURANCA_PUBLICA;
            case RSConstants.DEPARTAMENTO_ESTRADAS_RODAGEM -> RSConstants.SECRETARIA_LOGISTICA_TRANSPORTES;
            case RSConstants.JUNTA_COMERCIAL -> RSConstants.SECRETARIA_DESENVOLVIMENTO_ECONOMICO;
            case RSConstants.AGENCIA_DELEGADOS -> RSConstants.SECRETARIA_PARCERIAS_CONCESSOES;
            case RSConstants.INSTITUTO_PREVIDENCIA, RSConstants.INSTITUTO_ASSISTENCIA_SAUDE_SERVIDORES,
                 RSConstants.ENCARGOS_FINANCEIROS -> RSConstants.SECRETARIA_FAZENDA;
            default -> ministerio;
        };
    }
}
