package com.somecompany.bookstore.mapper;

import com.somecompany.bookstore.model.entity.OrderDetail;
import com.somecompany.bookstore.controller.dto.OrderDetailDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BookMapper.class})
public interface OrderDetailMapper {
    @Mapping(target = "order", ignore = true)
    OrderDetailDto toDto(OrderDetail orderDetail);

    @Mapping(target = "order", ignore = true)
    OrderDetail toEntity(OrderDetailDto orderDetailDto);
}
