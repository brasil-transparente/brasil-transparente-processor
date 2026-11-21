package com.brasil.transparente.processor.service.simplificada;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimplificadaService {

    private final UniaoSimplificadaService uniaoService;

    public void generateSimplifiedReportUniao(double totalExpenses) {
        log.info("Gerando estrutura simplificada - União");
        uniaoService.calculateAndSaveAposentadoriasEPensoes(totalExpenses);
        uniaoService.calculateAndSaveJurosDividaPublica(totalExpenses);
        uniaoService.calculateAndSaveSaude(totalExpenses);
        uniaoService.calculateAndSaveTransferenciaRenda(totalExpenses);
        uniaoService.calculateAndSaveEducacao(totalExpenses);
        uniaoService.calculateAndSavePrecatoriosERpvs(totalExpenses);
        uniaoService.calculateAndSaveDefesa(totalExpenses);
        uniaoService.calculateAndSaveSeguroDesempregoEAbono(totalExpenses);
        uniaoService.calculateAndSavePoderJudiciarioFederal(totalExpenses);
        uniaoService.calculateAndSaveOutros(totalExpenses);
    }

}
