package com.brasil.transparente.processor.util;

import com.brasil.transparente.processor.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrdererService {

    public <T extends UnidadeOrcamentaria> List<T> orderBySpending(List<T> list) {
        list.sort((i1, i2) -> Double.compare(i2.getTotalValueSpent(), i1.getTotalValueSpent()));
        return list;
    }

}
