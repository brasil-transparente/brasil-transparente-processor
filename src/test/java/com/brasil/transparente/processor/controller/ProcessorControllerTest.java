package com.brasil.transparente.processor.controller;

import com.brasil.transparente.processor.service.ProcessorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ProcessorControllerTest {

    @Mock
    private ProcessorService processorService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ProcessorController(processorService)).build();
    }

    @Test
    void processYearAndSaveOnDatabase_shouldInvokeProcessorService() throws Exception {
        mockMvc.perform(post("/processYear").param("year", "2024"))
                .andExpect(status().isOk());

        verify(processorService).generateCompleteReportService("2024");
    }
}
