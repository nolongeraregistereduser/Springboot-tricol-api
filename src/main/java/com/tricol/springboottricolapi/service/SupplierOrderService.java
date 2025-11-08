package com.tricol.springboottricolapi.service;

import com.tricol.springboottricolapi.dto.Request.SupplierOrderLineRequestDTO;
import com.tricol.springboottricolapi.dto.Request.SupplierOrderRequestDTO;
import com.tricol.springboottricolapi.dto.Response.SupplierOrderResponseDTO;
import com.tricol.springboottricolapi.entity.Product;
import com.tricol.springboottricolapi.entity.Supplier;
import com.tricol.springboottricolapi.entity.SupplierOrder;
import com.tricol.springboottricolapi.entity.SupplierOrderLine;
import com.tricol.springboottricolapi.entity.enums.OrderStatus;
import com.tricol.springboottricolapi.exception.ResourceNotFoundException;
import com.tricol.springboottricolapi.mapper.SupplierOrderMapper;
import com.tricol.springboottricolapi.repository.ProductRepository;
import com.tricol.springboottricolapi.repository.SupplierOrderRepository;
import com.tricol.springboottricolapi.repository.SupplierRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SupplierOrderService {

    private final SupplierOrderRepository orderRepository;
    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;
    private final SupplierOrderMapper orderMapper;
    private final StockService stockService;

    public SupplierOrderService(SupplierOrderRepository orderRepository,
                                SupplierRepository supplierRepository,
                                ProductRepository productRepository,
                                SupplierOrderMapper orderMapper,
                                StockService stockService) {
        this.orderRepository = orderRepository;
        this.supplierRepository = supplierRepository;
        this.productRepository = productRepository;
        this.orderMapper = orderMapper;
        this.stockService = stockService;
    }

    public List<SupplierOrderResponseDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public SupplierOrderResponseDTO getOrderById(Long id) {
        SupplierOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return orderMapper.toResponseDTO(order);
    }

    public List<SupplierOrderResponseDTO> getOrdersBySupplierId(Long supplierId) {
        supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + supplierId));

        return orderRepository.findBySupplierId(supplierId)
                .stream()
                .map(orderMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<SupplierOrderResponseDTO> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status)
                .stream()
                .map(orderMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<SupplierOrderResponseDTO> getOrdersByDateRange(LocalDate startDate, LocalDate endDate) {
        return orderRepository.findByOrderDateBetween(startDate, endDate)
                .stream()
                .map(orderMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public SupplierOrderResponseDTO createOrder(SupplierOrderRequestDTO requestDTO) {
        Supplier supplier = supplierRepository.findById(requestDTO.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + requestDTO.getSupplierId()));

        SupplierOrder order = SupplierOrder.builder()
                .orderNumber(generateOrderNumber())
                .supplier(supplier)
                .orderDate(LocalDate.now())
                .status(OrderStatus.EN_ATTENTE)
                .comments(requestDTO.getNotes())
                .build();

        for (SupplierOrderLineRequestDTO lineDTO : requestDTO.getOrderLines()) {
            Product product = productRepository.findById(lineDTO.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + lineDTO.getProductId()));

            SupplierOrderLine line = SupplierOrderLine.builder()
                    .product(product)
                    .quantity(lineDTO.getQuantityOrdered())
                    .unitPurchasePrice(lineDTO.getUnitPurchasePrice())
                    .build();

            order.addOrderLine(line);
        }

        order.calculateTotalAmount();

        SupplierOrder savedOrder = orderRepository.save(order);
        return orderMapper.toResponseDTO(savedOrder);
    }

    public SupplierOrderResponseDTO updateOrder(Long id, SupplierOrderRequestDTO requestDTO) {
        SupplierOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        if (order.getStatus() != OrderStatus.EN_ATTENTE) {
            throw new IllegalStateException("Cannot update order with status: " + order.getStatus());
        }

        if (!order.getSupplier().getId().equals(requestDTO.getSupplierId())) {
            Supplier supplier = supplierRepository.findById(requestDTO.getSupplierId())
                    .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + requestDTO.getSupplierId()));
            order.setSupplier(supplier);
        }

        order.setComments(requestDTO.getNotes());
        order.getOrderLines().clear();

        for (SupplierOrderLineRequestDTO lineDTO : requestDTO.getOrderLines()) {
            Product product = productRepository.findById(lineDTO.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + lineDTO.getProductId()));

            SupplierOrderLine line = SupplierOrderLine.builder()
                    .product(product)
                    .quantity(lineDTO.getQuantityOrdered())
                    .unitPurchasePrice(lineDTO.getUnitPurchasePrice())
                    .build();

            order.addOrderLine(line);
        }

        order.calculateTotalAmount();

        SupplierOrder updatedOrder = orderRepository.save(order);
        return orderMapper.toResponseDTO(updatedOrder);
    }

    public void deleteOrder(Long id) {
        SupplierOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        if (order.getStatus() == OrderStatus.LIVREE || order.getStatus() == OrderStatus.VALIDEE) {
            throw new IllegalStateException("Cannot delete order with status: " + order.getStatus());
        }

        orderRepository.delete(order);
    }

    public SupplierOrderResponseDTO cancelOrder(Long id) {
        SupplierOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        if (order.getStatus() == OrderStatus.LIVREE) {
            throw new IllegalStateException("Cannot cancel a delivered order");
        }

        order.setStatus(OrderStatus.ANNULEE);
        SupplierOrder cancelledOrder = orderRepository.save(order);
        return orderMapper.toResponseDTO(cancelledOrder);
    }

    public SupplierOrderResponseDTO validateOrder(Long id) {
        SupplierOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        if (order.getStatus() != OrderStatus.EN_ATTENTE) {
            throw new IllegalStateException("Only orders with status EN_ATTENTE can be validated");
        }

        order.setStatus(OrderStatus.VALIDEE);
        SupplierOrder validatedOrder = orderRepository.save(order);
        return orderMapper.toResponseDTO(validatedOrder);
    }

    public SupplierOrderResponseDTO receiveOrder(Long id) {
        SupplierOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        if (order.getStatus() != OrderStatus.VALIDEE) {
            throw new IllegalStateException("Only validated orders can be received");
        }

        order.setStatus(OrderStatus.LIVREE);
        order.setReceptionDate(LocalDate.now());

        SupplierOrder receivedOrder = orderRepository.save(order);

        // Create stock entries using FIFO logic
        stockService.createStockEntryFromOrder(receivedOrder);

        return orderMapper.toResponseDTO(receivedOrder);
    }

    private String generateOrderNumber() {
        String prefix = "CMD-";
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long count = orderRepository.count() + 1;
        return String.format("%s%s-%04d", prefix, date, count);
    }
}
