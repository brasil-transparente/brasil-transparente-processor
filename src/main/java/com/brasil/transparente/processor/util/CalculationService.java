package com.brasil.transparente.processor.util;

import org.springframework.stereotype.Component;

@Component
public class CalculationService {

    public double getPercentageOfTotal(double specificExpense, double totalExpense) {
        return totalExpense == 0 ? 0.0 : (specificExpense * 100) / totalExpense;
    }

}
