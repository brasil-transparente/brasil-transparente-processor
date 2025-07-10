package com.brasil.transparente.processor.util.constants;

import lombok.Getter;

import java.util.List;

@Getter
public final class Constants {

    public static final double ZERO_DOUBLE = 0.00;
    public static final double LESS_THAN_ONE_CENT = 0.009;
    public static final String SEM_INFORMACAO = "Sem informação";
    public static final String REPASSES = "Distribuição Constitucional ou Legal de Rec";
    public static final String AMORTIZACAO_DIVIDA = "Amortização/Refinanciamento da Dívida";
    public static final String TRANSFERENCIA_MUNICIPIOS = "Transferencias A Municipios";
    public static final String PAGAMENTO = "Pagamento";
    public static final List<String> EXECUTIVO = List.of("PODER EXECUTIVO", "Executivo");
    public static final List<String> LEGISLATIVO = List.of("PODER LEGISLATIVO", "Legislativo");
    public static final List<String> JUDICIARIO = List.of("PODER JUDICIARIO", "Judiciário");

}
