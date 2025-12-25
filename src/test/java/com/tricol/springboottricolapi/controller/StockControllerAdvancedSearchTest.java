package com.tricol.springboottricolapi.controller;

import com.tricol.springboottricolapi.entity.enums.MovementType;
import com.tricol.springboottricolapi.service.StockService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StockController.class)
class StockControllerAdvancedSearchTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StockService stockService;

    @Test
    void testAdvancedSearchEndpoint() throws Exception {
        // Mock service response
        when(stockService.searchMovements(any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        // Test the endpoint with all parameters
        mockMvc.perform(get("/api/v1/stock/mouvements")
                .param("dateDebut", "2025-01-01")
                .param("dateFin", "2025-03-31")
                .param("produitId", "123")
                .param("type", "SORTIE")
                .param("numeroLot", "LOT-2025-001")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk());
    }
}