package com.brasil.transparente.processor.service.creation;

import com.brasil.transparente.processor.entity.*;
import com.brasil.transparente.processor.util.CalculationService;
import com.brasil.transparente.processor.util.OrdererService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ProcessExpensesServiceTest {

    @Spy
    private OrdererService ordererService = new OrdererService();

    @Spy
    private CalculationService calculationService = new CalculationService();

    @InjectMocks
    private ProcessExpensesService processExpensesService;

    private Poder poder;
    private Ministerio ministerio;
    private Orgao orgao;
    private UnidadeGestora unidadeGestora;
    private ElementoDespesa elementoDespesa;

    @BeforeEach
    void setUp() {
        poder = new Poder("Executivo");
        ministerio = new Ministerio("Min");
        orgao = new Orgao("Org");
        unidadeGestora = new UnidadeGestora("UG");
        elementoDespesa = new ElementoDespesa("ED");

        unidadeGestora.getListElementoDespesa().add(elementoDespesa);
        orgao.getListUnidadeGestora().add(unidadeGestora);
        ministerio.getListOrgao().add(orgao);
        poder.getListMinisterio().add(ministerio);
    }

    @Test
    void aggregateTotalExpense_shouldSumPowersTotalSpending() {
        Poder p1 = new Poder("P1");
        p1.setTotalValueSpent(100.0);
        Poder p2 = new Poder("P2");
        p2.setTotalValueSpent(200.0);

        double total = processExpensesService.aggregateTotalExpense(List.of(p1, p2));

        assertEquals(300.0, total, 0.001);
    }

    @Test
    void aggregateAllPowerSpending_shouldSumMinisteriosSpending() {
        ministerio.setTotalValueSpent(500.0);

        Ministerio ministerio2 = new Ministerio("Min2");
        ministerio2.setTotalValueSpent(300.0);
        poder.getListMinisterio().add(ministerio2);

        processExpensesService.aggregateAllPowerSpending(poder);

        assertEquals(800.0, poder.getTotalValueSpent(), 0.001);
    }

    @Test
    void updateTotalValueSpent_shouldIncrementAllEntitiesValues() {
        processExpensesService.updateTotalValueSpent(ministerio, orgao, unidadeGestora, elementoDespesa, 150.0);

        assertEquals(150.0, ministerio.getTotalValueSpent(), 0.001);
        assertEquals(150.0, orgao.getTotalValueSpent(), 0.001);
        assertEquals(150.0, unidadeGestora.getTotalValueSpent(), 0.001);
        assertEquals(150.0, elementoDespesa.getTotalValueSpent(), 0.001);
    }

    @Test
    void removeNegativeOrZeroExpenses_shouldRemoveInvalidExpenses() {
        elementoDespesa.setTotalValueSpent(0.005);
        ElementoDespesa validDespesa = new ElementoDespesa("Valid");
        validDespesa.setTotalValueSpent(100.0);
        unidadeGestora.getListElementoDespesa().add(validDespesa);

        processExpensesService.removeNegativeOrZeroExpenses(List.of(poder));

        assertEquals(1, unidadeGestora.getListElementoDespesa().size());
        assertEquals("Valid", unidadeGestora.getListElementoDespesa().getFirst().getNameElementoDespesa());
    }

    @Test
    void setTotalPercentages_shouldCalculatePercentagesAcrossHierarchy() {
        poder.setTotalValueSpent(1000.0);
        ministerio.setTotalValueSpent(1000.0);
        orgao.setTotalValueSpent(1000.0);
        unidadeGestora.setTotalValueSpent(1000.0);
        elementoDespesa.setTotalValueSpent(500.0);

        List<Poder> poderList = new ArrayList<>(List.of(poder));
        processExpensesService.setTotalPercentages(poderList, 2000.0);

        assertEquals(50.0, poder.getPercentageOfTotal(), 0.001);
        assertEquals(100.0, ministerio.getPercentageOfTotal(), 0.001);
        assertEquals(100.0, orgao.getPercentageOfTotal(), 0.001);
        assertEquals(100.0, unidadeGestora.getPercentageOfTotal(), 0.001);
        assertEquals(50.0, elementoDespesa.getPercentageOfTotal(), 0.001);
    }
}
