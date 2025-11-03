package com.tricol.springboottricolapi.mapper;

import com.tricol.springboottricolapi.dto.Request.SupplierOrderRequestDTO;
import com.tricol.springboottricolapi.dto.Response.SupplierOrderLineResponseDTO;
import com.tricol.springboottricolapi.dto.Response.SupplierOrderResponseDTO;
import com.tricol.springboottricolapi.entity.SupplierOrder;
import com.tricol.springboottricolapi.entity.SupplierOrderLine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SupplierOrderMapper {

    @Mapping(target = "supplierId", source = "supplier.id")
    @Mapping(target = "supplierName", source = "supplier.raisonSociale")
    @Mapping(target = "orderLines", source = "orderLines")
    SupplierOrderResponseDTO toResponseDTO(SupplierOrder order);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productReference", source = "product.reference")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "unitPrice", source = "unitPurchasePrice")
    @Mapping(target = "totalPrice", source = "lineTotal")
    SupplierOrderLineResponseDTO toLineResponseDTO(SupplierOrderLine line);
}

