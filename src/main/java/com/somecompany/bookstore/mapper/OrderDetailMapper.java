package com.somecompany.bookstore.mapper;

import com.somecompany.bookstore.model.entity.OrderDetail;
import com.somecompany.bookstore.controller.dto.OrderDetailDto;
import org.mapstruct.Mapper;

@Mapper(uses = {BookMapper.class})
public interface OrderDetailMapper {
    OrderDetailDto toDto(OrderDetail orderDetail);

    OrderDetail toEntity(OrderDetailDto orderDetailDto);
}
