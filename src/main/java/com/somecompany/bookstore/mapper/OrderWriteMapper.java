package com.somecompany.bookstore.mapper;

import com.somecompany.bookstore.controller.dto.OrderCreateDto;
import com.somecompany.bookstore.model.entity.Order;
import com.somecompany.bookstore.model.entity.User;
import com.somecompany.bookstore.service.UserService;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(uses = {OrderDetailWriteMapper.class, UserMapper.class}, collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public abstract class OrderWriteMapper {
    @Autowired
    private UserService userService;

    @Mapping(source = "userId", target = "user")
    public abstract Order toEntity(OrderCreateDto orderDto);

    protected User getUserById(Long id) {
        return userService.getById(id);
    }
}
