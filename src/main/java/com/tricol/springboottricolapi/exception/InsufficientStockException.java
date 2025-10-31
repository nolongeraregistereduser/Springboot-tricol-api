package com.tricol.springboottricolapi.exception;

public class InsufficientStockException extends BusinessException {
    public InsufficientStockException(String productName, int availableStock, int requestedQuantity) {
        super(String.format("Insufficient stock for product '%s': available %d, requested %d.",
                productName, availableStock, requestedQuantity));
    }
}
