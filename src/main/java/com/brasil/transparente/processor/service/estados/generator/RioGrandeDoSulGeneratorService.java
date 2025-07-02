package com.brasil.transparente.processor.service.estados.generator;

import com.brasil.transparente.processor.entity.Poder;
import com.brasil.transparente.processor.service.ExpenseGenerator;
import com.brasil.transparente.processor.service.creation.CreateEntityService;
import com.brasil.transparente.processor.service.creation.ProcessExpensesService;
import com.brasil.transparente.processor.util.PoderResolverService;
import com.brasil.transparente.processor.util.constants.Constants;
import com.brasil.transparente.processor.util.constants.estados.RSConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class RioGrandeDoSulGeneratorService extends ExpenseGenerator {

    private final PoderResolverService poderResolverService;

    protected RioGrandeDoSulGeneratorService(CreateEntityService createEntityService,
                                             ProcessExpensesService processExpensesService,
                                             PoderResolverService poderResolverService) {
        super(createEntityService, processExpensesService);
        this.poderResolverService = poderResolverService;
    }

    @Override
    protected void processLine(List<Poder> poderList, List<String> refinedLine) {
        String tipoGasto = refinedLine.get(2);
        String poderString = refinedLine.get(12);
        String ministerio = refinedLine.get(16);
        String orgao = refinedLine.get(18);
        String unidadeGestora = refinedLine.get(18);
        String elementoDespesa = refinedLine.get(26);
        String valorString = refinedLine.get(44);
        valorString = valorString.replace(",", ".");
        double valorPagoFinal = Double.parseDouble(valorString);

        if (!Objects.equals(tipoGasto, Constants.PAGAMENTO)
                || Objects.equals(valorPagoFinal, Constants.ZERO_DOUBLE)) {
            return;
        }

        String ministerioRevisado = resolveMinisterio(ministerio);
        if (Objects.nonNull(ministerioRevisado)) {
            orgao = ministerio;
            ministerio = ministerioRevisado;
        }

        Poder poder = poderResolverService.definePoder(poderString, poderList);
        createEntitiesAndUpdateValues(poder, ministerio, orgao, unidadeGestora, elementoDespesa, valorPagoFinal);
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
