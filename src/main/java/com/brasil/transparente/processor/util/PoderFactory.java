package com.brasil.transparente.processor.util;

import com.brasil.transparente.processor.entity.Poder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PoderFactory {

    private PoderFactory() {
        throw new UnsupportedOperationException("Classe não deve ser instanciada");
    }

    public static List<Poder> criarListaPoderes() {
        return new ArrayList<>(Arrays.asList(
                new Poder("Poder Executivo"),
                new Poder("Poder Legislativo"),
                new Poder("Poder Judiciário"),
                new Poder("Órgãos Autônomos")
        ));
    }

}
