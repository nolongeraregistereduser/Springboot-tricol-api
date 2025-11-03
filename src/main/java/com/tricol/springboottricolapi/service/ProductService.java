package com.tricol.springboottricolapi.service;

import com.tricol.springboottricolapi.dto.Request.ProductRequestDTO;
import com.tricol.springboottricolapi.dto.Response.ProductResponseDTO;
import com.tricol.springboottricolapi.dto.Response.ProductStockDTO;
import com.tricol.springboottricolapi.entity.Product;
import com.tricol.springboottricolapi.exception.DuplicateRessourceException;
import com.tricol.springboottricolapi.exception.ResourceNotFoundException;
import com.tricol.springboottricolapi.mapper.ProductMapper;
import com.tricol.springboottricolapi.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toResponseDTO)
                .toList();
    }

    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return productMapper.toResponseDTO(product);
    }

    public ProductResponseDTO createProduct(ProductRequestDTO requestDTO) {
        // Check if product with same reference already exists
        if (productRepository.existsByReference(requestDTO.getReference())) {
            throw new DuplicateRessourceException("Product", "reference", requestDTO.getReference());
        }

        Product product = productMapper.toEntity(requestDTO);
        Product savedProduct = productRepository.save(product);
        return productMapper.toResponseDTO(savedProduct);
    }

    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO requestDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        // Check if reference is being changed to an existing reference
        if (!existingProduct.getReference().equals(requestDTO.getReference())
                && productRepository.existsByReference(requestDTO.getReference())) {
            throw new DuplicateRessourceException("Product", "reference", requestDTO.getReference());
        }

        productMapper.updateEntity(requestDTO, existingProduct);
        Product updatedProduct = productRepository.save(existingProduct);
        return productMapper.toResponseDTO(updatedProduct);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        productRepository.delete(product);
    }

    public ProductStockDTO getProductStock(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        return ProductStockDTO.builder()
                .productId(product.getId())
                .reference(product.getReference())
                .name(product.getName())
                .currentStock(product.getCurrentStock())
                .reorderPoint(product.getReorderPoint())
                .unitOfMeasure(product.getUnitOfMeasure())
                .isBelowReorderPoint(product.isBelowReorderPoint())
                .build();
    }
}
