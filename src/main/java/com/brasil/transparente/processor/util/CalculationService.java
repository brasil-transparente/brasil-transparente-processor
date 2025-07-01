package com.brasil.transparente.processor.util;

import org.springframework.stereotype.Component;

@Component
public class CalculationService {

    public double getPercentageOfTotal(double specificExpense, double totalExpense) {
        return (specificExpense / totalExpense) * 100;
    }

}
