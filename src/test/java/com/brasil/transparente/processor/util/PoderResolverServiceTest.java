package com.brasil.transparente.processor.util;

import com.brasil.transparente.processor.entity.Poder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {PoderResolverService.class})
class PoderResolverServiceTest {

    @Autowired
    private PoderResolverService poderResolverService;

    private List<Poder> poderList;

    @BeforeEach
    void setUp() {
        poderList = List.of(
                new Poder("Executivo"),
                new Poder("Legislativo"),
                new Poder("Judiciário"),
                new Poder("Outros")
        );
    }

    @Test
    void testDefinePoder_nullInput_returnsDefault() {
        Poder result = poderResolverService.definePoder(null, poderList);
        assertEquals("Outros", result.getNamePoder());
    }

    @Test
    void testDefinePoder_executivoUpperCase_returnsExecutivo() {
        Poder result = poderResolverService.definePoder("PODER EXECUTIVO", poderList);
        assertEquals("Executivo", result.getNamePoder());
    }

    @Test
    void testDefinePoder_executivoMixedCase_returnsExecutivo() {
        Poder result = poderResolverService.definePoder("Executivo", poderList);
        assertEquals("Executivo", result.getNamePoder());
    }

    @Test
    void testDefinePoder_executivoWithSpaces_returnsExecutivo() {
        Poder result = poderResolverService.definePoder("  PODER EXECUTIVO  ", poderList);
        assertEquals("Executivo", result.getNamePoder());
    }

    @Test
    void testDefinePoder_legislativoUpperCase_returnsLegislativo() {
        Poder result = poderResolverService.definePoder("PODER LEGISLATIVO", poderList);
        assertEquals("Legislativo", result.getNamePoder());
    }

    @Test
    void testDefinePoder_legislativoMixedCase_returnsLegislativo() {
        Poder result = poderResolverService.definePoder("Legislativo", poderList);
        assertEquals("Legislativo", result.getNamePoder());
    }

    @Test
    void testDefinePoder_legislativoWithSpaces_returnsLegislativo() {
        Poder result = poderResolverService.definePoder("  PODER LEGISLATIVO  ", poderList);
        assertEquals("Legislativo", result.getNamePoder());
    }

    @Test
    void testDefinePoder_judiciarioUpperCase_returnsJudiciario() {
        Poder result = poderResolverService.definePoder("PODER JUDICIARIO", poderList);
        assertEquals("Judiciário", result.getNamePoder());
    }

    @Test
    void testDefinePoder_judiciarioMixedCase_returnsJudiciario() {
        Poder result = poderResolverService.definePoder("Judiciário", poderList);
        assertEquals("Judiciário", result.getNamePoder());
    }

    @Test
    void testDefinePoder_judiciarioWithSpaces_returnsJudiciario() {
        Poder result = poderResolverService.definePoder("  PODER JUDICIARIO  ", poderList);
        assertEquals("Judiciário", result.getNamePoder());
    }

    @Test
    void testDefinePoder_unknownInput_returnsDefault() {
        Poder result = poderResolverService.definePoder("Unknown Poder", poderList);
        assertEquals("Outros", result.getNamePoder());
    }

    @Test
    void testDefinePoder_emptyString_returnsDefault() {
        Poder result = poderResolverService.definePoder("", poderList);
        assertEquals("Outros", result.getNamePoder());
    }
}
