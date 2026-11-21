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
    public static final String MINISTERIO_TRABALHO_EMPREGO = "Ministério do Trabalho e Emprego";
    public static final List<String> DESPESAS_DIVIDA_PUBLICA = List.of(
            "Juros, Deságios e Descontos da Dívida Mobiliária",
            "Juros sobre a Dívida por Contrato",
            "Outros Encargos sobre a Dívida Mobiliária",
            "Outros Encargos sobre a Dívida por Contrato",
            "Obrigações decorrentes de Política Monetária",
            "Juros sobre a Dívida Externa por Contrato",
            "Encargos sobre a Dívida Externa por Contrato"
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
    public static final String TRANSFERENCIA_RENDA = "Programas de Transferência de Renda";
    public static final String MINISTERIO_DEFESA = "Ministério da Defesa";
    public static final String PODER_JUDICIARIO = "Poder Judiciário";
    public static final String PODER_JUDICIARIO_FEDERAL = "Poder Judiciário Federal";
    public static final String DEFESA = "Defesa";
    public static final String SEGURO_DESEMPREGO_ABONO_SALARIAL = "Seguro Desemprego e Abono Salarial";
    public static final String OUTROS = "Outros";
}
