package com.brasil.transparente.processor.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CurrencyNormalizer {

    public static double normalizeCurrencyValue(String valorString) {
        if (valorString == null || valorString.trim().isEmpty()) {
            return 0.0;
        }

        String normalized = valorString.replace("R$", "").trim();
        normalized = normalized.replace("- ", "-");
        normalized = normalized.replace(",", ".");
        
        if (normalized.chars().filter(ch -> ch == '.').count() > 1) {
            int lastDotIndex = normalized.lastIndexOf('.');
            String beforeDecimal = normalized.substring(0, lastDotIndex).replace(".", "");
            String afterDecimal = normalized.substring(lastDotIndex);
            normalized = beforeDecimal + afterDecimal;
        }

        try {
            return Double.parseDouble(normalized);
        } catch (NumberFormatException e) {
            log.warn("Erro ao converter número: {}", valorString);
            return 0.0;
        }
    }
}
