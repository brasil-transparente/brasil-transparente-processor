package com.brasil.transparente.processor.service.estados.generator;

import com.brasil.transparente.processor.entity.Poder;
import com.brasil.transparente.processor.service.ExpenseGenerator;
import com.brasil.transparente.processor.service.creation.CreateEntityService;
import com.brasil.transparente.processor.service.creation.ProcessExpensesService;
import com.brasil.transparente.processor.util.PoderResolverService;
import com.brasil.transparente.processor.util.constants.Constants;
import com.brasil.transparente.processor.util.constants.estados.PBConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class ParaibaGeneratorService extends ExpenseGenerator {

    private final PoderResolverService poderResolverService;

    protected ParaibaGeneratorService(CreateEntityService createEntityService,
                                      ProcessExpensesService processExpensesService,
                                      PoderResolverService poderResolverService) {
        super(createEntityService, processExpensesService);
        this.poderResolverService = poderResolverService;
    }

    @Override
    protected void processLine(List<Poder> poderList, List<String> refinedLine) {
        try {
            String poderString = refinedLine.get(2);
            String ministerio = refinedLine.get(3);
            String orgao = refinedLine.get(3);
            String unidadeGestora = refinedLine.get(3);
            String elementoDespesa = refinedLine.get(7);
            String valorPagoString = refinedLine.get(17);
            double valorPagoFinal = Double.parseDouble(valorPagoString);

            if (Objects.equals(valorPagoFinal, Constants.ZERO_DOUBLE)
                    || Constants.PRINCIPAL_DIVIDA_AMORTIZACAO.contains(elementoDespesa)) {
                return;
            }

            if (Objects.isNull(unidadeGestora) || unidadeGestora.isEmpty()) {
                unidadeGestora = Constants.SEM_INFORMACAO;
            }

            if (Objects.isNull(elementoDespesa) || elementoDespesa.isEmpty()) {
                elementoDespesa = Constants.SEM_INFORMACAO;
            }

            String ministerioRevisado = resolveMinisterio(ministerio);

            Poder poder = poderResolverService.definePoder(poderString, poderList);

            createEntitiesAndUpdateValues(poder, ministerioRevisado, orgao, unidadeGestora, elementoDespesa, valorPagoFinal);

        } catch (NumberFormatException e) {
            log.warn("Não foi possível converter o valor para número. Linha pulada. Conteúdo da linha: {}", refinedLine);
        } catch (IndexOutOfBoundsException e) {
            log.warn("Linha com número inesperado de colunas. Linha pulada. Conteúdo da linha: {}", refinedLine);
        }
    }

    private String resolveMinisterio(String ministerio) {
        return switch (ministerio) {
            case PBConstants.ENCARGOS_GERAIS_ESTADO_SEFAZ,
                 PBConstants.LOTERIA_ESTADO_PARAIBA,
                 PBConstants.FUNDO_ESTADUAL_COMBATE_CORRUPCAO,
                 PBConstants.FUNDO_APOIO_DES_ADMIN_TRIBUTARIA,
                 PBConstants.FUNDO_ESTADUAL_EQUILIBRIO_FISCAL,
                 PBConstants.PARAIBA_PREVIDENCIA_PBPREV
                    -> PBConstants.SEC_ESTADO_FAZENDA;

            case PBConstants.FUNDO_ESPECIAL_DESENV_RECURSOS_HUMANOS,
                 PBConstants.ESPEP_ESCOLA_SERVICO_PUBLICO_PARAIBA,
                 PBConstants.ENCARGOS_GERAIS_ESTADO_SEAD,
                 PBConstants.JUNTA_COMERCIAL_ESTADO_PARAIBA,
                 PBConstants.CODATA_CIA_PROCESSAMENTO_DADOS_PARAIBA,
                 PBConstants.FUNDO_ESTADUAL_TRABALHO_FET_PB,
                 PBConstants.FUNDO_APOIO_ACOES_CIDADAS
                    -> PBConstants.SEC_ESTADO_ADMINISTRACAO;

            case PBConstants.UNIVERSIDADE_ESTADUAL_PARAIBA,
                 PBConstants.FUNDACAO_APOIO_PESQUISA_ESTADO_PARAIBA,
                 PBConstants.FUND_CENTRO_APOIO_PESSOA_DEFICIENCIA_FUNAD,
                 PBConstants.FUNDACAO_ESPACO_CULTURAL_PARAIBA
                    -> PBConstants.SEC_ESTADO_EDUCACAO;

            case PBConstants.INSTITUTO_ASSISTENCIA_SAUDE_SERVIDOR,
                 PBConstants.AGENCIA_ESTADUAL_VIGILANCIA_SANITARIA,
                 PBConstants.LABORATORIO_INDUSTRIAL_FARMACEUTICO_PARAIBA
                    -> PBConstants.SEC_ESTADO_SAUDE;

            case PBConstants.POLICIA_MILITAR_ESTADO,
                 PBConstants.CORPO_BOMBEIROS_MILITAR,
                 PBConstants.FUNDO_ESPECIAL_CORPO_BOMBEIROS,
                 PBConstants.FUNDO_SEGURANCA_DEFESA_SOCIAL_PB,
                 PBConstants.DEPARTAMENTO_ESTADUAL_TRANSITO,
                 PBConstants.POLICIA_CIVIL_ESTADO_PARAIBA
                    -> PBConstants.SEC_ESTADO_SEGURANCA_DEFESA_SOCIAL;

            case PBConstants.FUNDO_ESTADUAL_ASSISTENCIA_SOCIAL,
                 PBConstants.FUNDO_ESTADUAL_CRIANCA_ADOLESCENTE,
                 PBConstants.FUND_DESENV_CRIANCA_ADOLESCENTE_ALICE_ALMEIDA,
                 PBConstants.FUNDACAO_ERNANI_SATYRO,
                 PBConstants.FUNDO_APOIO_DESENVOLVIMENTO_INDUSTRIAL_PB,
                 PBConstants.FUNDO_DESENVOLVIMENTO_ESTADO_PARAIBA,
                 PBConstants.FUNDO_ESTADUAL_APOIO_EMPREENDEDORISMO,
                 PBConstants.COMPANHIA_ESTADUAL_HABITACAO_POPULAR,
                 PBConstants.EMPRESA_PARAIBANA_PESQ_EXT_RURAL_FUNDIARIA,
                 PBConstants.CIA_DESENVOLVIMENTO_PARAIBA,
                 PBConstants.PROJETO_COOPERAR_ESTADO_PARAIBA
                    -> PBConstants.SEC_ESTADO_DESENVOLVIMENTO_HUMANO;

            case PBConstants.PB_TUR_HOTEIS_SA,
                 PBConstants.EMPRESA_PARAIBANA_TURISMO_PB_TUR,
                 PBConstants.INST_METROLOGIA_QUALIDADE_INDUST_PARAIBA
                    -> PBConstants.SEC_ESTADO_TURISMO_DESENV_ECONOMICO;

            case PBConstants.FUNDO_DESENVOLVIMENTO_AGROPECUARIO_PB
                    -> PBConstants.SEC_ESTADO_DESENV_AGROPECUARIA_PESCA;

            case PBConstants.AGENCIA_REGULACAO_ESTADO_PARAIBA,
                 PBConstants.AGENCIA_GESTAO_AGUAS_ESTADO_PB,
                 PBConstants.FUNDO_MANUTENCAO_OPERACIONALIZACAO_CENTRO_CO,
                 PBConstants.SUP_OBRAS_PLANO_DESENVOLVIMENTO_ESTADO,
                 PBConstants.COMPANHIA_AGUA_ESGOTOS_ESTADO_PARAIBA,
                 PBConstants.DEPARTAMENTO_ESTRADAS_RODAGEM,
                 PBConstants.COMPANHIA_DOCAS_PARAIBA,
                 PBConstants.FUNDO_ESTADUAL_RECURSOS_HIDRICOS
                    -> PBConstants.SEC_ESTADO_INFRAESTRUTURA_RECURSOS_HIDRICOS;

            case PBConstants.SUPERINTENDENCIA_ADMIN_MEIO_AMBIENTE,
                 PBConstants.FUNDO_ESTADUAL_PROTECAO_MEIO_AMBIENTE
                    -> PBConstants.SEC_ESTADO_MEIO_AMBIENTE_SUSTENTABILIDADE;

            case PBConstants.INST_PATRIMONIO_HISTORICO_ART_ESTADO_PARAIBA,
                 PBConstants.FUNDACAO_CASA_JOSE_AMERICO,
                 PBConstants.FUNDO_INCENTIVO_CULTURA_AUGUSTO_DOS_ANJOS
                    -> PBConstants.SEC_ESTADO_CULTURA;

            case PBConstants.FUNDO_APOIO_ESPORTE_LAZER_PARAIBA
                    -> PBConstants.SEC_ESTADO_JUVENTUDE_ESPORTE_LAZER;

            case PBConstants.FUNDO_ESPECIAL_DEFENSORIA_PUBLICA,
                 PBConstants.DEFENSORIA_PUBLICA_ESTADO_PARAIBA
                    -> PBConstants.DEFENSORIA_PUBLICA_ESTADO_PARAIBA;

            case PBConstants.EMPRESA_PARAIBANA_COMUNICACAO_EPC
                    -> PBConstants.SEC_ESTADO_COMUNICACAO_INSTITUCIONAL;

            case PBConstants.FUNDO_RECUPERACAO_PRESIDIARIOS
                    -> PBConstants.SEC_ESTADO_ADMINISTRACAO_PENITENCIARIA;

            case PBConstants.FUNDO_MODERNIZACAO_PROC_GERAL_ESTADO
                    -> PBConstants.PROCURADORIA_GERAL_ESTADO;

            case PBConstants.FUNDO_ESTADUAL_DEFESA_DIREITOS_CONSUMIDOR,
                 PBConstants.PROTEC_DEFESA_CONSUMIDOR
                    -> PBConstants.SEC_REPRESENTACAO_INSTITUCIONAL;

            case PBConstants.FUNDO_ESPEC_PODER_JUDICIARIO,
                 PBConstants.FUNDO_APOIO_PESSOAS_NATUAIS
                    -> PBConstants.TRIBUNAL_JUSTICA_ESTADO_PARAIBA;

            case PBConstants.FUNDO_ESPECIAL_MINISTERIO_PUBLICO,
                 PBConstants.FUNDO_ESP_DEFESA_CONSUMIDOR_MIN_PUBLICO_PB,
                 PBConstants.FUNDO_FISCALIZACAO_ORCAMENTARIA_FINANC_MUNICIPAL,
                 PBConstants.FUNDO_ESP_PROT_BENS_VALORES_INTERESSES_DIFUSOS
                    -> PBConstants.MINISTERIO_PUBLICO;

            default -> ministerio;
        };
    }

}