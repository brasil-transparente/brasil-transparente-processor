package com.brasil.transparente.processor.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SmokeTestControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new SmokeTestController()).build();
    }

    @Test
    void smokeTest_shouldReturnSmokeSuccessful() throws Exception {
        mockMvc.perform(get("/smoke"))
                .andExpect(status().isOk())
                .andExpect(content().string("[SMOKE SUCCESSFUL]"));
    }
}
