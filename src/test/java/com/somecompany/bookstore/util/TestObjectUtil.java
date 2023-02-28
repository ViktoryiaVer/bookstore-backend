package com.somecompany.bookstore.util;

import com.somecompany.bookstore.controller.dto.AuthorDto;
import com.somecompany.bookstore.controller.dto.BookCreateDto;
import com.somecompany.bookstore.controller.dto.BookDto;
import com.somecompany.bookstore.controller.dto.LoginDto;
import com.somecompany.bookstore.controller.dto.UserDto;
import com.somecompany.bookstore.model.entity.Author;
import com.somecompany.bookstore.model.entity.Book;
import com.somecompany.bookstore.model.entity.Login;
import com.somecompany.bookstore.model.entity.User;
import com.somecompany.bookstore.model.entity.enums.Cover;
import com.somecompany.bookstore.model.entity.enums.Role;
import com.somecompany.bookstore.util.constant.TestObjectConstant;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TestObjectUtil {
    public static List<User> getUserList() {
        List<User> users = new ArrayList<>();
        users.add(getUserWithId());
        return users;
    }

    public static User getUserWithoutId() {
        User user = new User();
        user.setLogin(getLoginWithoutId());
        user.setFirstName(TestObjectConstant.USER_FIRSTNAME);
        user.setLastName(TestObjectConstant.USER_LASTNAME);
        user.setEmail(TestObjectConstant.USER_EMAIL);
        user.setPhoneNumber(TestObjectConstant.USER_PHONE_NUMBER);
        user.setRole(Role.valueOf(TestObjectConstant.USER_ROLE));
        return user;
    }

    public static User getUserWithId() {
        User user = getUserWithoutId();
        user.setId(TestObjectConstant.USER_ID);
        user.getLogin().setId(TestObjectConstant.LOGIN_ID);
        return user;
    }

    public static User getExistingUserInDb() {
        User user = new User();
        user.setId(17L);
        user.setFirstName("Reader");
        user.setLastName("Reader");
        user.setEmail("reader@mail.ru");
        user.setPhoneNumber("+3758888888");
        user.setRole(Role.USER);

        Login login = new Login();
        login.setId(17L);
        login.setUsername("reader");
        login.setPassword("reader");

        user.setLogin(login);
        return user;
    }

    public static UserDto getUserDtoWithoutId() {
        UserDto user = new UserDto();
        user.setLogin(getLoginDto());
        user.setFirstName(TestObjectConstant.USER_FIRSTNAME);
        user.setLastName(TestObjectConstant.USER_LASTNAME);
        user.setEmail(TestObjectConstant.USER_EMAIL);
        user.setPhoneNumber(TestObjectConstant.USER_PHONE_NUMBER);
        user.setRole(Role.valueOf(TestObjectConstant.USER_ROLE));
        return user;
    }

    public static UserDto getUserDtoWithId() {
        UserDto userDto = getUserDtoWithoutId();
        userDto.setId(TestObjectConstant.USER_ID);
        userDto.getLogin().setId(TestObjectConstant.LOGIN_ID);
        return userDto;
    }

    public static List<Book> getBookList() {
        List<Book> books = new ArrayList<>();
        books.add(getBookWithId());
        return books;
    }

    public static Book getBookWithoutId() {
        Book book = new Book();
        book.setTitle(TestObjectConstant.BOOK_TITLE);
        book.setPublisher(TestObjectConstant.BOOK_PUBLISHER);
        book.setIsbn(TestObjectConstant.BOOK_ISBN);
        book.setYearOfPublication(TestObjectConstant.BOOK_PUBLICATION_YEAR);
        book.setPrice(TestObjectConstant.BOOK_PRICE);
        book.setCover(Cover.valueOf(TestObjectConstant.BOOK_COVER));
        book.setAuthors(new ArrayList<>(List.of(getAuthor())));
        return book;
    }

    public static Book getBookWithId() {
        Book book = getBookWithoutId();
        book.setId(TestObjectConstant.USER_ID);
        return book;
    }

    public static Book getExistingBook() {
        Book book = new Book();
        book.setId(17L);
        book.setTitle("The Magic Mountain");
        book.setPublisher("VINTAGE");
        book.setIsbn("0749386428");
        book.setYearOfPublication(1996);
        book.setPrice(BigDecimal.valueOf(40.23));
        book.setCover(Cover.HARD);

        Author author = new Author();
        author.setId(14L);
        author.setFirstName("Thomas");
        author.setLastName("Mann");
        author.setBirthdate(LocalDate.of(1875, 6, 6));
        book.setAuthors(new ArrayList<>(List.of(author)));
        return book;
    }

    public static BookCreateDto getBookDtoToSave() {
        BookCreateDto book = new BookCreateDto();
        book.setTitle(TestObjectConstant.BOOK_TITLE);
        book.setPublisher(TestObjectConstant.BOOK_PUBLISHER);
        book.setIsbn(TestObjectConstant.BOOK_ISBN);
        book.setYearOfPublication(TestObjectConstant.BOOK_PUBLICATION_YEAR);
        book.setPrice(TestObjectConstant.BOOK_PRICE);
        book.setCover(Cover.valueOf(TestObjectConstant.BOOK_COVER));
        book.setAuthorIds(new ArrayList<>(List.of(TestObjectConstant.AUTHOR_ID)));
        return book;
    }

    public static BookCreateDto getBookDtoToUpdate() {
        BookCreateDto book = getBookDtoToSave();
        book.setId(TestObjectConstant.BOOK_ID);
        return book;
    }

    public static BookDto getBookDtoToRead() {
        BookDto book = new BookDto();
        book.setId(TestObjectConstant.USER_ID);
        book.setTitle(TestObjectConstant.BOOK_TITLE);
        book.setPublisher(TestObjectConstant.BOOK_PUBLISHER);
        book.setIsbn(TestObjectConstant.BOOK_ISBN);
        book.setYearOfPublication(TestObjectConstant.BOOK_PUBLICATION_YEAR);
        book.setPrice(TestObjectConstant.BOOK_PRICE);
        book.setCover(Cover.valueOf(TestObjectConstant.BOOK_COVER));
        book.setAuthors(new ArrayList<>(List.of(getAuthorDto())));
        return book;
    }

    public static Author getAuthor() {
        Author author = new Author();
        author.setId(TestObjectConstant.AUTHOR_ID);
        author.setFirstName(TestObjectConstant.AUTHOR_FIRSTNAME);
        author.setLastName(TestObjectConstant.AUTHOR_LASTNAME);
        author.setBirthdate(TestObjectConstant.AUTHOR_BIRTHDATE);
        return author;
    }

    public static AuthorDto getAuthorDto() {
        AuthorDto author = new AuthorDto();
        author.setId(TestObjectConstant.AUTHOR_ID);
        author.setFirstName(TestObjectConstant.AUTHOR_FIRSTNAME);
        author.setLastName(TestObjectConstant.AUTHOR_LASTNAME);
        author.setBirthdate(TestObjectConstant.AUTHOR_BIRTHDATE);
        return author;
    }

    private static Login getLoginWithoutId() {
        Login login = new Login();
        login.setUsername(TestObjectConstant.LOGIN_USERNAME);
        login.setPassword(TestObjectConstant.LOGIN_PASSWORD);
        return login;
    }

    private static LoginDto getLoginDto() {
        LoginDto login = new LoginDto();
        login.setUsername(TestObjectConstant.LOGIN_USERNAME);
        login.setPassword(TestObjectConstant.LOGIN_PASSWORD);
        return login;
    }
}
