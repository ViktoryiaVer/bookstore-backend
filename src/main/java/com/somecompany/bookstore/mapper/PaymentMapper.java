package com.somecompany.bookstore.mapper;

import com.somecompany.bookstore.model.entity.Payment;
import com.somecompany.bookstore.controller.dto.PaymentDto;
import org.mapstruct.Mapper;

@Mapper(uses = {UserMapper.class, OrderMapper.class})
public interface PaymentMapper {
    PaymentDto toDto(Payment payment);

    Payment toEntity(PaymentDto paymentDto);
}
