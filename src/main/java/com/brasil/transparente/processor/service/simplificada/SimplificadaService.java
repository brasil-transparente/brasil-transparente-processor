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
        uniaoService.calculateAndSaveUniao1(totalExpenses);
        uniaoService.calculateAndSaveUniao2(totalExpenses);
        uniaoService.calculateAndSaveUniao3(totalExpenses);
        uniaoService.calculateAndSaveUniao4(totalExpenses);
        uniaoService.calculateAndSaveUniao5(totalExpenses);
        uniaoService.calculateAndSaveUniao6(totalExpenses);
        uniaoService.calculateAndSaveUniao7(totalExpenses);
        uniaoService.calculateAndSaveUniao8(totalExpenses);
        uniaoService.calculateAndSaveUniao9(totalExpenses);
        uniaoService.calculateAndSaveUniaoOutros(totalExpenses);
    }

}
