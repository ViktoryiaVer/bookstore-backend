package com.somecompany.bookstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.somecompany.bookstore.SecurityConfiguration;
import com.somecompany.bookstore.controller.dto.BookCreateDto;
import com.somecompany.bookstore.controller.dto.BookDto;
import com.somecompany.bookstore.controller.dto.response.BooksWithPaginationDto;
import com.somecompany.bookstore.controller.dto.response.ValidationResultDto;
import com.somecompany.bookstore.exception.NotFoundException;
import com.somecompany.bookstore.exception.ObjectAlreadyExistsException;
import com.somecompany.bookstore.exception.ServiceException;
import com.somecompany.bookstore.mapper.BookCreateMapper;
import com.somecompany.bookstore.mapper.BookMapper;
import com.somecompany.bookstore.model.entity.Book;
import com.somecompany.bookstore.model.repository.AuthorRepository;
import com.somecompany.bookstore.security.CustomBasicAuthenticationEntryPoint;
import com.somecompany.bookstore.security.jwt.JwtAuthenticationFilter;
import com.somecompany.bookstore.security.jwt.JwtUtils;
import com.somecompany.bookstore.service.api.BookService;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

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
@ContextConfiguration(classes = {JwtAuthenticationFilter.class, JwtUtils.class, BookController.class,
        ExceptionController.class, SecurityConfiguration.class})
