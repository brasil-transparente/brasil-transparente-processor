package com.brasil.transparente.processor.util;

import com.brasil.transparente.processor.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {OrdererService.class})
class OrdererServiceTest{

    @Autowired
    private OrdererService ordererService;

    @Test
    void shouldOrderPoderListBySpending() {
        Poder poderLower = Poder.builder()
                .namePoder("PoderLower")
                .totalValueSpent(10000)
                .build();
        Poder poderMiddle = Poder.builder()
                .namePoder("PoderMiddle")
                .totalValueSpent(30000)
                .build();
        Poder poderHigher = Poder.builder()
                .namePoder("PoderHigher")
                .totalValueSpent(50000)
                .build();

        List<Poder> result = ordererService.orderBySpending(new ArrayList<>(List.of(poderLower, poderMiddle, poderHigher)));

        assertEquals("PoderHigher", result.get(0).getNamePoder());
        assertEquals("PoderMiddle", result.get(1).getNamePoder());
        assertEquals("PoderLower", result.get(2).getNamePoder());
    }

    @Test
    void shouldOrderMinisterioListBySpending() {
        Ministerio ministerioLower = Ministerio.builder()
                .nameMinisterio("MinisterioLower")
                .totalValueSpent(10000)
                .build();
        Ministerio ministerioMiddle = Ministerio.builder()
                .nameMinisterio("MinisterioMiddle")
                .totalValueSpent(30000)
                .build();
        Ministerio ministerioHigher = Ministerio.builder()
                .nameMinisterio("MinisterioHigher")
                .totalValueSpent(50000)
                .build();

        List<Ministerio> result = ordererService.orderBySpending(new ArrayList<>(List.of(ministerioLower, ministerioMiddle, ministerioHigher)));

        assertEquals("MinisterioHigher", result.get(0).getNameMinisterio());
        assertEquals("MinisterioMiddle", result.get(1).getNameMinisterio());
        assertEquals("MinisterioLower", result.get(2).getNameMinisterio());
    }

    @Test
    void shouldOrderOrgaoListBySpending() {
        Orgao orgaoLower = Orgao.builder()
                .nameOrgao("OrgaoLower")
                .totalValueSpent(10000)
                .build();
        Orgao orgaoMiddle = Orgao.builder()
                .nameOrgao("OrgaoMiddle")
                .totalValueSpent(30000)
                .build();
        Orgao orgaoHigher = Orgao.builder()
                .nameOrgao("OrgaoHigher")
                .totalValueSpent(50000)
                .build();

        List<Orgao> result = ordererService.orderBySpending(new ArrayList<>(List.of(orgaoLower, orgaoMiddle, orgaoHigher)));

        assertEquals("OrgaoHigher", result.get(0).getNameOrgao());
        assertEquals("OrgaoMiddle", result.get(1).getNameOrgao());
        assertEquals("OrgaoLower", result.get(2).getNameOrgao());
    }

    @Test
    void shouldOrderUnidadeGestoraListBySpending() {
        UnidadeGestora ugLower = UnidadeGestora.builder()
                .nameUnidadeGestora("UGLower")
                .totalValueSpent(10000)
                .build();
        UnidadeGestora ugMiddle = UnidadeGestora.builder()
                .nameUnidadeGestora("UGMiddle")
                .totalValueSpent(30000)
                .build();
        UnidadeGestora ugHigher = UnidadeGestora.builder()
                .nameUnidadeGestora("UGHigher")
                .totalValueSpent(50000)
                .build();

        List<UnidadeGestora> result = ordererService.orderBySpending(new ArrayList<>(List.of(ugLower, ugMiddle, ugHigher)));

        assertEquals("UGHigher", result.get(0).getNameUnidadeGestora());
        assertEquals("UGMiddle", result.get(1).getNameUnidadeGestora());
        assertEquals("UGLower", result.get(2).getNameUnidadeGestora());
    }

    @Test
    void shouldOrderElementoDespesaListBySpending() {
        ElementoDespesa edLower = ElementoDespesa.builder()
                .nameElementoDespesa("EDLower")
                .totalValueSpent(10000)
                .build();
        ElementoDespesa edMiddle = ElementoDespesa.builder()
                .nameElementoDespesa("EDMiddle")
                .totalValueSpent(30000)
                .build();
        ElementoDespesa edHigher = ElementoDespesa.builder()
                .nameElementoDespesa("EDHigher")
                .totalValueSpent(50000)
                .build();

        List<ElementoDespesa> result = ordererService.orderBySpending(new ArrayList<>(List.of(edLower, edMiddle, edHigher)));

        assertEquals("EDHigher", result.get(0).getNameElementoDespesa());
        assertEquals("EDMiddle", result.get(1).getNameElementoDespesa());
        assertEquals("EDLower", result.get(2).getNameElementoDespesa());
    }

}
