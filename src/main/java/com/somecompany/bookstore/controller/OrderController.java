package com.somecompany.bookstore.controller;

import com.somecompany.bookstore.controller.dto.OrderWriteDto;
import com.somecompany.bookstore.mapper.OrderMapper;
import com.somecompany.bookstore.controller.dto.OrderDto;
import com.somecompany.bookstore.mapper.OrderWriteMapper;
import com.somecompany.bookstore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/orders/")
public class OrderController {
    private final OrderService orderService;
    private final OrderMapper mapper;
    private final OrderWriteMapper writeMapper;
    @GetMapping()
    public List<OrderDto> getAllOrders() {
        return orderService.getAll().stream().map(mapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public OrderDto getOrder(@PathVariable Long id) {
        return mapper.toDto(orderService.getById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto createOrder(@RequestBody OrderWriteDto order) {
        return mapper.toDto(orderService.save(writeMapper.toEntity(order)));
    }

    @PutMapping()
    public OrderDto updateOrder(@RequestBody OrderWriteDto order) {
        return mapper.toDto(orderService.update(writeMapper.toEntity(order)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.RESET_CONTENT)
    public void deleteOrder(@PathVariable Long id) {
        orderService.deleteById(id);
    }
}
