package com.somecompany.bookstore.mapper;

import com.somecompany.bookstore.model.entity.Order;
import com.somecompany.bookstore.controller.dto.OrderDto;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;

@Mapper(uses = {OrderDetailMapper.class, UserMapper.class}, collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface OrderMapper {
    OrderDto toDto(Order order);

    Order toEntity(OrderDto orderDto);
}
