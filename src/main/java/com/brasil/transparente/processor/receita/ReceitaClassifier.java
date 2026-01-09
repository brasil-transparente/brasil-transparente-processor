package com.brasil.transparente.processor.receita;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReceitaClassifier {

    public static ReceitaTipo classify(String raw) {
        if (raw == null || raw.isBlank()) {
            return ReceitaTipo.OUTROS;
        }

        String s = raw.toUpperCase()
                .replace("\"", "")
                .replace('\u00A0', ' ')
                .trim();

        if (s.startsWith("TIT.RESP.TN") || s.startsWith("TITULOS DA DIVIDA AGRARIA")) return ReceitaTipo.TITULOS_DIVIDA;

        if (s.startsWith("IRRF")) return ReceitaTipo.IRRF;
        if (s.startsWith("IRPF")) return ReceitaTipo.IRPF;
        if (s.startsWith("IRPJ")) return ReceitaTipo.IRPJ;
        if (s.startsWith("IPI")) return ReceitaTipo.IPI;
        if (s.startsWith("IOF")) return ReceitaTipo.IOF;
        if (s.startsWith("ITR")) return ReceitaTipo.ITR;
        if (s.startsWith("CSLL")) return ReceitaTipo.CSLL;
        if (s.startsWith("MULTA") || s.startsWith("MLT.") || s.startsWith("MUL.") || s.startsWith("MULT.") || s.startsWith("BENS,DIR.VAL.PERD.") || s.startsWith("ALIEN.BENS MERC.APREEND") || s.startsWith("DEP.ABANDONADOS")
        || s.startsWith("INDENIZ.") || s.startsWith("ONUS DE SUCUMBENCIA") || s.startsWith("OUTROS RESSARCIMENTOS") || s.startsWith("PRESTACAO DE CONTAS ELEITORAIS-MULTAS E JUROS") || s.startsWith("REC.POR FORCA DEC.JUD.")
        || s.startsWith("RESS.DANO CAUS.USURP.REC.") || s.startsWith("RESSARCIMENTO DE CUSTOS-MULTAS E JUROS") || s.startsWith("TERMO DE AJUSTAMENTO DE CONDUTA")) return ReceitaTipo.MULTAS_E_PENALIDADES;
        if (s.startsWith("SERV.") || s.startsWith("ADICIONAL SOBRE TARIFA AEROPORTUARIA") || s.startsWith("SERVICOS DE NAVEGACAO") || s.startsWith("SERVICOS DE INFORMACAO") || s.startsWith("TARIFA AEROPORTUARIA")
        || s.startsWith("SERVS.TECNICOS.APROV")) return ReceitaTipo.SERVICOS_PUBLICOS_ADMINISTRATIVOS;
        if (s.startsWith("IMPOSTO SOBRE A IMPORTACAO")) return ReceitaTipo.II;
        if (s.startsWith("CONTRIB.S/LOTERIAS") || s.startsWith("PART.UNIAO EM REC.LOT.") || s.startsWith("PART.UNIAO EM RECEITA") || s.startsWith("OUTORGA LOTERIA APOSTAS")
        || s.startsWith("PREMIOS PRESCRITOS CONCUR.") || s.startsWith("CONTRIBUICAO SOBRE A LOTERIA") || s.startsWith("CONTRIBUICAO SOBRE LOTERIAS") || s.startsWith("CONTRIB.S/LOTERIA PROGNOST.")) return ReceitaTipo.LOTERIAS;

        if (s.contains("AMORT")) return ReceitaTipo.RETORNO_DE_EMPRESTIMOS;
        if (s.contains("COFINS")) return ReceitaTipo.COFINS;
        if (s.contains("PIS/PASEP")) return ReceitaTipo.PIS_PASEP;
        if (s.contains("PREVID") || s.contains("RGPS") || s.contains("CONTR.PREV") || s.contains("CONTRIB.PATRONAL-SERV") || s.contains("CONTRIBUICAO DO SERVIDOR CIVIL") || s.contains("CONTR.P/CUST.PENS.MILIT")
        || s.contains("CONTRIB.DO SERVIDOR") || s.contains("CONTRIB.SENT.JUD.-SERV.CIVIL") || s.contains("CONT.PREV.EMPREG") || s.contains("CONTR.CUST.PENSOES") || s.startsWith("CONTR.PATR.-SENT.JUD-SERV.")
        || s.startsWith("CONTRIB.SERVIDOR CIVIL") || s.startsWith("REST.CONTRIB.P/PREV.")) return ReceitaTipo.CONTRIB_PREVIDENCIARIA;

        if (s.contains("REMUNER.DISPONIBILIDADES DO TESOURO-PRINC.") || s.contains("JUROS SOBRE O CAPITAL PROPRIO-PRINCIPAL") || s.contains("RETORNO DE OP.,JUR.E ENC.FINANCEIROS-PRINC.")
        || s.contains("REMUN.S/REPASSE P") || s.contains("CESSAO DIR") || s.contains("ALUGUEIS") || s.contains("FOROS,LAUDEMIOS") || s.contains("REMUN.SALDOS") || s.startsWith("ALIEN.TIT.,VAL.MOB")
        || s.startsWith("ALIENACAO DE BENS") || s.contains("DIR.USO IMG") || s.startsWith("JUROS DE TITULOS") || s.startsWith("OUTRAS RECEITAS IMOBILIARIAS") || s.startsWith("OUTRAS RECEITAS PATRIMONIAIS")
        || s.startsWith("REMUNERACAO DE DEPOSITOS") || s.startsWith("ROYAL.COMERC.PROD.") || s.startsWith("VARIACAO CAMBIAL")
        || s.contains("OPERACAO DE CREDITO") || s.contains("REMUNERACAO DAS DISPONIBILIDADES") || s.contains("GARANTIAS") || s.contains("RETORNO DE OP.,JUR.") || s.startsWith("OP.DE CREDITO")
        || s.startsWith("ALIENACAO DE ESTOQUES") || s.startsWith("VAL.NAO TRIB.AUF.UNIAO A OP.FERROV") || s.startsWith("RESERVA GLOBAL DE REVERSAO")) return ReceitaTipo.RENDIMENTOS_FINANCEIROS_E_PATRIMONIAIS;

        if (s.contains("DIVIDENDOS") || s.contains("JUROS SOBRE O CAPITAL PROPRIO") || s.contains("RECURSOS MINERAIS")) return ReceitaTipo.DIVIDENDOS;
        if (s.contains("PETRO") || s.contains("PETR.") || s.contains("HIDRICOS") || s.contains("FLORESTAS") || s.startsWith("BONUS ASS.CONTR.PARTILHA") || s.startsWith("BONUS ASSINAT.") || s.startsWith("CESS.DIR.USO.RADFRQ")
        || s.startsWith("CONCESSAO FLOREST.NACIONAIS") || s.startsWith("CONC.SERV.GER.TRANSM.DISTR.ENER") || s.startsWith("CONTR.S/REC.CONCESS.PERM.ENERG.ELETR") || s.contains("CONCESSAO FLORESTAL")
        || s.startsWith("DELEG.P/EXPL") || s.startsWith("DELEG.P/PREST.SERV") || s.startsWith("DLG.SRV") || s.startsWith("OUTORGA DIR") || s.startsWith("OUT.DIR.EXPLOR") || s.startsWith("OUTRAS.DLG.SV.TELEC")
        || s.startsWith("PART.PROPR.TERRA") || s.startsWith("PGTO.PELA RETENCAO AREA EXPL.") || s.startsWith("RES.POSIT.OP.COMERC.") || s.startsWith("UTIL.REC.HID-DEM.")) return ReceitaTipo.RECURSOS_NATURAIS;
        if (s.contains("SALARIO-EDUCACAO")) return ReceitaTipo.SALARIO_EDUCACAO;
        if (s.contains("CIDE") || s.startsWith("CONDECINE") || s.startsWith("CONTR.S/REC") || s.startsWith("CONTRIB.P/FOMENTO RADIODIFUSAO") || s.startsWith("COTA-PARTE DO AFRMM")) return ReceitaTipo.CIDE;
        if (s.contains("RFB")) return ReceitaTipo.RECEITAS_ADMINISTRATIVAS_RFB;
        if (s.contains("TAXA") || s.contains("TX.") || s.startsWith("BARREIRAS TECNICAS AO COMERC") || s.startsWith("EMOLUMENTOS E CUSTAS")) return ReceitaTipo.TAXAS;

        return ReceitaTipo.OUTROS;
    }
}
