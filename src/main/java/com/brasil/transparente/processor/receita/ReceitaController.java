package com.brasil.transparente.processor.receita;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReceitaController {

    private final ProcessorReceitaService processorReceitaService;

    @PostMapping("/receitaByAno")
    public void receitaByAno(@RequestParam String ano) {
        processorReceitaService.processReceitaByAno(ano);
    }

}
