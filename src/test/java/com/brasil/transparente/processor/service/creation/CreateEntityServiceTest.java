package com.brasil.transparente.processor.service.creation;

import com.brasil.transparente.processor.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {CreateEntityService.class})
class CreateEntityServiceTest {

    @InjectMocks
    private CreateEntityService createEntityService;

    private Poder poder;
    private Ministerio ministerio;
    private Orgao orgao;
    private ElementoDespesa elementoDespesa;
    private  UnidadeGestora unidadeGestora;

    @BeforeEach
    void setUp() {
        poder = new Poder("Executivo");
        ministerio = new Ministerio("Ministério da Defesa");
        orgao = new Orgao("Comando do Exército");
        elementoDespesa = new ElementoDespesa("Departamento de logistica");
        unidadeGestora = new UnidadeGestora("Comando Logístico");

        unidadeGestora.getListElementoDespesa().add(elementoDespesa);
        poder.getListMinisterio().add(ministerio);
        ministerio.getListOrgao().add(orgao);
        orgao.getListUnidadeGestora().add(unidadeGestora);
    }

    @Test
    void findOrCreateMinisterio_shouldFindExistingMinisterio() {
        int initialSize = poder.getListMinisterio().size();
        Ministerio foundMinisterio = createEntityService.findOrCreateMinisterio("Ministério da Defesa", poder);

        assertNotNull(foundMinisterio);
        assertSame(ministerio, foundMinisterio);
        assertEquals(initialSize, poder.getListMinisterio().size());
    }

    @Test
    void findOrCreateMinisterio_shouldCreateNewMinisterio() {
        int initialSize = poder.getListMinisterio().size();
        Ministerio newMinisterio = createEntityService.findOrCreateMinisterio("Ministério da Economia", poder);

        assertNotNull(newMinisterio);
        assertEquals("Ministério da Economia", newMinisterio.getNameMinisterio());
        assertEquals(initialSize + 1, poder.getListMinisterio().size());
    }

    @Test
    void findOrCreateUnidadeGestora_shouldFindExistingUnidadeGestora() {
        int initialSize = ministerio.getListOrgao().getFirst().getListUnidadeGestora().size();
        UnidadeGestora foundUnidadeGestora = createEntityService.findOrCreateUnidadeGestora("Comando Logístico", orgao);

        assertNotNull(unidadeGestora);
        assertEquals(initialSize,  ministerio.getListOrgao().getFirst().getListUnidadeGestora().size());
        assertSame(unidadeGestora, foundUnidadeGestora);
    }

    @Test
    void findOrCreateUnidadeGestora_shouldCreateNewUnidadeGestora() {
        int initialSize = ministerio.getListOrgao().getFirst().getListUnidadeGestora().size();
        UnidadeGestora newUnidadeGestora = createEntityService.findOrCreateUnidadeGestora("Centro de urgências", orgao);

        assertNotNull(unidadeGestora);
        assertEquals(initialSize + 1,  ministerio.getListOrgao().getFirst().getListUnidadeGestora().size());
        assertEquals("Centro de urgências", newUnidadeGestora.getNameUnidadeGestora());
    }

    @Test
    void findOrCreateOrgao_shouldFindExistingOrgao() {
        int initialSize = ministerio.getListOrgao().size();
        Orgao foundOrgao = createEntityService.findOrCreateOrgao("Comando do Exército", ministerio);
        assertEquals(initialSize, ministerio.getListOrgao().size());
        assertSame(orgao, foundOrgao);
    }

    @Test
    void findOrCreateOrgao_shouldCreateNewOrgao() {
        int initialSize = ministerio.getListOrgao().size();
        Orgao newOrgao = createEntityService.findOrCreateOrgao("Comando da Marinha", ministerio);
        assertEquals("Comando da Marinha", newOrgao.getNameOrgao());
        assertEquals(initialSize + 1, ministerio.getListOrgao().size());
    }

    @Test
    void findOrCreateElementoDespesa_shouldFindExistingElementoDespesa() {
        int initialSize = orgao.getListUnidadeGestora().getFirst().getListElementoDespesa().size();

        ElementoDespesa foundElementoDespesa = createEntityService.findOrCreateElementoDespesa("Departamento de logistica", unidadeGestora);

        assertEquals(initialSize, orgao.getListUnidadeGestora().getFirst().getListElementoDespesa().size());
        assertSame(elementoDespesa, foundElementoDespesa);
    }

    @Test
    void findOrCreateElementoDespesa_shouldCreateNewElementoDespesa() {
        int initialSize = orgao.getListUnidadeGestora().getFirst().getListElementoDespesa().size();
        ElementoDespesa newElementoDespesa = createEntityService.findOrCreateElementoDespesa("Papel A4", unidadeGestora);

        assertEquals("Papel A4", newElementoDespesa.getNameElementoDespesa());
        assertEquals(initialSize + 1, orgao.getListUnidadeGestora().getFirst().getListElementoDespesa().size());
    }

    @Test
    void setRelationships_shouldSetBidirectionalRelationsCorrectly() {
        UnidadeFederativa mockUf = mock(UnidadeFederativa.class);
        Poder mockPoder = mock(Poder.class);
        Ministerio mockMinisterio = mock(Ministerio.class);
        Orgao mockOrgao = mock(Orgao.class);
        UnidadeGestora mockUg = mock(UnidadeGestora.class);
        ElementoDespesa mockEd = mock(ElementoDespesa.class);

        when(mockUg.getListElementoDespesa()).thenReturn(List.of(mockEd));
        when(mockOrgao.getListUnidadeGestora()).thenReturn(List.of(mockUg));
        when(mockMinisterio.getListOrgao()).thenReturn(List.of(mockOrgao));
        when(mockPoder.getListMinisterio()).thenReturn(List.of(mockMinisterio));
        when(mockUf.getListPoder()).thenReturn(List.of(mockPoder));

        createEntityService.setRelationships(mockUf);

        assertEquals(mockPoder, mockUf.getListPoder().getFirst());
        assertEquals(mockMinisterio, mockUf.getListPoder().getFirst().getListMinisterio().getFirst());
        assertEquals(mockMinisterio, mockUf.getListPoder().getFirst().getListMinisterio().getFirst());

        verify(mockPoder).setUnidadeFederativa(mockUf);
        verify(mockMinisterio).setPoder(mockPoder);
        verify(mockOrgao).setMinisterio(mockMinisterio);
        verify(mockUg).setOrgao(mockOrgao);
        verify(mockEd).setUnidadeGestora(mockUg);
    }
}
