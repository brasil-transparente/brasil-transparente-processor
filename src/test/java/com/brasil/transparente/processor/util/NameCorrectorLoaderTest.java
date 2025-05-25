package com.brasil.transparente.processor.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NameCorrectorLoaderTest {

    private ResourceLoader resourceLoader;
    private Resource resource;
    private NameCorrectorLoader nameCorrectorLoader;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        resourceLoader = mock(ResourceLoader.class);
        resource = mock(Resource.class);
        nameCorrectorLoader = new NameCorrectorLoader();

        var resourceLoaderField = NameCorrectorLoader.class.getDeclaredField("resourceLoader");
        resourceLoaderField.setAccessible(true);
        resourceLoaderField.set(nameCorrectorLoader, resourceLoader);
    }

    @Test
    void testGetCorrectionsHashMap_success() throws IOException {
        String json = "{\"Ministério A\":\"Ministério Corrigido A\",\"Ministério B\":\"Ministério Corrigido B\"}";
        byte[] jsonBytes = json.getBytes();

        when(resourceLoader.getResource("classpath:name-correction.json")).thenReturn(resource);
        when(resource.getContentAsByteArray()).thenReturn(jsonBytes);

        Map<String, String> corrections = nameCorrectorLoader.getCorrectionsHashMap();

        assertEquals(2, corrections.size());
        assertEquals("Ministério Corrigido A", corrections.get("Ministério A"));
        assertEquals("Ministério Corrigido B", corrections.get("Ministério B"));
    }

    @Test
    void testGetCorrectionsHashMap_throwsUncheckedIOException() throws IOException {
        when(resourceLoader.getResource("classpath:name-correction.json")).thenReturn(resource);
        when(resource.getContentAsByteArray()).thenThrow(new IOException("Falha na leitura"));

        UncheckedIOException exception = assertThrows(UncheckedIOException.class, () -> {
            nameCorrectorLoader.getCorrectionsHashMap();
        });

        assertTrue(exception.getMessage().contains("Erro ao carregar correções de ministérios"));
        assertNotNull(exception.getCause());
    }
}