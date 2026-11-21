package com.brasil.transparente.processor.util;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class NameCorrectorLoader {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final ResourceLoader resourceLoader;

    public Map<String, String> getCorrectionsHashMap() {
        try {
            var resource = resourceLoader.getResource("classpath:name-correction.json");
            return OBJECT_MAPPER.readValue(
                    resource.getContentAsByteArray(),
                    new TypeReference<>() {}
            );
        } catch (IOException e) {
            throw new UncheckedIOException("Erro ao carregar correções de ministérios", e);
        }
    }

}