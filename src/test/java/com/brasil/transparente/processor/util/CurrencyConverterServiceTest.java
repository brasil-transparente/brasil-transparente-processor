package com.brasil.transparente.processor.util;

import com.brasil.transparente.processor.util.constants.Currency2025Constants;
import com.brasil.transparente.processor.util.constants.EmbaixadasConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CurrencyConverterService.class})
class CurrencyConverterServiceTest {

    @Autowired
    private CurrencyConverterService currencyConverterService;

    private double expected(double value, double rate) {
        return value * rate;
    }

    @Test
    void testConvertCurrency_usdConversion_appliesRate() {
        double result = currencyConverterService.convertCurrency("EMBAIXADA DO BRASIL EM PEQUIM - USD", 100.0);
        assertEquals(expected(100.0, Currency2025Constants.USD), result, 0.01);
    }

    @Test
    void testConvertCurrency_chinaPequimLocal_appliesYuanRate() {
        double result = currencyConverterService.convertCurrency(EmbaixadasConstants.EMBAIXADA_CHINA_PEQUIM_LOCAL, 100.0);
        assertEquals(expected(100.0, Currency2025Constants.YUAN), result, 0.01);
    }

    @Test
    void testConvertCurrency_chinaCantaoLocal_appliesYuanRate() {
        double result = currencyConverterService.convertCurrency(EmbaixadasConstants.EMBAIXADA_CHINA_CANTAO_LOCAL, 100.0);
        assertEquals(expected(100.0, Currency2025Constants.YUAN), result, 0.01);
    }

    @Test
    void testConvertCurrency_chinaXangaiLocal_appliesYuanRate() {
        double result = currencyConverterService.convertCurrency(EmbaixadasConstants.EMBAIXADA_CHINA_XANGAI_LOCAL, 100.0);
        assertEquals(expected(100.0, Currency2025Constants.YUAN), result, 0.01);
    }

    @Test
    void testConvertCurrency_boliviaLocal_appliesBolivianoRate() {
        double result = currencyConverterService.convertCurrency(EmbaixadasConstants.EMBAIXADA_BOLIVIA_LOCAL, 100.0);
        assertEquals(expected(100.0, Currency2025Constants.BOLIVIANO), result, 0.01);
    }

    @Test
    void testConvertCurrency_thailandLocal_appliesBahtRate() {
        double result = currencyConverterService.convertCurrency(EmbaixadasConstants.EMBAIXADA_TAILANDIA_LOCAL, 100.0);
        assertEquals(expected(100.0, Currency2025Constants.BAHT), result, 0.01);
    }

    @Test
    void testConvertCurrency_poloniaLocal_appliesZlotyRate() {
        double result = currencyConverterService.convertCurrency(EmbaixadasConstants.EMBAIXADA_POLONIA_LOCAL, 100.0);
        assertEquals(expected(100.0, Currency2025Constants.ZLOTY), result, 0.01);
    }

    @Test
    void testConvertCurrency_nigeriaLocal_appliesNairaRate() {
        double result = currencyConverterService.convertCurrency(EmbaixadasConstants.EMBAIXADA_NIGERIA_LOCAL, 100.0);
        assertEquals(expected(100.0, Currency2025Constants.NAIRA), result, 0.01);
    }

    @Test
    void testConvertCurrency_beiruteLocal_appliesLibraLibanesaRate() {
        double result = currencyConverterService.convertCurrency(EmbaixadasConstants.EMBAIXADA_BEIRUTE_LOCAL, 100.0);
        assertEquals(expected(100.0, Currency2025Constants.LIBRA_LIBANESA), result, 0.0001);
    }

    @Test
    void testConvertCurrency_pasoLosLibresLocal_appliesPesoArgentinoRate() {
        double result = currencyConverterService.convertCurrency(EmbaixadasConstants.EMBAIXADA_PASO_LOS_LIBRES_LOCAL, 100.0);
        assertEquals(expected(100.0, Currency2025Constants.PESO_ARGENTINO), result, 0.001);
    }

    @Test
    void testConvertCurrency_genebraEuro_appliesEuroRate() {
        double result = currencyConverterService.convertCurrency(EmbaixadasConstants.EMBAIXADA_GENEBRA_EURO, 100.0);
        assertEquals(expected(100.0, Currency2025Constants.EURO), result, 0.01);
    }

    @Test
    void testConvertCurrency_unknownLocalCurrency_returnsOriginal() {
        double result = currencyConverterService.convertCurrency("UNKNOWN EMBASSY - MOEDA LOCAL", 100.0);
        assertEquals(100.0, result, 0.01);
    }

    @Test
    void testConvertCurrency_noCurrencyIndicator_returnsOriginal() {
        double result = currencyConverterService.convertCurrency("EMBAIXADA DO BRASIL EM PEQUIM", 100.0);
        assertEquals(100.0, result, 0.01);
    }

    @Test
    void testConvertCurrency_zeroValue_convertsCorrectly() {
        double result = currencyConverterService.convertCurrency("EMBAIXADA DO BRASIL EM PEQUIM - USD", 0.0);
        assertEquals(0.0, result, 0.01);
    }

    @Test
    void testConvertCurrency_negativeValue_convertsCorrectly() {
        double result = currencyConverterService.convertCurrency("EMBAIXADA DO BRASIL EM PEQUIM - USD", -100.0);
        assertEquals(expected(-100.0, Currency2025Constants.USD), result, 0.01);
    }

    @Test
    void testConvertCurrency_largeValue_convertsCorrectly() {
        double result = currencyConverterService.convertCurrency("EMBAIXADA DO BRASIL EM PEQUIM - USD", 1000000.0);
        assertEquals(expected(1000000.0, Currency2025Constants.USD), result, 0.01);
    }
}
