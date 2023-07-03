package com.somecompany.bookstore.controller;

import com.somecompany.bookstore.controller.dto.PaymentCreateDto;
import com.somecompany.bookstore.controller.dto.response.ItemsWithPaginationDto;
import com.somecompany.bookstore.controller.dto.response.MessageDto;
import com.somecompany.bookstore.controller.dto.response.ValidationResultDto;
import com.somecompany.bookstore.mapper.PaymentCreateMapper;
import com.somecompany.bookstore.mapper.PaymentMapper;
import com.somecompany.bookstore.controller.dto.PaymentDto;
import com.somecompany.bookstore.model.entity.Payment;
import com.somecompany.bookstore.service.api.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("api/payments")
@Tag(name = "payments", description = "operations with payments")
public class PaymentController {
    private final PaymentService paymentService;
    private final PaymentMapper mapper;
    private final PaymentCreateMapper writeMapper;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @Operation(summary = "Get all payments (paginated result)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the payments",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ItemsWithPaginationDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid pageable object supplied",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))}),
            @ApiResponse(responseCode = "401", description = "Error by authentication",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))})})
    public ResponseEntity<ItemsWithPaginationDto<PaymentDto>> getAllPayments(@ParameterObject Pageable pageable) {
        Page<Payment> paymentPage = paymentService.getAll(pageable);
        return ResponseEntity.ok(new ItemsWithPaginationDto<>(paymentPage.getContent().stream().map(mapper::toDto).toList(),
                paymentPage.getTotalPages()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a payment by its id")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the payment",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaymentDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))}),
            @ApiResponse(responseCode = "401", description = "Error by authentication",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))}),
            @ApiResponse(responseCode = "404", description = "Payment not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))})})
    public ResponseEntity<PaymentDto> getPayment(@Parameter(description = "Id of the book to be found",
            required = true) @PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDto(paymentService.getById(id)));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Create a payment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Payment is created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaymentDto.class))}),
            @ApiResponse(responseCode = "400", description = "Some of the payment properties are not valid",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationResultDto.class))}),
            @ApiResponse(responseCode = "401", description = "Error by authentication",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))}),
            @ApiResponse(responseCode = "403", description = "Access denied: user has no authority for creating a payment",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))}),
            @ApiResponse(responseCode = "500", description = "Server error by creating a payment",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))})})
    public ResponseEntity<PaymentDto> createPayment(@Valid @RequestBody PaymentCreateDto payment) {
        PaymentDto savedPayment = mapper.toDto(paymentService.save(writeMapper.toEntity(payment)));
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPayment);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Update a payment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment is updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaymentDto.class))}),
            @ApiResponse(responseCode = "400", description = "Some of the payment properties are not valid",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationResultDto.class))}),
            @ApiResponse(responseCode = "401", description = "Error by authentication",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))}),
            @ApiResponse(responseCode = "403", description = "Access denied: user has no authority for updating a payment",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))}),
            @ApiResponse(responseCode = "500", description = "Server error by updating a payment",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))})})
    public ResponseEntity<PaymentDto> updatePayment(@Valid @RequestBody PaymentCreateDto payment) {
        PaymentDto updatedPayment = mapper.toDto(paymentService.update(writeMapper.toEntity(payment)));
        return ResponseEntity.ok(updatedPayment);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Delete a payment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "205", description = "Payment is deleted",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))}),
            @ApiResponse(responseCode = "401", description = "Error by authentication",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))}),
            @ApiResponse(responseCode = "403", description = "Access denied: user has no authority for deleting a payment",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))}),
            @ApiResponse(responseCode = "404", description = "Payment not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))})})
    public ResponseEntity<PaymentDto> deletePayment(@Parameter(description = "Id of the book to be deleted",
            required = true) @PathVariable Long id) {
        paymentService.deleteById(id);
        return ResponseEntity.status(HttpStatus.RESET_CONTENT).build();
    }
}
