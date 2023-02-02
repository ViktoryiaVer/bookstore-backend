package com.somecompany.bookstore.controller;

import com.somecompany.bookstore.controller.dto.OrderCreateDto;
import com.somecompany.bookstore.exception.ValidationException;
import com.somecompany.bookstore.mapper.OrderMapper;
import com.somecompany.bookstore.controller.dto.OrderDto;
import com.somecompany.bookstore.mapper.OrderWriteMapper;
import com.somecompany.bookstore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("api/orders/")
@RequiredArgsConstructor
@RestController
public class OrderController {
    private final OrderService orderService;
    private final OrderMapper mapper;
    private final OrderWriteMapper writeMapper;

    @GetMapping
    public List<OrderDto> getAllOrders(Pageable pageable) {
        return orderService.getAll(pageable).stream().map(mapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public OrderDto getOrder(@PathVariable Long id) {
        return mapper.toDto(orderService.getById(id));
    }

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody OrderCreateDto order, Errors errors) {
        if (errors.hasErrors()) {
            throw new ValidationException(errors);
        }

        OrderDto savedOrder = mapper.toDto(orderService.save(writeMapper.toEntity(order)));
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
    }

    @PutMapping
    public ResponseEntity<OrderDto> updateOrder(@Valid @RequestBody OrderCreateDto order, Errors errors) {
        if (errors.hasErrors()) {
            throw new ValidationException(errors);
        }

        OrderDto updatedOrder = mapper.toDto(orderService.update(writeMapper.toEntity(order)));
        return ResponseEntity.ok(updatedOrder);
    }

    @ResponseStatus(HttpStatus.RESET_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        orderService.deleteById(id);
    }
}
