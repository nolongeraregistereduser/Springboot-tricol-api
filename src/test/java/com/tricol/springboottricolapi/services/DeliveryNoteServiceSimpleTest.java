package com.tricol.springboottricolapi.service;

import com.tricol.springboottricolapi.entity.DeliveryNote;
import com.tricol.springboottricolapi.entity.DeliveryNoteLine;
import com.tricol.springboottricolapi.entity.Product;
import com.tricol.springboottricolapi.entity.enums.ExitOrderStatus;
import com.tricol.springboottricolapi.entity.enums.ExitReason;
import com.tricol.springboottricolapi.exception.BusinessException;
import com.tricol.springboottricolapi.mapper.DeliveryNoteMapper;
import com.tricol.springboottricolapi.repository.DeliveryNoteRepository;
import com.tricol.springboottricolapi.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeliveryNoteServiceSimpleTest {

    @Mock
    private DeliveryNoteRepository deliveryNoteRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private DeliveryNoteMapper mapper;

    @Mock
    private StockService stockService;

    @InjectMocks
    private DeliveryNoteService deliveryNoteService;

    private DeliveryNote testDeliveryNote;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = Product.builder()
                .id(1L)
                .reference("PROD001")
                .name("T-Shirt")
                .currentStock(BigDecimal.valueOf(100))
                .build();

        DeliveryNoteLine line = new DeliveryNoteLine();
        line.setId(1L);
        line.setProduct(testProduct);
        line.setQuantity(BigDecimal.valueOf(50));

        testDeliveryNote = new DeliveryNote();
        testDeliveryNote.setId(1L);
        testDeliveryNote.setNoteNumber("BS-001");
        testDeliveryNote.setExitDate(LocalDate.now());
        testDeliveryNote.setWorkshop("Workshop A");
        testDeliveryNote.setExitReason(ExitReason.PRODUCTION);
        testDeliveryNote.setStatus(ExitOrderStatus.BROUILLON);

        testDeliveryNote.getDeliveryNoteLines().add(line);
    }

    // Task 1.2 - Test 1: Validation triggers stock movements
    @Test
    void test1_ValidationTriggersStockMovements() {
        when(deliveryNoteRepository.findById(1L)).thenReturn(Optional.of(testDeliveryNote));
        when(deliveryNoteRepository.save(any(DeliveryNote.class))).thenReturn(testDeliveryNote);
        doNothing().when(stockService).processStockExit(any(), any(), any(), any());

        deliveryNoteService.validateDeliveryNote(1L);

        verify(stockService, times(1)).processStockExit(
                eq(1L),
                eq(BigDecimal.valueOf(50)),
                eq("BS-001"),
                anyString()
        );

        assertThat(testDeliveryNote.getStatus()).isEqualTo(ExitOrderStatus.VALIDE);

        System.out.println("TEST 1 PASSED : VALIDATION TRIGGERS STOCK MOVEMENTS!!!");
    }

    // Task 1.2 - Test 2: Cannot validate already validated
    @Test
    void test2_CannotValidateAlreadyValidated() {
        testDeliveryNote.setStatus(ExitOrderStatus.VALIDE);
        when(deliveryNoteRepository.findById(1L)).thenReturn(Optional.of(testDeliveryNote));

        assertThatThrownBy(() -> deliveryNoteService.validateDeliveryNote(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("already validated");

        verify(stockService, never()).processStockExit(any(), any(), any(), any());

        System.out.println("TEST 2 PASSED : CANNOT VALIDATE TWICE!!!");
    }

    // Task 1.2 - Test 3: Cannot validate cancelled
    @Test
    void test3_CannotValidateCancelled() {
        testDeliveryNote.setStatus(ExitOrderStatus.ANNULE);
        when(deliveryNoteRepository.findById(1L)).thenReturn(Optional.of(testDeliveryNote));

        assertThatThrownBy(() -> deliveryNoteService.validateDeliveryNote(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("cancelled");

        verify(stockService, never()).processStockExit(any(), any(), any(), any());

        System.out.println("TEST 3 PASSED : CANNOT VALIDATE CANCELLED NOTE!!!");
    }

    // Task 1.2 - Test 4: Validation with multiple products
    @Test
    void test4_ValidationWithMultipleProducts() {
        Product product2 = Product.builder()
                .id(2L)
                .reference("PROD002")
                .name("Pants")
                .currentStock(BigDecimal.valueOf(200))
                .build();

        DeliveryNoteLine line2 = new DeliveryNoteLine();
        line2.setId(2L);
        line2.setProduct(product2);
        line2.setQuantity(BigDecimal.valueOf(30));

        testDeliveryNote.getDeliveryNoteLines().add(line2);

        when(deliveryNoteRepository.findById(1L)).thenReturn(Optional.of(testDeliveryNote));
        when(deliveryNoteRepository.save(any())).thenReturn(testDeliveryNote);
        doNothing().when(stockService).processStockExit(any(), any(), any(), any());

        deliveryNoteService.validateDeliveryNote(1L);

        verify(stockService, times(2)).processStockExit(any(), any(), any(), any());
        verify(stockService).processStockExit(eq(1L), eq(BigDecimal.valueOf(50)), any(), any());
        verify(stockService).processStockExit(eq(2L), eq(BigDecimal.valueOf(30)), any(), any());

        System.out.println("TEST 4 PASSED : MULTIPLE PRODUCTS VALIDATED!!!");
    }
}