class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BookService bookService;
    @MockBean
    private BookMapper bookMapper;
    @MockBean
    private BookCreateMapper bookCreateMapper;
    @MockBean
    private AuthorRepository authorRepository;
    @MockBean
    private UserDetailsService userDetailsService;
    @MockBean
    private CustomBasicAuthenticationEntryPoint customBasicAuthenticationEntryPoint;
    private BookCreateDto bookDtoToSave;
    private BookCreateDto bookDtoToUpdate;
    private BookDto bookDtoToRead;
    private Book bookWithoutId;
    private Book bookWithId;

    @BeforeEach
    public void setup() {
        bookDtoToSave = TestObjectUtil.getBookDtoToSave();
        bookDtoToUpdate = TestObjectUtil.getBookDtoToUpdate();
        bookDtoToRead = TestObjectUtil.getBookDtoToRead();
        bookWithoutId = TestObjectUtil.getBookWithoutId();
        bookWithId = TestObjectUtil.getBookWithId();
    }

    @Test
    @WithMockUser(authorities = "USER")
    void whenRequestAllBooks_thenReturnOkAndAllBooks() throws Exception {
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        Page<Book> bookPage = new PageImpl<>(TestObjectUtil.getBookList());

        when(bookService.getAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(bookWithId)).thenReturn(bookDtoToRead);

        MvcResult mvcResult = this.mockMvc.perform(get("/api/books/")
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize()))
                        .param("sort", "id"))
                .andDo(print())
                .andExpectAll(status().isOk(), content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        BooksWithPaginationDto foundBooks = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BooksWithPaginationDto.class);
        assertEquals(bookDtoToRead, foundBooks.getBooks().get(0));
        verify(bookService, times(1)).getAll(pageable);
        verify(bookMapper, times(1)).toDto(bookWithId);
    }

    @Test
    @WithMockUser(authorities = "USER")
    void whenRequestExistingBook_thenReturnOkAndBook() throws Exception {
        Long bookId = bookWithId.getId();
        when(bookService.getById(bookId)).thenReturn(bookWithId);
        when(bookMapper.toDto(bookWithId)).thenReturn(bookDtoToRead);

        MvcResult mvcResult = this.mockMvc.perform(get("/api/books/" + bookId))
                .andDo(print())
                .andExpectAll(status().isOk(), content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        BookDto foundBook = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BookDto.class);
        assertEquals(bookDtoToRead, foundBook);
        verify(bookService, times(1)).getById(bookId);
        verify(bookMapper, times(1)).toDto(bookWithId);
    }

    @Test
    @WithMockUser(authorities = "USER")
    void whenRequestNonExistingBook_thenReturn404() throws Exception {
        Long bookId = 100L;
        when(bookService.getById(bookId)).thenThrow(NotFoundException.class);

        this.mockMvc.perform(get("/api/books/" + bookId))
                .andDo(print())
                .andExpectAll(status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON));

        verify(bookService, times(1)).getById(bookId);
        verify(bookMapper, never()).toDto(bookWithId);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void givenCorrectData_whenRequestBookCreation_thenReturnStatusCreatedAndSavedBook() throws Exception {
        when(bookCreateMapper.toEntity(bookDtoToSave)).thenReturn(bookWithoutId);
        when(bookService.save(bookWithoutId)).thenReturn(bookWithId);
        when(bookMapper.toDto(bookWithId)).thenReturn(bookDtoToRead);
        when(authorRepository.existsById(bookWithId.getId())).thenReturn(true);

        MvcResult mvcResult = this.mockMvc.perform(post("/api/books/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(bookDtoToSave)))
                .andDo(print())
                .andExpectAll(status().isCreated(), content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        BookDto createdBook = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BookDto.class);
        assertEquals(bookDtoToRead, createdBook);
        verify(bookService, times(1)).save(bookWithoutId);
        verify(bookMapper, times(1)).toDto(bookWithId);
        verify(bookCreateMapper, times(1)).toEntity(bookDtoToSave);
    }

    @Test
    @WithMockUser(authorities = "USER")
    void givenUserWithInsufficientRights_whenRequestBookCreation_thenReturn403() throws Exception {
        when(authorRepository.existsById(bookWithId.getId())).thenReturn(true);

        this.mockMvc.perform(post("/api/books/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(bookDtoToSave)))
                .andDo(print())
                .andExpectAll(status().isForbidden(), content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        verify(bookService, never()).save(bookWithoutId);
        verify(bookMapper, never()).toDto(bookWithId);
        verify(bookCreateMapper, never()).toEntity(bookDtoToSave);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void givenIncorrectData_whenRequestBookCreation_thenReturnStatus400AndErrors() throws Exception {
        bookDtoToSave.setPrice(BigDecimal.ZERO);
        when(bookCreateMapper.toEntity(bookDtoToSave)).thenReturn(bookWithoutId);

        MvcResult mvcResult = this.mockMvc.perform(post("/api/books/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(bookDtoToSave)))
                .andDo(print())
                .andExpectAll(status().isBadRequest(), content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        ValidationResultDto validationResultDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ValidationResultDto.class);
        assertNotNull(validationResultDto);
        verify(bookService, never()).save(bookWithoutId);
        verify(bookMapper, never()).toDto(bookWithId);
        verify(bookCreateMapper, never()).toEntity(bookDtoToSave);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void whenRequestBookCreationWithExistingData_thenReturn500() throws Exception {
        when(bookCreateMapper.toEntity(bookDtoToSave)).thenReturn(bookWithoutId);
        when(authorRepository.existsById(bookWithId.getId())).thenReturn(true);
        when(bookService.save(bookWithoutId)).thenThrow(ObjectAlreadyExistsException.class);

        this.mockMvc.perform(post("/api/books/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(bookDtoToSave)))
                .andDo(print())
                .andExpectAll(status().isInternalServerError(),
                        content().contentType(MediaType.APPLICATION_JSON));

        verify(bookCreateMapper, times(1)).toEntity(bookDtoToSave);
        verify(bookService, times(1)).save(bookWithoutId);
        verify(bookMapper, never()).toDto(bookWithId);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void givenCorrectData_whenRequestBookUpdate_thenReturnStatusOkAndUpdatedBook() throws Exception {
        bookWithId.setTitle("Test-Upd");
        bookDtoToUpdate.setTitle("Test-Upd");
        bookDtoToRead.setTitle("Test-Upd");

        when(bookCreateMapper.toEntity(bookDtoToUpdate)).thenReturn(bookWithId);
        when(authorRepository.existsById(bookWithId.getId())).thenReturn(true);
        when(bookService.update(bookWithId)).thenReturn(bookWithId);
        when(bookMapper.toDto(bookWithId)).thenReturn(bookDtoToRead);


        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.put("/api/books/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(bookDtoToUpdate)))
                .andDo(print())
                .andExpectAll(status().isOk(), content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        BookDto updatedBook = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BookDto.class);
        assertEquals(bookDtoToRead, updatedBook);
        verify(bookService, times(1)).update(bookWithId);
        verify(bookMapper, times(1)).toDto(bookWithId);
        verify(bookCreateMapper, times(1)).toEntity(bookDtoToUpdate);
    }

    @Test
    @WithMockUser(authorities = "USER")
    void givenUserWithInsufficientRights_whenRequestBookUpdate_thenReturn403() throws Exception {
        when(authorRepository.existsById(bookWithId.getId())).thenReturn(true);

        this.mockMvc.perform(put("/api/books/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(bookDtoToUpdate)))
                .andDo(print())
                .andExpectAll(status().isForbidden(), content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        verify(bookService, never()).update(bookWithoutId);
        verify(bookMapper, never()).toDto(bookWithId);
        verify(bookCreateMapper, never()).toEntity(bookDtoToUpdate);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void givenIncorrectData_whenRequestBookUpdate_thenReturnStatus400AndErrors() throws Exception {
        bookWithId.setPrice(BigDecimal.ZERO);
        bookDtoToUpdate.setPrice(BigDecimal.ZERO);
        bookDtoToRead.setPrice(BigDecimal.ZERO);

        when(bookCreateMapper.toEntity(bookDtoToSave)).thenReturn(bookWithId);
        when(authorRepository.existsById(bookWithId.getId())).thenReturn(true);

        MvcResult mvcResult = this.mockMvc.perform(put("/api/books/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(bookDtoToUpdate)))
                .andDo(print())
                .andExpectAll(status().isBadRequest(), content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        ValidationResultDto validationResultDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ValidationResultDto.class);
        assertNotNull(validationResultDto);
        verify(bookService, never()).update(bookWithId);
        verify(bookMapper, never()).toDto(bookWithId);
        verify(bookCreateMapper, never()).toEntity(bookDtoToUpdate);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void whenRequestBookUpdateWithExistingDataAndAnotherId_thenReturn500() throws Exception {
        when(bookCreateMapper.toEntity(bookDtoToUpdate)).thenReturn(bookWithId);
        when(authorRepository.existsById(bookWithId.getId())).thenReturn(true);
        when(bookService.update(bookWithId)).thenThrow(ObjectAlreadyExistsException.class);

        this.mockMvc.perform(put("/api/books/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(bookDtoToUpdate)))
                .andDo(print())
                .andExpectAll(status().isInternalServerError(),
                        content().contentType(MediaType.APPLICATION_JSON));

        verify(bookCreateMapper, times(1)).toEntity(bookDtoToUpdate);
        verify(bookService, times(1)).update(bookWithId);
        verify(bookMapper, never()).toDto(bookWithId);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void givenCorrectExistingId_whenRequestBookDelete_thenReturn205AndNoContent() throws Exception {
        Long bookId = bookWithId.getId();
        doNothing().when(bookService).deleteById(bookId);

        this.mockMvc.perform(delete("/api/books/" + bookId))
                .andDo(print())
                .andExpectAll(status().isResetContent(), jsonPath("$").doesNotExist());

        verify(bookService, times(1)).deleteById(bookId);
    }

    @Test
    @WithMockUser(authorities = "USER")
    void givenUserWithInsufficientRights_whenRequestBookDelete_thenReturn403() throws Exception {
        Long bookId = bookWithId.getId();
        when(authorRepository.existsById(bookWithId.getId())).thenReturn(true);

        this.mockMvc.perform(delete("/api/books/" + bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(bookDtoToUpdate)))
                .andDo(print())
                .andExpectAll(status().isForbidden(), content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        verify(bookService, never()).deleteById(bookId);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void givenIdOfInvalidType_whenRequestBookDelete_thenReturn205BadRequest() throws Exception {
        String invalidString = "asf";

        this.mockMvc.perform(delete("/api/books/" + invalidString))
                .andDo(print())
                .andExpectAll(status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.message").isNotEmpty());

        verify(bookService, never()).deleteById(any());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void whenRequestBookDeleteForNonExistingId_thenReturn205BadRequest() throws Exception {
        Long bookId = 100L;
        doThrow(NotFoundException.class).when(bookService).deleteById(bookId);

        this.mockMvc.perform(delete("/api/books/" + bookId))
                .andDo(print())
                .andExpectAll(status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON));

        verify(bookService, times(1)).deleteById(bookId);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void whenRequestBookDeleteForUserWithOrders_thenReturn500() throws Exception {
        Long bookId = bookWithId.getId();
        doThrow(ServiceException.class).when(bookService).deleteById(bookId);

        this.mockMvc.perform(delete("/api/books/" + bookId))
                .andDo(print())
                .andExpectAll(status().isInternalServerError(),
                        content().contentType(MediaType.APPLICATION_JSON));

        verify(bookService, times(1)).deleteById(bookId);
    }
}
