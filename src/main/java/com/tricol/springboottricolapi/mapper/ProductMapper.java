package com.tricol.springboottricolapi.mapper;

import com.tricol.springboottricolapi.dto.Request.ProductRequestDTO;
import com.tricol.springboottricolapi.dto.Response.ProductResponseDTO;
import com.tricol.springboottricolapi.entity.Product;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {


    Product toEntity(ProductRequestDTO dto);

    ProductRequestDTO toRequestDTO(Product product);

    ProductResponseDTO toResponseDTO(Product product);

    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "currentStock", ignore = true)
    @Mapping(target = "supplierOrderLines", ignore = true)
    @Mapping(target = "stockBatches", ignore = true)
    @Mapping(target = "stockMovements", ignore = true)
    @Mapping(target = "deliveryNoteLines", ignore = true)


    void updateEntity(ProductRequestDTO dto, @MappingTarget Product product);
}
