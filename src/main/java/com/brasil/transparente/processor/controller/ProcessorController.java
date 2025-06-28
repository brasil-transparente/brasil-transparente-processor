package com.brasil.transparente.processor.controller;

import com.brasil.transparente.processor.service.ProcessorOrchestrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProcessorController {

    private final ProcessorOrchestrationService processorOrchestrationService;

    @PostMapping("/processYear")
    public void processYearAndSaveOnDatabase(@RequestParam String year) {
        processorOrchestrationService.generateCompleteReportService(year);
    }
}
