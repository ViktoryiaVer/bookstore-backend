package com.somecompany.bookstore.controller.dto.response;

import com.somecompany.bookstore.controller.dto.PaymentDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "dto for returning paginated result for authors")
public class PaymentsWithPaginationDto {
    private List<PaymentDto> payments;
    private Integer totalPages;
}
