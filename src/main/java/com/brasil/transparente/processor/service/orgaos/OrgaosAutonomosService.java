package com.brasil.transparente.processor.service.orgaos;

import com.brasil.transparente.processor.entity.*;
import com.brasil.transparente.processor.service.creation.ProcessExpensesService;
import com.brasil.transparente.processor.service.orgaos.generator.DefensoriaPublicaGeneratorService;
import com.brasil.transparente.processor.service.orgaos.generator.MinisterioPublicoFederalGeneratorService;
import com.brasil.transparente.processor.service.orgaos.generator.TribunalDeContasGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${CSV_PATH}")
    private String csvPath;

    private static final String ORGAOS_AUTONOMOS = "Órgãos Autônomos";
    Poder poder = new Poder(ORGAOS_AUTONOMOS);
    private static final String MINISTERIO_PUBLICO_FEDERAL = "Ministério Público Federal";
    private static final String MINISTERIO_PUBLICO_TRABALHO = "Ministério Público do Trabalho";
    private static final String MINISTERIO_PUBLICO_MILITAR = "Ministério Público Militar";
    private static final String MINISTERIO_PUBLICO_DF_TERRITORIOS = "Ministério Público do Distrito Federal e Territórios";
    private static final String ESCOLA_SUPERIOR_MINISTERIO_PUBLICO_UNIAO = "Escola Superior do Ministério Público da União";

    public Poder generateIndependentOrgansBranch(String year) {
        log.info("Orgãos Autônomos - Iniciando");
        tribunalDeContasGeneratorService.generateExpensesByMonth(poder, StandardCharsets.UTF_8, "/Orgaos Autonomos/Tribunal de Contas da Uniao/",14, ";", year);
        defensoriaPublicaGeneratorService.generateExpenses(poder, StandardCharsets.UTF_8, "/Orgaos Autonomos/Defensoria Publica da Uniao/" + year + ".csv",2, ";");
        ministerioPublicoFederalGeneratorService.generateExpenses(poder, StandardCharsets.UTF_8, "/Orgaos Autonomos/Ministerio Publico/" + MINISTERIO_PUBLICO_FEDERAL + "/" + year + ".csv",2, ";");
        ministerioPublicoFederalGeneratorService.generateExpenses(poder, StandardCharsets.UTF_8, "/Orgaos Autonomos/Ministerio Publico/" + MINISTERIO_PUBLICO_TRABALHO + "/" + year + ".csv",2, ";");
        ministerioPublicoFederalGeneratorService.generateExpenses(poder, StandardCharsets.UTF_8, "/Orgaos Autonomos/Ministerio Publico/" + MINISTERIO_PUBLICO_MILITAR + "/" + year + ".csv",2, ";");
        ministerioPublicoFederalGeneratorService.generateExpenses(poder, StandardCharsets.UTF_8, "/Orgaos Autonomos/Ministerio Publico/" + MINISTERIO_PUBLICO_DF_TERRITORIOS + "/" + year + ".csv",2, ";");
        ministerioPublicoFederalGeneratorService.generateExpenses(poder, StandardCharsets.UTF_8, "/Orgaos Autonomos/Ministerio Publico/" + ESCOLA_SUPERIOR_MINISTERIO_PUBLICO_UNIAO + "/" + year + ".csv",2, ";");
        processExpensesService.aggregateAllPowerSpending(poder);
        log.info("Órgãos Autônomos - Finalizado");
        return poder;
    }

}
