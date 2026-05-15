package com.brasil.transparente.processor.util;

import com.brasil.transparente.processor.util.constants.Currency2025Constants;
import com.brasil.transparente.processor.util.constants.EmbaixadasConstants;
import org.springframework.stereotype.Component;

@Component
public class CurrencyConverterService {

    private static String ENDING_USD = "- USD";
    private static String ENDING_MOEDA_LOCAL = "- MOEDA LOCAL";
    private static String ENDING_ARS = "- ARS";
    private static String ENDING_EURO = "- EURO";

    public double convertCurrency(String unidadeGestora, double valorPago) {
        if (unidadeGestora.contains(ENDING_USD)) {
            return valorPago * Currency2025Constants.USD;
        } else if (unidadeGestora.contains(ENDING_ARS)) {
            return valorPago * Currency2025Constants.PESO_ARGENTINO;
        } else if (unidadeGestora.contains(ENDING_EURO)) {
            return valorPago * Currency2025Constants.EURO;
        } else if (unidadeGestora.contains(ENDING_MOEDA_LOCAL)) {
            return switch (unidadeGestora) {
                case EmbaixadasConstants.EMBAIXADA_CHINA_PEQUIM_LOCAL, EmbaixadasConstants.EMBAIXADA_CHINA_CANTAO_LOCAL,
                     EmbaixadasConstants.EMBAIXADA_CHINA_XANGAI_LOCAL -> valorPago * Currency2025Constants.YUAN;
                case EmbaixadasConstants.EMBAIXADA_BOLIVIA_LOCAL -> valorPago * Currency2025Constants.BOLIVIANO;
                case EmbaixadasConstants.EMBAIXADA_TAILANDIA_LOCAL -> valorPago * Currency2025Constants.BAHT;
                case EmbaixadasConstants.EMBAIXADA_POLONIA_LOCAL -> valorPago * Currency2025Constants.ZLOTY;
                case EmbaixadasConstants.EMBAIXADA_NIGERIA_LOCAL -> valorPago * Currency2025Constants.NAIRA;
                case EmbaixadasConstants.EMBAIXADA_BEIRUTE_LOCAL -> valorPago * Currency2025Constants.LIBRA_LIBANESA;
                default -> valorPago;
            };
        }
        return valorPago;
    }

}
