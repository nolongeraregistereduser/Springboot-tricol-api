package com.tricol.springboottricolapi.mapper;

import com.tricol.springboottricolapi.dto.Response.DeliveryNoteLineResponseDTO;
import com.tricol.springboottricolapi.dto.Response.DeliveryNoteResponseDTO;
import com.tricol.springboottricolapi.entity.DeliveryNote;
import com.tricol.springboottricolapi.entity.DeliveryNoteLine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DeliveryNoteMapper {

    DeliveryNoteResponseDTO toResponseDTO(DeliveryNote deliveryNote);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productReference", source = "product.reference")
    @Mapping(target = "productName", source = "product.name")
    DeliveryNoteLineResponseDTO toLineResponseDTO(DeliveryNoteLine line);
}