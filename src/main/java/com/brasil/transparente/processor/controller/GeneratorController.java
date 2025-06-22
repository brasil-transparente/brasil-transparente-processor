package com.brasil.transparente.processor.controller;

import com.brasil.transparente.processor.service.GeneratorOrchestrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GeneratorController {

    private final GeneratorOrchestrationService generatorOrchestrationService;

    @PostMapping("/processYear")
    public void processYearAndSaveOnDatabase(@RequestParam String year) {
        generatorOrchestrationService.generateCompleteReportService(year);
    }
}
