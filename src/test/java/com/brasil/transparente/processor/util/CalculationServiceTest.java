package com.brasil.transparente.processor.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CalculationService.class})
class CalculationServiceTest {

    @Autowired
    private CalculationService calculationService;

    @Test
    void testGetPercentageOfTotal_basicCalculation_returnsCorrectPercentage() {
        double result = calculationService.getPercentageOfTotal(25.0, 100.0);
        assertEquals(25.0, result, 0.001);
    }

    @Test
    void testGetPercentageOfTotal_half_returnsFifty() {
        double result = calculationService.getPercentageOfTotal(50.0, 100.0);
        assertEquals(50.0, result, 0.001);
    }

    @Test
    void testGetPercentageOfTotal_quarter_returnsTwentyFive() {
        double result = calculationService.getPercentageOfTotal(25.0, 100.0);
        assertEquals(25.0, result, 0.001);
    }

    @Test
    void testGetPercentageOfTotal_smallValue_returnsSmallPercentage() {
        double result = calculationService.getPercentageOfTotal(1.0, 1000.0);
        assertEquals(0.1, result, 0.001);
    }

    @Test
    void testGetPercentageOfTotal_largeValue_returnsLargePercentage() {
        double result = calculationService.getPercentageOfTotal(750.0, 1000.0);
        assertEquals(75.0, result, 0.001);
    }

    @Test
    void testGetPercentageOfTotal_zeroSpecificExpense_returnsZero() {
        double result = calculationService.getPercentageOfTotal(0.0, 100.0);
        assertEquals(0.0, result, 0.001);
    }

    @Test
    void testGetPercentageOfTotal_equalValues_returnsHundred() {
        double result = calculationService.getPercentageOfTotal(100.0, 100.0);
        assertEquals(100.0, result, 0.001);
    }

    @Test
    void testGetPercentageOfTotal_decimalValues_returnsCorrectPercentage() {
        double result = calculationService.getPercentageOfTotal(33.33, 100.0);
        assertEquals(33.33, result, 0.001);
    }

    @Test
    void testGetPercentageOfTotal_fractionalPercentage_returnsCorrectValue() {
        double result = calculationService.getPercentageOfTotal(1.5, 100.0);
        assertEquals(1.5, result, 0.001);
    }

    @Test
    void testGetPercentageOfTotal_veryLargeNumbers_returnsCorrectPercentage() {
        double result = calculationService.getPercentageOfTotal(1500000.0, 3000000.0);
        assertEquals(50.0, result, 0.001);
    }

    @Test
    void testGetPercentageOfTotal_negativeSpecificExpense_returnsNegativePercentage() {
        double result = calculationService.getPercentageOfTotal(-50.0, 100.0);
        assertEquals(-50.0, result, 0.001);
    }

    @Test
    void testGetPercentageOfTotal_negativeTotalExpense_returnsNegativePercentage() {
        double result = calculationService.getPercentageOfTotal(50.0, -100.0);
        assertEquals(-50.0, result, 0.001);
    }

    @Test
    void testGetPercentageOfTotal_bothNegative_returnsPositivePercentage() {
        double result = calculationService.getPercentageOfTotal(-50.0, -100.0);
        assertEquals(50.0, result, 0.001);
    }
}
