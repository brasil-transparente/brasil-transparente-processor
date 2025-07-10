package com.brasil.transparente.processor.util.constants;

import lombok.Getter;

import java.util.List;

@Getter
public class DespesaSimplificadaConstants {

    private DespesaSimplificadaConstants() {
        throw new UnsupportedOperationException("Classe não deve ser instanciada");
    }

    public static final List<String> DESPESAS_APOSENTADORIAS = List.of(
            "Aposentadorias do RGPS - Área Urbana",
            "Aposentadorias do RGPS - Área Rural",
            "Aposentadorias, Reserva Remunerada e Reformas",
            "Aposentadorias e Pensões Civis da União",
            "Aposentadorias RPPS, reserva remunerada e reforma militar",
            "Aposentadorias",
            "Aposentadoria originária de subsídios - Pessoal civil",
            "Férias Vencidas e Proporcionais a Aposentados Civis",
            "Pensões do RGPS - Área Urbana",
            "Pensões do RGPS - Área Rural",
            "Pensões Especiais",
            "Pensões",
            "Pensões Civis",
            "13 Salário - Pensões Civis",
            "Complementação de Pensões - Pessoal Civil",
            "Vantagens Incorporadas - Pensionistas",
            "Pensões do RPPS e do Militar",
            "Pensões Oriundas de Adicional de Qualificação - Civis",
            "Pensões Especiais - Pessoal Civil",
            "Benefício Especial Lei 12.618/2012 - Pensão",
            "Pensões e indenizações oriundas de débitos periódicos vinculados a sentença judicial",
            "Benefício Mensal ao Deficiente e ao Idoso",
            "Contribuição a Entidades Fechadas de Previdência",
            "Contribuição da União, autarquias e fundações para o custeio da previdência dos servidores federais",
            "Contribuições a entidades fechadas de previdência",
            "Contribuição Previdenciária - Serviços Terceiros (PF)",
            "Contribuição a Entidade Fechada de Previdência",
            "Contribuições Previdenciárias - INSS",
            "Contribuições previdenciárias - Serviços de terceiros"
    );
    public static final String PRECATORIOS_RPVS = "Precatórios e Requisições de Pequeno Valor";
    public static final String FRGPS = "Fundo do Regime Geral da Previdência Social";
    public static final String APOSENTADORIAS_PENSOES = "Aposentadorias e Pensões";
    public static final String MINISTERIO_FAZENDA = "Ministério da Fazenda";
    public static final List<String> DESPESAS_DIVIDA_PUBLICA = List.of(
            "Juros, Deságios e Descontos da Dívida Mobiliária",
            "Juros sobre a Dívida por Contrato",
            "Outros Encargos sobre a Dívida Mobiliária",
            "Outros Encargos sobre a Dívida por Contrato",
            "Obrigações decorrentes de Política Monetária",
            "Principal da Dívida Contratual Resgatado"
    );
    public static final String JUROS_DIVIDA_PUBLICA = "Juros da Dívida Pública";
    public static final String MINISTERIO_EDUCACAO = "Ministério da Educação";
    public static final String EDUCACAO = "Educação";
    public static final String MINISTERIO_SAUDE = "Ministério da Saúde";
    public static final String SAUDE = "Saúde";
    public static final String MINISTERIO_ASSISTENCIA_SOCIAL = "Ministério do Desenvolvimento e Assistência Social, Família e Combate à Fome";
    public static final List<String> AUXILIOS = List.of(
            "Auxílios Financeiros a Pessoa Física",
            "Auxílios"
    );
    public static final String TRANSFERENCIA_RENDA = "Programas de Transferência de Renda*";
    public static final String MINISTERIO_DEFESA = "Ministério da Defesa";
    public static final String PODER_JUDICIARIO = "Poder Judiciário";
    public static final String PODER_JUDICIARIO_FEDERAL = "Poder Judiciário Federal";
    public static final String PODER_JUDICIARIO_ESTADUAL = "Poder Judiciário Estadual";
    public static final String DEFESA = "Defesa";
    public static final String BENEFICIOS_TRABALHISTAS = "Benefícios Trabalhistas**";
    public static final String SEGURO_DESEMPREGO_ABONO_SALARIAL = "Seguro Desemprego e Abono Salarial";
    public static final String OUTROS = "Outros";
    public static final String TERMO_APOSENTADORIA1 = "aposent";
    public static final String TERMO_APOSENTADORIA2 = "pens";
    public static final String TERMO_APOSENTADORIA3 = "previd";
    public static final List<String> INSTITUTO_PREVIDENCIA_RS = List.of("Instituto de Previdência do Estado do Rio Grande do Sul");
    public static final List<String> SECRETARIA_SAUDE = List.of(
            "Secretaria da Saúde",
            "Secretaria de Estado de Saúde"
            );
    public static final List<String> SECRETARIA_EDUCACAO = List.of(
            "Secretaria da Educação",
            "Secretaria de Estado de Educação e Desporto"
            );
    public static final String SEGURANCA = "Segurança";
    public static final List<String> FUNDOS_PREVIDENCIA_BAHIA = List.of(
            "Fundo Financeiro da Previdência Social dos Servidores Públicos do Estado da Bahia",
            "Fundo de Proteção Social dos Policiais Militares e dos Bombeiros Militares do Estado da Bahia"
    );
    public static final List<String> FUNDO_PREVIDENCIA_AMAZONAS = List.of(
            "Fundação Fundo Previdenciário do Estado do Amazonas"
    );
    public static final String DIVIDA_PUBLICA = "Dívida Pública";
    public static final String ENCARGOS_GERAIS = "Encargos Gerais do Estado";
    public static final String SECRETARIA_ESTADO_FAZENDA = "Secretaria de Estado da Fazenda";
    public static final String INFRAESTRUTURA = "Infraestrutura";
    public static final List<String> SECRETARIAS_SEGURANCA = List.of(
            "Secretaria da Segurança Pública",
            "Secretaria de Sistemas Penal e Socioeducativo",
            "Secretaria de Administração Penitenciária e Ressocialização",
            "Secretaria de Estado da Segurança Pública",
            "Secretaria de Estado de Administração Penitenciária"
    );
    public static final List<String> SECRETARIAS_INFRAESTRUTURA = List.of(
            "Secretaria de Infraestrutura",
            "Secretaria de Desenvolvimento Urbano",
            "Secretaria de Infraestrutura Hídrica e Saneamento"
    );
}
