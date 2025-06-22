package com.brasil.transparente.processor.util;

import com.brasil.transparente.processor.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class NameCorrector {

    private final NameCorrectorLoader nameCorrectorLoader;
    private Map<String, String> correctionHashMap;

    public void refactorNames(List<Poder> poderList) {
        log.info("Refatorando nomes");
        correctionHashMap = nameCorrectorLoader.getCorrectionsHashMap();
        for (Poder poder : poderList) {
            for (Ministerio ministerio : poder.getListMinisterio()) {
                correctMinisterioNames(ministerio);
                for (Orgao orgao : ministerio.getListOrgao()) {
                    correctOrgaoNames(orgao);
                    for (UnidadeGestora unidadeGestora : orgao.getListUnidadeGestora()) {
                        correctUnidadeGestoraNames(unidadeGestora);
                        for (ElementoDespesa elementoDespesa : unidadeGestora.getListElementoDespesa()) {
                            correctElementDespesaNames(elementoDespesa);
                        }
                    }
                }
            }
        }
    }

    private void correctMinisterioNames(Ministerio ministerio) {
        String original = ministerio.getNameMinisterio();
        String corrected = correctionHashMap.getOrDefault(original, original);
        ministerio.setNameMinisterio(corrected);
    }

    private void correctOrgaoNames(Orgao orgao) {
        String original = orgao.getNameOrgao();
        String corrected = correctionHashMap.getOrDefault(original, original);
        orgao.setNameOrgao(corrected);
    }

    private void correctUnidadeGestoraNames(UnidadeGestora unidadeGestora) {
        String original = unidadeGestora.getNameUnidadeGestora();
        String corrected = correctionHashMap.getOrDefault(original, original);
        unidadeGestora.setNameUnidadeGestora(corrected);
    }

    private void correctElementDespesaNames(ElementoDespesa elementoDespesa) {
        String original = elementoDespesa.getNameElementoDespesa();
        String corrected = correctionHashMap.getOrDefault(original, original);
        elementoDespesa.setNameElementoDespesa(corrected);
    }

}
