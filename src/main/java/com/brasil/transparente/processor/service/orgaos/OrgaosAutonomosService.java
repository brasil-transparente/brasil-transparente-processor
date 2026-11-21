package com.brasil.transparente.processor.service.orgaos;

import com.brasil.transparente.processor.entity.*;
import com.brasil.transparente.processor.service.creation.ProcessExpensesService;
import com.brasil.transparente.processor.service.orgaos.generator.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;

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
    private static final String MINISTERIO_PUBLICO_FEDERAL = "Ministério Público Federal";
    private static final String MINISTERIO_PUBLICO_TRABALHO = "Ministério Público do Trabalho";
    private static final String MINISTERIO_PUBLICO_MILITAR = "Ministério Público Militar";
    private static final String MINISTERIO_PUBLICO_DF_TERRITORIOS = "Ministério Público do Distrito Federal e Territórios";
    private static final String ESCOLA_SUPERIOR_MINISTERIO_PUBLICO_UNIAO = "Escola Superior do Ministério Público da União";
    private static final String MINISTERIO_PUBLICO_PATH = "/Orgaos Autonomos/Ministerio Publico/";

    public Poder generateOrgaoIndependentesBranch(String year) {
        log.info("Iniciando - Orgãos Autônomos");
        Poder poder = new Poder(ORGAOS_AUTONOMOS);
        tribunalDeContasGeneratorService.generateExpensesByMonth(poder, StandardCharsets.UTF_8, "/Orgaos Autonomos/Tribunal de Contas da Uniao/" + year + "/",15, ";", year);
        defensoriaPublicaGeneratorService.generateExpenses(poder, StandardCharsets.UTF_8, "/Orgaos Autonomos/Defensoria Publica da Uniao/" + year + "/" + year + ".csv",2, ";");
        ministerioPublicoFederalGeneratorService.generateExpenses(poder, StandardCharsets.UTF_8, MINISTERIO_PUBLICO_PATH + MINISTERIO_PUBLICO_FEDERAL + "/" + year + "/" + year + ".csv",2, ";");
        ministerioPublicoDFGeneratorService.generateExpenses(poder, StandardCharsets.UTF_8, MINISTERIO_PUBLICO_PATH + MINISTERIO_PUBLICO_DF_TERRITORIOS + "/" + year + "/" + year + ".csv",2, ";");
        ministerioPublicoMilitarGeneratorService.generateExpenses(poder, StandardCharsets.UTF_8, MINISTERIO_PUBLICO_PATH + MINISTERIO_PUBLICO_MILITAR + "/" + year + "/" + year + ".csv",2, ";");
        ministerioPublicoTrabalhoGeneratorService.generateExpenses(poder, StandardCharsets.UTF_8, MINISTERIO_PUBLICO_PATH + MINISTERIO_PUBLICO_TRABALHO + "/" + year + "/" + year + ".csv",2, ";");
        escolaMinisterioPublicoGeneratorService.generateExpenses(poder, StandardCharsets.UTF_8, MINISTERIO_PUBLICO_PATH + ESCOLA_SUPERIOR_MINISTERIO_PUBLICO_UNIAO + "/" + year + "/" + year + ".csv",2, ";");
        processExpensesService.aggregateAllPowerSpending(poder);
        log.info("Finalizado - Órgãos Autônomos");
        return poder;
    }

}
