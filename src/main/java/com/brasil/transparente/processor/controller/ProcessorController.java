package com.brasil.transparente.processor.controller;

import com.brasil.transparente.processor.service.ProcessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProcessorController {

    private final ProcessorService processorService;

    @PostMapping("/processYear")
    public void processYearAndSaveOnDatabase(@RequestParam String year) {
        processorService.generateCompleteReportService(year);
    }
}
