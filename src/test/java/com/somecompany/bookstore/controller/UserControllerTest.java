package com.somecompany.bookstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.somecompany.bookstore.SecurityConfiguration;
import com.somecompany.bookstore.controller.dto.UserDto;
import com.somecompany.bookstore.controller.dto.response.ValidationResultDto;
import com.somecompany.bookstore.exception.NotFoundException;
import com.somecompany.bookstore.exception.ObjectAlreadyExistsException;
import com.somecompany.bookstore.exception.ServiceException;
import com.somecompany.bookstore.mapper.UserMapper;
import com.somecompany.bookstore.model.entity.User;
import com.somecompany.bookstore.security.CustomBasicAuthenticationEntryPoint;
import com.somecompany.bookstore.security.jwt.JwtAuthenticationFilter;
import com.somecompany.bookstore.security.jwt.JwtUtils;
import com.somecompany.bookstore.service.UserService;
import com.somecompany.bookstore.util.TestObjectUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ExtendWith({MockitoExtension.class})
@ContextConfiguration(classes = {JwtAuthenticationFilter.class, JwtUtils.class, UserController.class,
        ExceptionController.class, SecurityConfiguration.class})
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;
    @MockBean
    private UserMapper userMapper;
    @MockBean
    private UserDetailsService userDetailsService;
    @MockBean
    private CustomBasicAuthenticationEntryPoint customBasicAuthenticationEntryPoint;
    private UserDto userDtoWithoutId;
    private UserDto userDtoWithId;
    private User userWithoutId;
    private User userWithId;

    @BeforeEach
    public void setup() {
        userDtoWithoutId = TestObjectUtil.getUserDtoWithoutId();
        userDtoWithId = TestObjectUtil.getUserDtoWithId();
        userWithoutId = TestObjectUtil.getUserWithoutId();
        userWithId = TestObjectUtil.getUserWithId();
    }

    @Test
    @WithMockUser(authorities = "USER")
    void whenRequestAllUsers_thenReturnOkAndAllUsers() throws Exception {
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        Page<User> userPage = new PageImpl<>(TestObjectUtil.getUserList());

        when(userService.getAll(pageable)).thenReturn(userPage);
        when(userMapper.toDto(userWithId)).thenReturn(userDtoWithId);

        MvcResult mvcResult = this.mockMvc.perform(get("/api/users/")
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize()))
                        .param("sort", "id"))
                .andDo(print())
                .andExpectAll(status().isOk(), content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        List<UserDto> foundUsers = List.of(objectMapper.readValue(mvcResult.getResponse().getContentAsString(), UserDto[].class));
        assertEquals(userDtoWithId, foundUsers.get(0));
        verify(userService, times(1)).getAll(pageable);
        verify(userMapper, times(1)).toDto(userWithId);
    }

    @Test
    @WithMockUser(authorities = "USER")
    void whenRequestExistingUser_thenReturnOkAndUser() throws Exception {
        Long userId = userWithId.getId();
        when(userService.getById(userId)).thenReturn(userWithId);
        when(userMapper.toDto(userWithId)).thenReturn(userDtoWithId);

        MvcResult mvcResult = this.mockMvc.perform(get("/api/users/" + userId))
                .andDo(print())
                .andExpectAll(status().isOk(), content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        UserDto foundUser = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), UserDto.class);
        assertEquals(userDtoWithId, foundUser);
        verify(userService, times(1)).getById(userId);
        verify(userMapper, times(1)).toDto(userWithId);
    }

    @Test
    @WithMockUser(authorities = "USER")
    void whenRequestNonExistingUser_thenReturn404() throws Exception {
        Long userId = 100L;
        when(userService.getById(userId)).thenThrow(NotFoundException.class);

        this.mockMvc.perform(get("/api/users/" + userId))
                .andDo(print())
                .andExpectAll(status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON));

        verify(userService, times(1)).getById(userId);
        verify(userMapper, never()).toDto(userWithId);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void givenCorrectData_whenRequestUserCreation_thenReturnStatusCreatedAndSavedUser() throws Exception {
        when(userMapper.toEntity(userDtoWithoutId)).thenReturn(userWithoutId);
        when(userService.save(userWithoutId)).thenReturn(userWithId);
        when(userMapper.toDto(userWithId)).thenReturn(userDtoWithId);

        MvcResult mvcResult = this.mockMvc.perform(post("/api/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userDtoWithoutId)))
                .andDo(print())
                .andExpectAll(status().isCreated(), content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        UserDto createdUser = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), UserDto.class);
        assertEquals(userDtoWithId, createdUser);
        verify(userService, times(1)).save(userWithoutId);
        verify(userMapper, times(1)).toDto(userWithId);
        verify(userMapper, times(1)).toEntity(userDtoWithoutId);
    }

    @Test
    @WithMockUser(authorities = "USER")
    void givenUserWithInsufficientRights_whenRequestUserCreation_thenReturn403() throws Exception {
        this.mockMvc.perform(post("/api/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userDtoWithoutId)))
                .andDo(print())
                .andExpectAll(status().isForbidden(), content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        verify(userService, never()).save(userWithoutId);
        verify(userMapper, never()).toDto(userWithId);
        verify(userMapper, never()).toEntity(userDtoWithoutId);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void givenIncorrectData_whenRequestUserCreation_thenReturnStatus400AndErrors() throws Exception {
        userDtoWithoutId.setFirstName("12345");
        when(userMapper.toEntity(userDtoWithoutId)).thenReturn(userWithoutId);

        MvcResult mvcResult = this.mockMvc.perform(post("/api/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userDtoWithoutId)))
                .andDo(print())
                .andExpectAll(status().isBadRequest(), content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        ValidationResultDto validationResultDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ValidationResultDto.class);
        assertNotNull(validationResultDto);
        verify(userService, never()).save(userWithoutId);
        verify(userMapper, never()).toDto(userWithId);
        verify(userMapper, never()).toEntity(userDtoWithoutId);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void whenRequestUserCreationWithExistingData_thenReturn500() throws Exception {
        when(userMapper.toEntity(userDtoWithoutId)).thenReturn(userWithoutId);
        when(userService.save(userWithoutId)).thenThrow(ObjectAlreadyExistsException.class);

        this.mockMvc.perform(post("/api/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userDtoWithoutId)))
                .andDo(print())
                .andExpectAll(status().isInternalServerError(),
                        content().contentType(MediaType.APPLICATION_JSON));

        verify(userMapper, times(1)).toEntity(userDtoWithoutId);
        verify(userService, times(1)).save(userWithoutId);
        verify(userMapper, never()).toDto(userWithId);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void givenCorrectData_whenRequestUserUpdate_thenReturnStatusOkAndUpdatedUser() throws Exception {
        userWithId.setFirstName("Test-Upd");
        userDtoWithId.setFirstName("Test-Upd");

        when(userMapper.toEntity(userDtoWithId)).thenReturn(userWithId);
        when(userService.update(userWithId)).thenReturn(userWithId);
        when(userMapper.toDto(userWithId)).thenReturn(userDtoWithId);

        MvcResult mvcResult = this.mockMvc.perform(put("/api/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userDtoWithId)))
                .andDo(print())
                .andExpectAll(status().isOk(), content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        UserDto updatedUser = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), UserDto.class);
        assertEquals(userDtoWithId, updatedUser);
        verify(userService, times(1)).update(userWithId);
        verify(userMapper, times(1)).toDto(userWithId);
        verify(userMapper, times(1)).toEntity(userDtoWithId);
    }

    @Test
    @WithMockUser(authorities = "USER")
    void givenUserWithInsufficientRights_whenRequestUserUpdate_thenReturn403() throws Exception {
        this.mockMvc.perform(put("/api/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userDtoWithoutId)))
                .andDo(print())
                .andExpectAll(status().isForbidden(), content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        verify(userService, never()).update(userWithId);
        verify(userMapper, never()).toDto(userWithId);
        verify(userMapper, never()).toEntity(userDtoWithId);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void givenIncorrectData_whenRequestUserUpdate_thenReturnStatus400AndErrors() throws Exception {
        userDtoWithId.setFirstName("12345");
        userWithId.setFirstName("12345");

        when(userMapper.toEntity(userDtoWithId)).thenReturn(userWithId);

        MvcResult mvcResult = this.mockMvc.perform(put("/api/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userDtoWithId)))
                .andDo(print())
                .andExpectAll(status().isBadRequest(), content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        ValidationResultDto validationResultDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ValidationResultDto.class);
        assertNotNull(validationResultDto);
        verify(userService, never()).update(userWithId);
        verify(userMapper, never()).toDto(userWithId);
        verify(userMapper, never()).toEntity(userDtoWithId);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void whenRequestUserUpdateWithExistingDataAndAnotherId_thenReturn500() throws Exception {
        when(userMapper.toEntity(userDtoWithId)).thenReturn(userWithId);
        when(userService.update(userWithId)).thenThrow(ObjectAlreadyExistsException.class);

        this.mockMvc.perform(put("/api/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userDtoWithId)))
                .andDo(print())
                .andExpectAll(status().isInternalServerError(),
                        content().contentType(MediaType.APPLICATION_JSON));

        verify(userMapper, times(1)).toEntity(userDtoWithId);
        verify(userService, times(1)).update(userWithId);
        verify(userMapper, never()).toDto(userWithId);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void givenCorrectExistingId_whenRequestUserDelete_thenReturn205AndNoContent() throws Exception {
        Long userId = userWithId.getId();
        doNothing().when(userService).deleteById(userId);

        this.mockMvc.perform(delete("/api/users/" + userId))
                .andDo(print())
                .andExpectAll(status().isResetContent(), jsonPath("$").doesNotExist());

        verify(userService, times(1)).deleteById(userId);
    }

    @Test
    @WithMockUser(authorities = "USER")
    void givenUserWithInsufficientRights_whenRequestUserDelete_thenReturn403() throws Exception {
        Long userId = userWithId.getId();
        this.mockMvc.perform(delete("/api/users/" + userId))
                .andDo(print())
                .andExpectAll(status().isForbidden(), content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        verify(userService, never()).deleteById(userId);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void givenIdOfInvalidType_whenRequestUserDelete_thenReturn205BadRequest() throws Exception {
        String invalidString = "asf";

        this.mockMvc.perform(delete("/api/users/" + invalidString))
                .andDo(print())
                .andExpectAll(status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.message").isNotEmpty());

        verify(userService, never()).deleteById(any());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void whenRequestUserDeleteForNonExistingId_thenReturn205BadRequest() throws Exception {
        Long userId = 100L;
        doThrow(NotFoundException.class).when(userService).deleteById(userId);

        this.mockMvc.perform(delete("/api/users/" + userId))
                .andDo(print())
                .andExpectAll(status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON));

        verify(userService, times(1)).deleteById(userId);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void whenRequestUserDeleteForUserWithOrders_thenReturn500() throws Exception {
        Long userId = userWithId.getId();
        doThrow(ServiceException.class).when(userService).deleteById(userId);

        this.mockMvc.perform(delete("/api/users/" + userId))
                .andDo(print())
                .andExpectAll(status().isInternalServerError(),
                        content().contentType(MediaType.APPLICATION_JSON));

        verify(userService, times(1)).deleteById(userId);
    }
}
