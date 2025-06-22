package com.brasil.transparente.processor.service;

import com.brasil.transparente.processor.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
@Service
public class CreatorService {

    public Ministerio findOrCreateMinisterio(String nameMinisterio, Poder poder) {
        for (Ministerio ministerio : poder.getListMinisterio()) {
            if (Objects.equals(nameMinisterio, ministerio.getNameMinisterio())) {
                return ministerio;
            }
        }
        return createMinisterio(nameMinisterio, poder);
    }

    private Ministerio createMinisterio(String nameMinisterioLine, Poder poder) {
        Ministerio newMinisterio = new Ministerio(nameMinisterioLine);
        poder.getListMinisterio().add(newMinisterio);
        return newMinisterio;
    }

    public Orgao findOrCreateOrgao(String nameOrgao, Ministerio ministerio) {
        if (!ministerio.getListOrgao().isEmpty()) {
            for (Orgao orgao : ministerio.getListOrgao()) {
                if (Objects.equals(nameOrgao, orgao.getNameOrgao())) {
                    return orgao;
                }
            }
        }
        return createOrgao(nameOrgao, ministerio);
    }

    private Orgao createOrgao(String nameOrgao, Ministerio ministerio) {
        Orgao newOrgao = new Orgao(nameOrgao);
        ministerio.getListOrgao().add(newOrgao);
        return newOrgao;
    }

    public UnidadeGestora findOrCreateUnidadeGestora(String nameUnidadeGestora, Orgao orgao) {
        if (!orgao.getListUnidadeGestora().isEmpty()) {
            for (UnidadeGestora unidadeGestora : orgao.getListUnidadeGestora()) {
                if (Objects.equals(nameUnidadeGestora, unidadeGestora.getNameUnidadeGestora())) {
                    return unidadeGestora;
                }
            }
        }
        return createUnidadeGestora(nameUnidadeGestora, orgao);
    }

    private UnidadeGestora createUnidadeGestora(String nameUnidadeGestora, Orgao orgao) {
        UnidadeGestora newUnidadeGestora = new UnidadeGestora(nameUnidadeGestora);
        orgao.getListUnidadeGestora().add(newUnidadeGestora);
        return newUnidadeGestora;
    }

    public ElementoDespesa findOrCreateNewElementoDespesa(String nameElementoDespesaLine, UnidadeGestora unidadeGestora) {
        if (!unidadeGestora.getListElementoDespesa().isEmpty()) {
            for (ElementoDespesa elementoDespesa : unidadeGestora.getListElementoDespesa()) {
                if (Objects.equals(nameElementoDespesaLine, elementoDespesa.getNameElementoDespesa())) {
                    return elementoDespesa;
                }
            }
        }
        return createElementoDespesa(nameElementoDespesaLine, unidadeGestora);
    }

    private ElementoDespesa createElementoDespesa(String nameElementoDespesaLine, UnidadeGestora unidadeGestora) {
        ElementoDespesa elementoDespesa = new ElementoDespesa(nameElementoDespesaLine);
        unidadeGestora.getListElementoDespesa().add(elementoDespesa);
        return elementoDespesa;
    }

    public void setRelationships(UnidadeFederativa unidadeFederativa) {
        log.info("{} - Criando relacionamentos entre as entidades", unidadeFederativa.getNameUnidadeFederativa());
        for (Poder poder : unidadeFederativa.getListPoder()) {
            for (Ministerio ministerio : poder.getListMinisterio()) {
                for (Orgao orgao : ministerio.getListOrgao()) {
                    for (UnidadeGestora unidadeGestora : orgao.getListUnidadeGestora()) {
                        for (ElementoDespesa elementoDespesa : unidadeGestora.getListElementoDespesa()) {
                            elementoDespesa.setUnidadeGestora(unidadeGestora);
                        }
                        unidadeGestora.setOrgao(orgao);
                    }
                    orgao.setMinisterio(ministerio);
                }
                ministerio.setPoder(poder);
            }
            poder.setUnidadeFederativa(unidadeFederativa);
        }
    }

}
