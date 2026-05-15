package com.brasil.transparente.processor.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyNormalizerTest {

    @Test
    void testNormalizeCurrencyValue_nullInput_returnsZero() {
        double result = CurrencyNormalizer.normalizeCurrencyValue(null);
        assertEquals(0.0, result);
    }

    @Test
    void testNormalizeCurrencyValue_emptyString_returnsZero() {
        double result = CurrencyNormalizer.normalizeCurrencyValue("");
        assertEquals(0.0, result);
    }

    @Test
    void testNormalizeCurrencyValue_whitespaceOnly_returnsZero() {
        double result = CurrencyNormalizer.normalizeCurrencyValue("   ");
        assertEquals(0.0, result);
    }

    @Test
    void testNormalizeCurrencyValue_withRSymbol_removesSymbol() {
        double result = CurrencyNormalizer.normalizeCurrencyValue("R$ 100,00");
        assertEquals(100.0, result);
    }

    @Test
    void testNormalizeCurrencyValue_withRSymbolNoSpace_removesSymbol() {
        double result = CurrencyNormalizer.normalizeCurrencyValue("R$100,00");
        assertEquals(100.0, result);
    }

    @Test
    void testNormalizeCurrencyValue_commaDecimal_convertsToDot() {
        double result = CurrencyNormalizer.normalizeCurrencyValue("100,50");
        assertEquals(100.50, result);
    }

    @Test
    void testNormalizeCurrencyValue_negativeWithSpace_convertsCorrectly() {
        double result = CurrencyNormalizer.normalizeCurrencyValue("- 100,00");
        assertEquals(-100.0, result);
    }

    @Test
    void testNormalizeCurrencyValue_negativeWithoutSpace_convertsCorrectly() {
        double result = CurrencyNormalizer.normalizeCurrencyValue("-100,00");
        assertEquals(-100.0, result);
    }

    @Test
    void testNormalizeCurrencyValue_multipleDots_removesThousandSeparators() {
        double result = CurrencyNormalizer.normalizeCurrencyValue("1.000,50");
        assertEquals(1000.50, result);
    }

    @Test
    void testNormalizeCurrencyValue_multipleDotsLargeNumber_convertsCorrectly() {
        double result = CurrencyNormalizer.normalizeCurrencyValue("10.000.000,50");
        assertEquals(10000000.50, result);
    }

    @Test
    void testNormalizeCurrencyValue_withRSymbolAndDots_convertsCorrectly() {
        double result = CurrencyNormalizer.normalizeCurrencyValue("R$ 1.000,00");
        assertEquals(1000.0, result);
    }

    @Test
    void testNormalizeCurrencyValue_simpleInteger_convertsCorrectly() {
        double result = CurrencyNormalizer.normalizeCurrencyValue("100");
        assertEquals(100.0, result);
    }

    @Test
    void testNormalizeCurrencyValue_simpleDecimal_convertsCorrectly() {
        double result = CurrencyNormalizer.normalizeCurrencyValue("100.50");
        assertEquals(100.50, result);
    }

    @Test
    void testNormalizeCurrencyValue_invalidFormat_returnsZero() {
        double result = CurrencyNormalizer.normalizeCurrencyValue("invalid");
        assertEquals(0.0, result);
    }

    @Test
    void testNormalizeCurrencyValue_mixedInvalid_returnsZero() {
        double result = CurrencyNormalizer.normalizeCurrencyValue("R$ abc");
        assertEquals(0.0, result);
    }

    @Test
    void testNormalizeCurrencyValue_zeroValue_convertsCorrectly() {
        double result = CurrencyNormalizer.normalizeCurrencyValue("0,00");
        assertEquals(0.0, result);
    }

    @Test
    void testNormalizeCurrencyValue_largeValue_convertsCorrectly() {
        double result = CurrencyNormalizer.normalizeCurrencyValue("999999999,99");
        assertEquals(999999999.99, result);
    }

    @Test
    void testNormalizeCurrencyValue_withTrailingSpaces_convertsCorrectly() {
        double result = CurrencyNormalizer.normalizeCurrencyValue("  100,00  ");
        assertEquals(100.0, result);
    }
}
