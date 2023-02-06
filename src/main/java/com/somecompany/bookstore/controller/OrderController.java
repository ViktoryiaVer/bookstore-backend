package com.somecompany.bookstore.controller;

import com.somecompany.bookstore.controller.dto.OrderCreateDto;
import com.somecompany.bookstore.mapper.OrderMapper;
import com.somecompany.bookstore.controller.dto.OrderDto;
import com.somecompany.bookstore.mapper.OrderCreateMapper;
import com.somecompany.bookstore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/orders/")
public class OrderController {
    private final OrderService orderService;
    private final OrderMapper mapper;
    private final OrderCreateMapper writeMapper;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<List<OrderDto>> getAllOrders(Pageable pageable) {
        return ResponseEntity.ok(orderService.getAll(pageable).stream().map(mapper::toDto).toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<OrderDto> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDto(orderService.getById(id)));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody OrderCreateDto order) {
        OrderDto savedOrder = mapper.toDto(orderService.save(writeMapper.toEntity(order)));
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<OrderDto> updateOrder(@Valid @RequestBody OrderCreateDto order) {
        OrderDto updatedOrder = mapper.toDto(orderService.update(writeMapper.toEntity(order)));
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<OrderDto> deleteOrder(@PathVariable Long id) {
        orderService.deleteById(id);
        return ResponseEntity.status(HttpStatus.RESET_CONTENT).build();
    }
}
