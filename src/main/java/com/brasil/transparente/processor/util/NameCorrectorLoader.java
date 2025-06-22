package com.brasil.transparente.processor.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class NameCorrectorLoader {

    private final ResourceLoader resourceLoader;

    public Map<String, String> getCorrectionsHashMap() {
        try {
            var mapper = new ObjectMapper();
            var resource = resourceLoader.getResource("classpath:name-correction.json");
            return mapper.readValue(
                    resource.getContentAsByteArray(),
                    new TypeReference<>() {}
            );
        } catch (IOException e) {
            throw new UncheckedIOException("Erro ao carregar correções de ministérios", e);
        }
    }

}