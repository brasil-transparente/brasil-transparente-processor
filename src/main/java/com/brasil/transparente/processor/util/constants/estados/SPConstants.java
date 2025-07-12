package com.brasil.transparente.processor.util.constants.estados;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class SPConstants {

    public static final String TRIBUNAL_DE_CONTAS_DO_ESTADO = "TRIBUNAL DE CONTAS DO ESTADO";
    public static final String MINISTERIO_PUBLICO = "MINISTERIO PUBLICO";
    public static final String DEFENSORIA_PUBLICA_DO_ESTADO = "DEFENSORIA PUBLICA DO ESTADO";
    public static final String TRIBUNAL_DE_JUSTICA = "TRIBUNAL DE JUSTICA";
    public static final String TRIBUNAL_DE_JUSTICA_MILITAR = "TRIBUNAL DE JUSTICA MILITAR";
    public static final String ASSEMBLEIA_LEGISLATIVA = "ASSEMBLEIA LEGISLATIVA";
    public static final List<String> TRANSFERENCIAS_MUNICIPIOS = Arrays.asList(
            "TRANSFERENCIA A MUNICIPIOS-ICMS",
            "TRANSFERENCIA A MUNICIPIOS-IPVA",
            "TRANSFERENCIA A MUNICIPIOS-IPI",
            "TRANSFERENCIA A MUNICIPIOS-OUTRAS RECEITAS",
            "TRANSFERENCIAS A MUNICIPIOS-OBRAS",
            "TRANSF.A MUNICIPIOS-EQUIP.MATER.PERMANENTE"
    );

}
