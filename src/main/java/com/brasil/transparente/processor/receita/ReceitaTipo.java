package com.brasil.transparente.processor.receita;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReceitaTipo {
    TITULOS_DIVIDA("Títulos da Dívida"),
    IRPF("Imposto de Renda - Pessoa Física"),
    IRPJ("Imposto de Renda - Pessoa Jurídica"),
    IRRF("Imposto de Renda Retido na Fonte"),
    IPI("IPI - Imposto sobre Produtos Industrializados"),
    IOF("IOF - Imposto sobre Operações Financeiras"),
    ITR("ITR - Imposto sobre a Propriedade Territorial Rural"),
    II("Imposto de Importação"),
    LOTERIAS("Loterias"),
    DIVIDENDOS("Dividendos"),
    CIDE("CIDE - Contribuição de Intervenção no Domínio Econômico"),
    RECURSOS_NATURAIS("Recursos Naturais"),
    SALARIO_EDUCACAO("Salário-Educação"),
    COFINS("COFINS"),
    PIS_PASEP("PIS/PASEP"),
    CSLL("CSLL - Contribuição Social sobre o Lucro Líquido"),
    CONTRIB_PREVIDENCIARIA("Contribuição Previdenciária"),
    RETORNO_DE_EMPRESTIMOS("Retorno de Empréstimos"),
    RENDIMENTOS_FINANCEIROS_E_PATRIMONIAIS("Rendimentos Financeiros e Patrimoniais"),
    RECEITAS_ADMINISTRATIVAS_RFB("Receitas Administrativas RFB"),
    MULTAS_E_PENALIDADES("Multas e Penalidades"),
    SERVICOS_PUBLICOS_ADMINISTRATIVOS("Serviços Públicos Administrativos"),
    TAXAS("Taxas"),
    OUTROS("Outros");

    private final String descricao;
}

