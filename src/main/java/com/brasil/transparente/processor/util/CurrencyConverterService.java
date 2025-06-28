package com.brasil.transparente.processor.util;

import com.brasil.transparente.processor.util.constants.Currency2024Constants;
import com.brasil.transparente.processor.util.constants.EmbaixadasConstants;
import org.springframework.stereotype.Component;

@Component
public class CurrencyConverterService {

    public double convertCurrency(String unidadeGestora, double valorPago) {
        if (unidadeGestora.contains("- USD")) {
            return valorPago * Currency2024Constants.USD;
        } else if (unidadeGestora.contains("- MOEDA LOCAL")) {
            return switch (unidadeGestora) {
                case EmbaixadasConstants.EMBAIXADA_CHINA_PEQUIM_LOCAL, EmbaixadasConstants.EMBAIXADA_CHINA_CANTAO_LOCAL,
                     EmbaixadasConstants.EMBAIXADA_CHINA_XANGAI_LOCAL -> valorPago * Currency2024Constants.YUAN;
                case EmbaixadasConstants.EMBAIXADA_BOLIVIA_LOCAL -> valorPago * Currency2024Constants.BOLIVIANO;
                case EmbaixadasConstants.EMBAIXADA_TAILANDIA_LOCAL -> valorPago * Currency2024Constants.BAHT;
                case EmbaixadasConstants.EMBAIXADA_POLONIA_LOCAL -> valorPago * Currency2024Constants.ZLOTY;
                case EmbaixadasConstants.EMBAIXADA_NIGERIA_LOCAL -> valorPago * Currency2024Constants.NAIRA;
                case EmbaixadasConstants.EMBAIXADA_BEIRUTE_LOCAL -> valorPago * Currency2024Constants.LIBRA_LIBANESA;
                case EmbaixadasConstants.EMBAIXADA_PASO_LOS_LIBRES_LOCAL -> valorPago * Currency2024Constants.PESO_ARGENTINO;
                case EmbaixadasConstants.EMBAIXADA_GENEBRA_EURO -> valorPago * Currency2024Constants.EURO;
                default -> valorPago;
            };
        }
        return valorPago;
    }

}
