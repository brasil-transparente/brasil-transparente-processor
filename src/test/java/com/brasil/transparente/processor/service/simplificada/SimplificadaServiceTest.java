package com.brasil.transparente.processor.service.simplificada;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SimplificadaServiceTest {

    @Mock
    private UniaoSimplificadaService uniaoService;

    @InjectMocks
    private SimplificadaService simplificadaService;

    @Test
    void generateSimplifiedReportUniao_shouldCallAllReportCalculations() {
        double totalExpenses = 1000.0;

        simplificadaService.generateSimplifiedReportUniao(totalExpenses);

        verify(uniaoService).calculateAndSaveAposentadoriasEPensoes(totalExpenses);
        verify(uniaoService).calculateAndSaveJurosDividaPublica(totalExpenses);
        verify(uniaoService).calculateAndSaveSaude(totalExpenses);
        verify(uniaoService).calculateAndSaveTransferenciaRenda(totalExpenses);
        verify(uniaoService).calculateAndSaveEducacao(totalExpenses);
        verify(uniaoService).calculateAndSavePrecatoriosERpvs(totalExpenses);
        verify(uniaoService).calculateAndSaveDefesa(totalExpenses);
        verify(uniaoService).calculateAndSaveSeguroDesempregoEAbono(totalExpenses);
        verify(uniaoService).calculateAndSavePoderJudiciarioFederal(totalExpenses);
        verify(uniaoService).calculateAndSaveOutros(totalExpenses);
    }
}
