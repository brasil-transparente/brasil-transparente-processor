package com.brasil.transparente.processor.util;

import com.brasil.transparente.processor.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {NameCorrector.class})
class NameCorrectorTest {

    @Autowired
    private NameCorrector nameCorrector;
    @MockBean
    private NameCorrectorLoader nameCorrectorLoader;

    @BeforeEach
    void setUp() {
        Map<String, String> corrections = Map.of(
                "MinAntigo", "MinNovo",
                "OrgaoAntigo", "OrgaoNovo",
                "UGAntiga", "UGNova",
                "ElementoAntigo", "ElementoNovo"
        );
        when(nameCorrectorLoader.getCorrectionsHashMap()).thenReturn(corrections);
    }

    @Test
    void testRefactorNames_appliesCorrections() {
        ElementoDespesa elementoDespesa = new ElementoDespesa();
        elementoDespesa.setNameElementoDespesa("ElementoAntigo");

        UnidadeGestora unidadeGestora = new UnidadeGestora();
        unidadeGestora.setNameUnidadeGestora("UGAntiga");
        unidadeGestora.setListElementoDespesa(List.of(elementoDespesa));

        Orgao orgao = new Orgao();
        orgao.setNameOrgao("OrgaoAntigo");
        orgao.setListUnidadeGestora(List.of(unidadeGestora));

        Ministerio ministerio = new Ministerio();
        ministerio.setNameMinisterio("MinAntigo");
        ministerio.setListOrgao(List.of(orgao));

        Poder poder = new Poder();
        poder.setListMinisterio(List.of(ministerio));

        nameCorrector.refactorNames(List.of(poder));

        assertEquals("MinNovo", ministerio.getNameMinisterio());
        assertEquals("OrgaoNovo", orgao.getNameOrgao());
        assertEquals("UGNova", unidadeGestora.getNameUnidadeGestora());
        assertEquals("ElementoNovo", elementoDespesa.getNameElementoDespesa());
    }

    @Test
    void testRefactorNames_doesNothingWhenNoCorrection() {
        when(nameCorrectorLoader.getCorrectionsHashMap()).thenReturn(Collections.emptyMap());

        ElementoDespesa elementoDespesa = new ElementoDespesa();
        elementoDespesa.setNameElementoDespesa("SemCorrecao");

        UnidadeGestora unidadeGestora = new UnidadeGestora();
        unidadeGestora.setNameUnidadeGestora("SemCorrecao");
        unidadeGestora.setListElementoDespesa(List.of(elementoDespesa));

        Orgao orgao = new Orgao();
        orgao.setNameOrgao("SemCorrecao");
        orgao.setListUnidadeGestora(List.of(unidadeGestora));

        Ministerio ministerio = new Ministerio();
        ministerio.setNameMinisterio("SemCorrecao");
        ministerio.setListOrgao(List.of(orgao));

        Poder poder = new Poder();
        poder.setListMinisterio(List.of(ministerio));

        nameCorrector.refactorNames(List.of(poder));

        assertEquals("SemCorrecao", ministerio.getNameMinisterio());
        assertEquals("SemCorrecao", orgao.getNameOrgao());
        assertEquals("SemCorrecao", unidadeGestora.getNameUnidadeGestora());
        assertEquals("SemCorrecao", elementoDespesa.getNameElementoDespesa());
    }
}
