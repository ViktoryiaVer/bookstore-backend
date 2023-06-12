package com.somecompany.bookstore.controller.dto.response;

import com.somecompany.bookstore.controller.dto.UserDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "dto for returning paginated result for authors")
public class UsersWithPaginationDto {
    private List<UserDto> users;
    private Integer totalPages;
}
