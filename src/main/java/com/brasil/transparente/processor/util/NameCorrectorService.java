package com.brasil.transparente.processor.util;

import com.brasil.transparente.processor.entity.*;
import com.brasil.transparente.processor.util.constants.EmbaixadasConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class NameCorrectorService {

    private final NameCorrectorLoader nameCorrectorLoader;

    public void refactorNames(List<Poder> poderList) {
        log.info("Refatorando nomes");
        Map<String, String> corrections = nameCorrectorLoader.getCorrectionsHashMap();
        for (Poder poder : poderList) {
            for (Ministerio ministerio : poder.getListMinisterio()) {
                ministerio.setNameMinisterio(correctName(ministerio.getNameMinisterio(), corrections));
                for (Orgao orgao : ministerio.getListOrgao()) {
                    orgao.setNameOrgao(correctName(orgao.getNameOrgao(), corrections));
                    for (UnidadeGestora unidadeGestora : orgao.getListUnidadeGestora()) {
                        unidadeGestora.setNameUnidadeGestora(correctName(unidadeGestora.getNameUnidadeGestora(), corrections));
                        for (ElementoDespesa elementoDespesa : unidadeGestora.getListElementoDespesa()) {
                            elementoDespesa.setNameElementoDespesa(correctName(elementoDespesa.getNameElementoDespesa(), corrections));
                        }
                    }
                }
            }
        }
    }

    private String correctName(String original, Map<String, String> corrections) {
        return corrections.getOrDefault(original, original);
    }

    public String mergeEmbassyNames(String unidadeGestora) {
        return switch (unidadeGestora) {
            case EmbaixadasConstants.EMBAIXADA_CHINA_PEQUIM_USD, EmbaixadasConstants.EMBAIXADA_CHINA_PEQUIM_LOCAL -> EmbaixadasConstants.EMBAIXADA_CHINA_PEQUIM;
            case EmbaixadasConstants.EMBAIXADA_CHINA_XANGAI_USD, EmbaixadasConstants.EMBAIXADA_CHINA_XANGAI_LOCAL -> EmbaixadasConstants.EMBAIXADA_CHINA_XANGAI;
            case EmbaixadasConstants.EMBAIXADA_CHINA_CANTAO_USD, EmbaixadasConstants.EMBAIXADA_CHINA_CANTAO_LOCAL -> EmbaixadasConstants.EMBAIXADA_CHINA_CANTAO;
            case EmbaixadasConstants.EMBAIXADA_BOLIVIA_USD, EmbaixadasConstants.EMBAIXADA_BOLIVIA_LOCAL -> EmbaixadasConstants.EMBAIXADA_BOLIVIA;
            case EmbaixadasConstants.EMBAIXADA_TAILANDIA_USD, EmbaixadasConstants.EMBAIXADA_TAILANDIA_LOCAL -> EmbaixadasConstants.EMBAIXADA_TAILANDIA;
            case EmbaixadasConstants.EMBAIXADA_POLONIA_USD, EmbaixadasConstants.EMBAIXADA_POLONIA_LOCAL -> EmbaixadasConstants.EMBAIXADA_POLONIA;
            case EmbaixadasConstants.EMBAIXADA_NIGERIA_USD, EmbaixadasConstants.EMBAIXADA_NIGERIA_LOCAL -> EmbaixadasConstants.EMBAIXADA_NIGERIA;
            case EmbaixadasConstants.EMBAIXADA_BEIRUTE_USD, EmbaixadasConstants.EMBAIXADA_BEIRUTE_LOCAL -> EmbaixadasConstants.EMBAIXADA_BEIRUTE;
            case EmbaixadasConstants.EMBAIXADA_PASO_LOS_LIBRES_LOCAL, EmbaixadasConstants.EMBAIXADA_PASO_LOS_LIBRES_USD -> EmbaixadasConstants.EMBAIXADA_PASO_LOS_LIBRES;
            case EmbaixadasConstants.EMBAIXADA_ASSUNCAO_USD -> EmbaixadasConstants.EMBAIXADA_ASSUNCAO;
            case EmbaixadasConstants.EMBAIXADA_ISTAMBUL_USD -> EmbaixadasConstants.EMBAIXADA_ISTAMBUL;
            case EmbaixadasConstants.EMBAIXADA_RAMALA_USD -> EmbaixadasConstants.EMBAIXADA_RAMALA;
            case EmbaixadasConstants.EMBAIXADA_PASO_TEL_AVIV_USD -> EmbaixadasConstants.EMBAIXADA_PASO_TEL_AVIV;
            case EmbaixadasConstants.EMBAIXADA_LA_PAZ_USD -> EmbaixadasConstants.EMBAIXADA_LA_PAZ;
            case EmbaixadasConstants.EMBAIXADA_GENEBRA_EURO -> EmbaixadasConstants.EMBAIXADA_GENEBRA;
            default -> unidadeGestora;
        };
    }

    public String replaceMinisteryName(String nameMinisterio) {
        return switch (nameMinisterio) {
            case EmbaixadasConstants.MINISTERIO_AGRICULTURA_CAPS -> EmbaixadasConstants.MINISTERIO_AGRICULTURA_ORIGINAL;
            case EmbaixadasConstants.MINISTERIO_TRABALHO_CAPS -> EmbaixadasConstants.MINISTERIO_TRABALHO_ORIGINAL;
            case EmbaixadasConstants.SECRETARIA_EMPREENDEDOR_CAPS -> EmbaixadasConstants.SECRETARIA_EMPREENDEDOR_ORIGINAL;
            case EmbaixadasConstants.MINISTERIO_ESPORTE_CAPS -> EmbaixadasConstants.MINISTERIO_ESPORTE_ORIGINAL;
            case EmbaixadasConstants.MINISTERIO_GESTAO_CAPS -> EmbaixadasConstants.MINISTERIO_GESTAO_ORIGINAL;
            case EmbaixadasConstants.MINISTERIO_ASSISTENCIA_SOCIAL_CAPS -> EmbaixadasConstants.MINISTERIO_ASSISTENCIA_SOCIAL_ORIGINAL;
            case EmbaixadasConstants.MINISTERIO_INDUSTRIA_CAPS -> EmbaixadasConstants.MINISTERIO_INDUSTRIA_ORIGINAL;
            case EmbaixadasConstants.PRESIDENCIA_REPUBLICA_CAPS -> EmbaixadasConstants.PRESIDENCIA_REPUBLICA_ORIGINAL;
            case EmbaixadasConstants.MINISTERIO_DESENV_AGRARIO_CAPS -> EmbaixadasConstants.MINISTERIO_DESENV_AGRARIO_ORIGINAL;
            case EmbaixadasConstants.MINISTERIO_EXTERIORES_CAPS -> EmbaixadasConstants.MINISTERIO_RELACOES_EXTERIORES_ORIGINAL;
            default -> nameMinisterio;
        };
    }

}
