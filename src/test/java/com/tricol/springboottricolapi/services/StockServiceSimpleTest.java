package com.tricol.springboottricolapi.service;

import com.tricol.springboottricolapi.entity.Product;
import com.tricol.springboottricolapi.entity.StockBatch;
import com.tricol.springboottricolapi.entity.SupplierOrder;
import com.tricol.springboottricolapi.entity.SupplierOrderLine;
import com.tricol.springboottricolapi.repository.ProductRepository;
import com.tricol.springboottricolapi.repository.StockBatchRepository;
import com.tricol.springboottricolapi.repository.StockMovementRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StockServiceSimpleTest {

    @Mock
    private StockBatchRepository stockBatchRepository;

    @Mock
    private StockMovementRepository stockMovementRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private StockService stockService;

    private Product testProduct;

    @BeforeEach
    void setUp(){
        testProduct = Product.builder()
                .id(1L)
                .reference("PROD001")
                .name("T-Shirt")
                .currentStock(BigDecimal.valueOf(100))
                .reorderPoint(BigDecimal.valueOf(10))
                .unitOfMeasure("PIECE")
                .build();
    }

    //Scenario 1: Partial consumption of single batch
    @Test
    void test1_SimpleFifo_PartialConsumption(){

        StockBatch batch = new StockBatch();
        batch.setId(1L);
        batch.setBatchNumber("LOT-001");
        batch.setProduct(testProduct);
        batch.setRemainingQuantity(BigDecimal.valueOf(100));
        batch.setUnitPurchasePrice(BigDecimal.valueOf(10.0));

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(stockBatchRepository.findAvailableBatchesByProductIdOrderedByFifo(1L))
                .thenReturn(List.of(batch));
        when(stockBatchRepository.save(any(StockBatch.class))).thenReturn(batch);

        stockService.processStockExit(
                1l,
                BigDecimal.valueOf(30),
                "REF-001",
                "Test exit"
        );

        assertThat(batch.getRemainingQuantity())
                .isEqualByComparingTo(BigDecimal.valueOf(70));

        verify(stockBatchRepository, times(1)).save(any(StockBatch.class));
        System.out.println("TEST 1 PASSED : TOOK 30 UNITS, 70 REMAIN!!!");
    }



    //Scenario 2: Multiple batches consumption
    @Test
    void test2_Fifo_MultipleConsumption(){

        StockBatch batch1 = new StockBatch();
        batch1.setId(1L);
        batch1.setBatchNumber("LOT-001");
        batch1.setProduct(testProduct);
        batch1.setInitialQuantity(BigDecimal.valueOf(100));
        batch1.setRemainingQuantity(BigDecimal.valueOf(100));
        batch1.setUnitPurchasePrice(BigDecimal.valueOf(10.0));
        batch1.setEntryDate(LocalDateTime.now().minusDays(3));

        StockBatch batch2 = new StockBatch();
        batch2.setId(2L);
        batch2.setBatchNumber("LOT-002");
        batch2.setProduct(testProduct);
        batch2.setInitialQuantity(BigDecimal.valueOf(100));
        batch2.setRemainingQuantity(BigDecimal.valueOf(100));
        batch2.setUnitPurchasePrice(BigDecimal.valueOf(12.0));
        batch2.setEntryDate(LocalDateTime.now().minusDays(2));

        StockBatch batch3 = new StockBatch();
        batch3.setId(3L);
        batch3.setBatchNumber("LOT-003");
        batch3.setProduct(testProduct);
        batch3.setInitialQuantity(BigDecimal.valueOf(100));
        batch3.setRemainingQuantity(BigDecimal.valueOf(100));
        batch3.setUnitPurchasePrice(BigDecimal.valueOf(15.0));
        batch3.setEntryDate(LocalDateTime.now().minusDays(1));

        testProduct.setCurrentStock(BigDecimal.valueOf(300));

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(stockBatchRepository.findAvailableBatchesByProductIdOrderedByFifo(1L))
                .thenReturn(List.of(batch1,batch2,batch3));

        when(stockBatchRepository.save(any(StockBatch.class)))
                .thenAnswer(i->i.getArgument(0));
        when(stockMovementRepository.save(any()))
                .thenReturn(null);
        when(productRepository.save(any(Product.class)))
                .thenReturn(testProduct);


        stockService.processStockExit(1L,BigDecimal.valueOf(250),"REF-002","Big exit");
        assertThat(batch1.getRemainingQuantity()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(batch2.getRemainingQuantity()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(batch3.getRemainingQuantity()).isEqualByComparingTo(BigDecimal.valueOf(50));

        System.out.println("TEST 2 PASSED : FIFO CONSUMED OLDEST BATCH 1ST!!!");

    }


    //Scenario 3: Insufficient stock error
    @Test
    void test3_InsufficientStock_ThrowsError(){
        testProduct.setCurrentStock(BigDecimal.valueOf(50));
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        assertThatThrownBy(()-> {
            stockService.processStockExit(1L,BigDecimal.valueOf(100),"REF-003","Too much!");
        })
                .isInstanceOf(Exception.class)
                .hasMessageContaining("T-Shirt");

        System.out.println("TEST 3 PASSED : ERROR THROWN WHEN NOT ENOUGH STOCK!!!");

    }

    //Scenario 4: Exact stock exhaustion
    @Test
    void test6_Fifo_ExactExhaustion(){
        StockBatch batch = new StockBatch();
        batch.setId(1L);
        batch.setBatchNumber("LOT-001");
        batch.setProduct(testProduct);
        batch.setInitialQuantity(BigDecimal.valueOf(100));
        batch.setRemainingQuantity(BigDecimal.valueOf(100));
        batch.setUnitPurchasePrice(BigDecimal.valueOf(10.0));
        batch.setEntryDate(LocalDateTime.now());

        testProduct.setCurrentStock(BigDecimal.valueOf(100));

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(stockBatchRepository.findAvailableBatchesByProductIdOrderedByFifo(1L))
                .thenReturn(List.of(batch));
        when(stockBatchRepository.save(any(StockBatch.class)))
                .thenAnswer(i -> i.getArgument(0));
        when(stockMovementRepository.save(any())).thenReturn(null);
        when(productRepository.save(any())).thenReturn(testProduct);

        stockService.processStockExit(1L, BigDecimal.valueOf(100), "REF-006", "Exact exhaustion");

        assertThat(batch.getRemainingQuantity()).isEqualByComparingTo(BigDecimal.ZERO);

        System.out.println("TEST 6 PASSED : STOCK EXHAUSTED EXACTLY!!!");
    }


    // Task 1.1.B
    @Test
    void test4_CreateBatch_FromSupplierOrder(){
        SupplierOrder order = SupplierOrder.builder()
                .id(1L)
                .orderNumber("CMD-001")
                .orderLines(new ArrayList<>())
                .build();

        SupplierOrderLine orderLine = SupplierOrderLine.builder()
                .product(testProduct)
                .quantity(BigDecimal.valueOf(50))
                .unitPurchasePrice(BigDecimal.valueOf(20.0))
                .build();

        order.getOrderLines().add(orderLine);

        when(stockBatchRepository.save(any(StockBatch.class)))
                .thenAnswer(i->i.getArgument(0));
        when(stockMovementRepository.save(any())).thenReturn(null);
        when(stockBatchRepository.findAvailableBatchesByProductIdOrderedByFifo(any()))
                .thenReturn(new ArrayList<>());
        when(productRepository.save(any())).thenReturn(testProduct);

        stockService.createStockEntryFromOrder(order);
        verify(stockBatchRepository,times(1)).save(any(StockBatch.class));
        verify(stockMovementRepository,times(1)).save(any());

        System.out.println("TEST 4 PASSED : BATCH CREATED FROM SUPPLIER ORDER!!!");
    }


    //1.1.C
    @Test
    void test5_StockValuation_CalculatesCorrectly(){
        StockBatch batch1 = new StockBatch();
        batch1.setRemainingQuantity(BigDecimal.valueOf(50));
        batch1.setUnitPurchasePrice(BigDecimal.valueOf(10));

        StockBatch batch2 = new StockBatch();
        batch2.setRemainingQuantity(BigDecimal.valueOf(75));
        batch2.setUnitPurchasePrice(BigDecimal.valueOf(12));

        when(stockBatchRepository.findAll()).thenReturn(List.of(batch1,batch2));
        when(productRepository.count()).thenReturn(1L);

        var valuation = stockService.getStockValuation();

        assertThat(valuation.getTotalStockValue())
                .isEqualByComparingTo(BigDecimal.valueOf(1400));

        assertThat(valuation.getTotalBatches()).isEqualTo(2);

        System.out.println("TEST 5 PASSED : STOCK VALUATION IS CORRECT !!!");
    }

}
