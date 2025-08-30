package com.brasil.transparente.processor.service.estados.generator;

import com.brasil.transparente.processor.entity.Poder;
import com.brasil.transparente.processor.service.ExpenseGenerator;
import com.brasil.transparente.processor.service.creation.CreateEntityService;
import com.brasil.transparente.processor.service.creation.ProcessExpensesService;
import com.brasil.transparente.processor.util.PoderResolverService;
import com.brasil.transparente.processor.util.constants.Constants;
import com.brasil.transparente.processor.util.constants.estados.AMConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class AmazonasGeneratorService extends ExpenseGenerator {

    private final PoderResolverService poderResolverService;

    protected AmazonasGeneratorService(CreateEntityService createEntityService,
                                       ProcessExpensesService processExpensesService,
                                       PoderResolverService poderResolverService) {
        super(createEntityService, processExpensesService);
        this.poderResolverService = poderResolverService;
    }

    @Override
    protected void processLine(List<Poder> poderList, List<String> refinedLine) {
        String poderString = refinedLine.get(7);
        String ministerio = refinedLine.get(9);
        String orgao = refinedLine.get(9);
        String unidadeGestora = refinedLine.get(3);
        String transferenciaMunicipios = refinedLine.get(28);
        String elementoDespesa = refinedLine.get(30);
        String valorPago = refinedLine.get(37).replace(",", ".");
        String valorPagoRestosPagar = refinedLine.get(39).replace(",", ".");
        double valorPagoFinal = Double.parseDouble(valorPago) + Double.parseDouble(valorPagoRestosPagar);

        if (Objects.equals(valorPagoFinal, Constants.ZERO_DOUBLE)
                || Objects.equals(transferenciaMunicipios, Constants.TRANSFERENCIA_MUNICIPIOS)
                || Constants.PRINCIPAL_DIVIDA_AMORTIZACAO.contains(elementoDespesa)) {
            return;
        }

        MinisterioOrgao mo = new MinisterioOrgao();
        mo.ministerio = ministerio;
        mo.orgao = orgao;
        reclassifyMinistryAndOrgan(mo);
        ministerio = mo.ministerio;
        orgao = mo.orgao;

        Poder poder = poderResolverService.definePoder(poderString, poderList);
        createEntitiesAndUpdateValues(poder, ministerio, orgao, unidadeGestora, elementoDespesa, valorPagoFinal);
    }

    public static class MinisterioOrgao {
        public String ministerio;
        public String orgao;
    }

    private void reclassifyMinistryAndOrgan(MinisterioOrgao mo) {
        switch (mo.ministerio) {
            case AMConstants.FUNDO_ESPECIAL_DA_DEFENSORIA_PUBLICA_DO_ESTADO_DO_AMAZONAS:
                mo.ministerio = AMConstants.DEFENSORIA_PUBLICA_DO_ESTADO_DO_AMAZONAS;
                break;
            case AMConstants.FUNDO_ESTADUAL_DE_ASSISTENCIA_SOCIAL:
            case AMConstants.SECRETARIA_EXECUTIVA_ERRADICACAO_POBREZA:
                mo.ministerio = AMConstants.SECRETARIA_DE_ESTADO_DA_ASSISTENCIA_SOCIAL;
                break;
            case AMConstants.FUNDO_PARA_FINANCIAMENTO_DA_MODERNIZACAO_FAZENDARIA_DO_ESTADO_DO_AMAZONAS:
            case AMConstants.INSTITUTO_DE_PESOS_E_MEDIDAS:
                mo.ministerio = AMConstants.SECRETARIA_DE_ESTADO_DA_FAZENDA;
                break;
            case AMConstants.FUNDO_ESTADUAL_DE_REGULARIZACAO_FUNDIARIA:
                mo.ministerio = AMConstants.SECRETARIA_DE_ESTADO_DAS_CIDADES_E_TERRITORIOS;
                break;
            case AMConstants.IMPRENSA_OFICIAL_DO_ESTADO_DO_AMAZONAS:
            case AMConstants.JUNTA_COMERCIAL_DO_ESTADO:
                mo.ministerio = AMConstants.SECRETARIA_DE_ESTADO_DE_ADMINISTRACAO_E_GESTAO;
                break;
            case AMConstants.FUNDO_PENITENCIARIO_DO_ESTADO_DO_AMAZONAS:
                mo.ministerio = AMConstants.SECRETARIA_DE_ESTADO_DE_ADMINISTRACAO_PENITENCIARIA;
                break;
            case AMConstants.FUNDO_ESTADUAL_DE_CULTURA:
                mo.ministerio = AMConstants.SECRETARIA_DE_ESTADO_DE_CULTURA_E_ECONOMIA_CRIATIVA;
                break;
            case AMConstants.EMPRESA_ESTADUAL_DE_TURISMO:
            case AMConstants.FUNDACAO_DE_AMPARO_A_PESQUISA_DO_ESTADO_DO_AMAZONAS:
                mo.ministerio = AMConstants.SECRETARIA_DE_ESTADO_DE_DESENVOLVIMENTO_ECONOMICO_CIENCA_TECNOLOGIA_E_INOVACAO;
                break;
            case AMConstants.COMPANHIA_DE_SANEAMENTO_DO_AMAZONAS_SA:
            case AMConstants.SUPERINTENDENCIA_ESTADUAL_DE_HABITACAO:
                mo.ministerio = AMConstants.SECRETARIA_DE_ESTADO_DE_DESENVOLVIMENTO_URBANO_E_METROPOLITANO;
                break;
            case AMConstants.FUNDO_ESTADUAL_DE_HABITACAO:
                mo.ministerio = AMConstants.SECRETARIA_DE_ESTADO_DE_DESENVOLVIMENTO_URBANO_E_METROPOLITANO;
                mo.orgao = AMConstants.SUPERINTENDENCIA_ESTADUAL_DE_HABITACAO;
                break;
            case AMConstants.CENTRO_DE_EDUCACAO_TECNOLOGICA_DO_AMAZONAS:
            case AMConstants.FUNDACAO_TELEVISAO_E_RADIO_CULTURA_DO_AMAZONAS:
            case AMConstants.UNIVERSIDADE_DO_ESTADO_DO_AMAZONAS:
                mo.ministerio = AMConstants.SECRETARIA_DE_ESTADO_DE_EDUCACAO_E_DESPORTO;
                break;
            case AMConstants.AGENCIA_REGULADORA_DE_SERVICOS_PUBLICOS_DELEGADOS_E_CONTRATADOS_DO_ESTADO_DO_AMA:
            case AMConstants.SUPERINTENDENCIA_NAVEGACAO_PORTOS_HIDROVIAS:
                mo.ministerio = AMConstants.SECRETARIA_DE_ESTADO_DE_INFRAESTRUTURA;
                break;
            case AMConstants.FUNDO_ESTADUAL_DA_CRIANCA_E_DO_ADOLESCENTE:
            case AMConstants.FUNDACAO_ESTADUAL_DO_INDIO:
                mo.ministerio = AMConstants.SECRETARIA_DE_ESTADO_DE_JUSTICA_DIREITOS_HUMANOS_E_CIDADANIA;
                break;
            case AMConstants.FUNDO_ESTADUAL_DE_DEFESA_DO_CONSUMIDOR:
                mo.ministerio = AMConstants.SECRETARIA_DE_ESTADO_DE_JUSTICA_DIREITOS_HUMANOS_E_CIDADANIA;
                mo.orgao = AMConstants.INSTITUTO_DE_DEFESA_DO_CONSUMIDOR_PROCON_AM;
            case AMConstants.AGENCIA_DE_DEFESA_AGROPECUARIA_E_FLORESTAL_DO_ESTADO_DO_AMAZONAS:
            case AMConstants.INSTITUTO_DE_DESENVOLVIMENTO_AGROPECUARIO_E_FLORESTAL_SUSTENTAVEL_DO_ESTADO_DO_AMAZONAS:
                mo.ministerio = AMConstants.SECRETARIA_DE_ESTADO_DE_PRODUCAO_RURAL;
                break;
            case AMConstants.FUNDO_ESTADUAL_DE_SAUDE:
            case AMConstants.FUNDACAO_HOSPITAL_ADRIANO_JORGE:
            case AMConstants.FUNDACAO_HOSPITAL_DO_CORACAO_FRANCISCA_MENDES:
            case AMConstants.FUNDACAO_HOSPITALAR_DE_DERMATOLOGIA_TROPICAL_E_VENEREOLOGIA_ALFREDO_DA_MATTA:
            case AMConstants.FUNDACAO_HOSPITALAR_DE_HEMATOLOGIA_E_HEMOTERAPIA_DO_AMAZONAS:
            case AMConstants.FUNDACAO_CENTRO_DE_CONTROLE_DE_ONCOLOGIA_DO_ESTADO_DO_AMAZONAS:
            case AMConstants.FUNDACAO_DE_MEDICINA_TROPICAL_DOUTOR_HEITOR_VIEIRA_DOURADO:
            case AMConstants.FUNDACAO_DE_VIGILANCIA_EM_SAUDE_DO_ESTADO_DO_AMAZONAS_DRA_ROSEMARY_COSTA_PINTO:
                mo.ministerio = AMConstants.SECRETARIA_DE_ESTADO_DE_SAUDE;
                break;
            case AMConstants.FUNDO_DE_RESERVA_PARA_AS_ACOES_DE_INTELIGENCIA:
            case AMConstants.FUNDO_ESTADUAL_DE_PROTECAO_E_DEFESA_CIVIL:
            case AMConstants.FUNDO_ESTADUAL_DE_SEGURANCA_PUBLICA_DO_ESTADO_DO_AMAZONAS:
            case AMConstants.DEPARTAMENTO_ESTADUAL_DE_TRANSITO:
                mo.ministerio = AMConstants.SECRETARIA_DE_ESTADO_DE_SEGURANCA_PUBLICA;
                break;
            case AMConstants.FUNDO_ESPECIAL_DO_CORPO_DE_BOMBEIROS_MILITAR_DO_ESTADO_DO_AMAZONAS:
                mo.ministerio = AMConstants.SECRETARIA_DE_ESTADO_DE_SEGURANCA_PUBLICA;
                mo.orgao = AMConstants.CORPO_DE_BOMBEIROS_MILITAR_DO_ESTADO_DO_AMAZONAS;
                break;
            case AMConstants.FUNDO_ESTADUAL_DO_MEIO_AMBIENTE:
            case AMConstants.AGENCIA_DE_DESENVOLVIMENTO_SUSTENTAVEL_DO_AMAZONAS:
            case AMConstants.INSTITUTO_DE_PROTECAO_AMBIENTAL_DO_AMAZONAS:
                mo.ministerio = AMConstants.SECRETARIA_DE_ESTADO_DO_MEIO_AMBIENTE;
                break;
            case AMConstants.FUNDO_ESPECIAL_DA_PROCURADORIA_GERAL_DO_ESTADO:
                mo.ministerio = AMConstants.GOVERNADORIA;
                mo.orgao = AMConstants.GOVERNADORIA;
                break;
            case AMConstants.PROCURADORIA_GERAL_JUSTICA:
                mo.ministerio = AMConstants.MINISTERIO_PUBLICO;
                break;
        }
    }

}
