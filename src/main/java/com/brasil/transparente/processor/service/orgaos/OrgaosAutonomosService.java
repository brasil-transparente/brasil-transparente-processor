package com.brasil.transparente.processor.service.orgaos;

import com.brasil.transparente.processor.entity.*;
import com.brasil.transparente.processor.service.creation.ProcessExpensesService;
import com.brasil.transparente.processor.service.orgaos.generator.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class OrgaosAutonomosService {

    private final ProcessExpensesService processExpensesService;
    private final TribunalDeContasGeneratorService tribunalDeContasGeneratorService;
    private final DefensoriaPublicaGeneratorService defensoriaPublicaGeneratorService;
    private final MinisterioPublicoFederalGeneratorService ministerioPublicoFederalGeneratorService;
    private final MinisterioPublicoDFGeneratorService ministerioPublicoDFGeneratorService;
    private final MinisterioPublicoMilitarGeneratorService ministerioPublicoMilitarGeneratorService;
    private final MinisterioPublicoTrabalhoGeneratorService ministerioPublicoTrabalhoGeneratorService;
    private final EscolaMinisterioPublicoGeneratorService escolaMinisterioPublicoGeneratorService;

    private static final String ORGAOS_AUTONOMOS = "Órgãos Autônomos";
    List<Poder> poderList = List.of(new Poder(ORGAOS_AUTONOMOS));
    private static final String MINISTERIO_PUBLICO_FEDERAL = "Ministério Público Federal";
    private static final String MINISTERIO_PUBLICO_TRABALHO = "Ministério Público do Trabalho";
    private static final String MINISTERIO_PUBLICO_MILITAR = "Ministério Público Militar";
    private static final String MINISTERIO_PUBLICO_DF_TERRITORIOS = "Ministério Público do Distrito Federal e Territórios";
    private static final String ESCOLA_SUPERIOR_MINISTERIO_PUBLICO_UNIAO = "Escola Superior do Ministério Público da União";
    private static final String MINISTERIO_PUBLICO_PATH = "/Orgaos Autonomos/Ministerio Publico/";

    public Poder generateOrgaoIndependentesBranch(String year) {
        log.info("Iniciando - Orgãos Autônomos");
        tribunalDeContasGeneratorService.generateExpensesByMonth(poderList, StandardCharsets.UTF_8, "/Orgaos Autonomos/Tribunal de Contas da Uniao/" + year + "/",15, ";", year); // Mudou de 2024 - Total de linhas foi de 14 para 15
        defensoriaPublicaGeneratorService.generateExpenses(poderList, StandardCharsets.UTF_8, "/Orgaos Autonomos/Defensoria Publica da Uniao/" + year + "/" + year + ".csv",2, ";");
        ministerioPublicoFederalGeneratorService.generateExpenses(poderList, StandardCharsets.UTF_8, MINISTERIO_PUBLICO_PATH + MINISTERIO_PUBLICO_FEDERAL + "/" + year + "/" + year + ".csv",2, ";");
        ministerioPublicoDFGeneratorService.generateExpenses(poderList, StandardCharsets.UTF_8, MINISTERIO_PUBLICO_PATH + MINISTERIO_PUBLICO_TRABALHO + "/" + year + "/" + year + ".csv",2, ";");
        ministerioPublicoMilitarGeneratorService.generateExpenses(poderList, StandardCharsets.UTF_8, MINISTERIO_PUBLICO_PATH + MINISTERIO_PUBLICO_MILITAR + "/" + year + "/" + year + ".csv",2, ";");
        ministerioPublicoTrabalhoGeneratorService.generateExpenses(poderList, StandardCharsets.UTF_8, MINISTERIO_PUBLICO_PATH + MINISTERIO_PUBLICO_DF_TERRITORIOS + "/" + year + "/" + year + ".csv",2, ";");
        escolaMinisterioPublicoGeneratorService.generateExpenses(poderList, StandardCharsets.UTF_8, MINISTERIO_PUBLICO_PATH + ESCOLA_SUPERIOR_MINISTERIO_PUBLICO_UNIAO + "/" + year + "/" + year + ".csv",2, ";");
        processExpensesService.aggregateAllPowerSpending(poderList.getFirst());
        log.info("Finalizado - Órgãos Autônomos");
        return poderList.getFirst();
    }

}
