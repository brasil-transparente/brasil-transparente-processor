package com.brasil.transparente.processor.util;

import com.brasil.transparente.processor.entity.Poder;
import com.brasil.transparente.processor.util.constants.Constants;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PoderResolverService {

    public Poder definePoder(String poder, List<Poder> poderList) {
        if (poder == null) {
            return poderList.get(3);
        }
        String input = poder.trim().toUpperCase();
        if (Constants.EXECUTIVO.stream().anyMatch(v -> input.equalsIgnoreCase(v.trim().toUpperCase()))) {
            return poderList.getFirst();
        } else if (Constants.LEGISLATIVO.stream().anyMatch(v -> input.equalsIgnoreCase(v.trim().toUpperCase()))) {
            return poderList.get(1);
        } else if (Constants.JUDICIARIO.stream().anyMatch(v -> input.equalsIgnoreCase(v.trim().toUpperCase()))) {
            return poderList.get(2);
        }
        return poderList.get(3);
    }

}
