package com.tricol.springboottricolapi.mapper;

import com.tricol.springboottricolapi.dto.Request.ProductRequestDTO;
import com.tricol.springboottricolapi.dto.Response.ProductResponseDTO;
import com.tricol.springboottricolapi.entity.Product;
import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;

public interface ProductMapper {


    Product toEntity(ProductRequestDTO dto);

    ProductRequestDTO toRequestDTO(Product product);

    ProductResponseDTO toResponseDTO(Product product);

    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    @org.mapstruct.Mapping(target = "id", ignore = true)
    @org.mapstruct.Mapping(target = "createdAt", ignore = true)
    @org.mapstruct.Mapping(target = "updatedAt", ignore = true)


    void updateEntity(ProductRequestDTO dto, @MappingTarget Product product);
}
