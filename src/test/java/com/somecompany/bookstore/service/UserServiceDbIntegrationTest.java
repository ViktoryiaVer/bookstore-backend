package com.somecompany.bookstore.service;

import com.somecompany.bookstore.exception.NotFoundException;
import com.somecompany.bookstore.exception.ObjectAlreadyExistsException;
import com.somecompany.bookstore.exception.ServiceException;
import com.somecompany.bookstore.model.entity.User;
import com.somecompany.bookstore.model.repository.UserRepository;
import com.somecompany.bookstore.util.BookstorePostgresqlContainer;
import com.somecompany.bookstore.util.TestObjectUtil;
import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UserServiceDbIntegrationTest {
    @ClassRule
    public static final PostgreSQLContainer<BookstorePostgresqlContainer> postgreSQLContainer = BookstorePostgresqlContainer.getInstance();
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder encoder;
    private User userWithoutIdToSave;
    private User existingUser;

    @BeforeEach
    public void setup() {
        userWithoutIdToSave = TestObjectUtil.getUserWithoutId();
        existingUser = TestObjectUtil.getExistingUserInDb();
    }

    @Test
    void whenFindAllUsersInPage_thenReturnAllUsersInPage() {
        Pageable pageable = PageRequest.of(0, 10);
        List<User> foundUsers = userService.getAll(pageable).stream().toList();
        assertEquals(pageable.getPageSize(), foundUsers.size());
    }

    @Test
    void whenFindExistingUserById_thenReturnUser() {
        User foundUser = userService.getById(existingUser.getId());

        assertEquals(existingUser.getId(), foundUser.getId());
        assertEquals(existingUser.getFirstName(), foundUser.getFirstName());
        assertEquals(existingUser.getLastName(), foundUser.getLastName());
        assertEquals(existingUser.getEmail(), foundUser.getEmail());
        assertEquals(existingUser.getPhoneNumber(), foundUser.getPhoneNumber());
        assertEquals(existingUser.getLogin().getId(), foundUser.getLogin().getId());
        assertEquals(existingUser.getLogin().getUsername(), foundUser.getLogin().getUsername());
        assertTrue(encoder.matches(existingUser.getLogin().getPassword(), foundUser.getLogin().getPassword()));
    }

    @Test
    void whenFindNonExistingUserById_thenThrowException() {
        Long nonExistingId = 100L;
        assertThrows(NotFoundException.class, () -> userService.getById(nonExistingId));
    }

    @Test
    void whenSaveOneNewUser_thenReturnSavedUser() {
        int listSizeBeforeSaving = getAllUsersListSize();
        userWithoutIdToSave.getLogin().setPassword(encoder.encode(userWithoutIdToSave.getLogin().getPassword()));
        User savedUser = userService.save(userWithoutIdToSave);

        assertNotNull(savedUser.getId());
        assertNotNull(savedUser.getLogin().getId());
        assertEquals(userWithoutIdToSave.getFirstName(), savedUser.getFirstName());
        assertEquals(userWithoutIdToSave.getLastName(), savedUser.getLastName());
        assertEquals(userWithoutIdToSave.getEmail(), savedUser.getEmail());
        assertEquals(userWithoutIdToSave.getPhoneNumber(), savedUser.getPhoneNumber());
        assertEquals(userWithoutIdToSave.getLogin().getUsername(), savedUser.getLogin().getUsername());
        assertEquals(userWithoutIdToSave.getLogin().getUsername(), savedUser.getLogin().getUsername());
        assertEquals(userWithoutIdToSave.getLogin().getPassword(), savedUser.getLogin().getPassword());
        assertEquals(listSizeBeforeSaving + 1, userRepository.findAll().size());

        userRepository.delete(savedUser);
    }

    @Test
    void whenSaveUserWithExistingUsername_thenThrowException() {
        int listSizeBeforeSaving = getAllUsersListSize();
        userWithoutIdToSave.getLogin().setUsername(existingUser.getLogin().getUsername());

        assertThrows(ObjectAlreadyExistsException.class, () -> userService.save(userWithoutIdToSave));
        assertEquals(listSizeBeforeSaving, getAllUsersListSize());
    }

    @Test
    void whenSaveUserWithExistingEmail_thenThrowException() {
        int listSizeBeforeSaving = getAllUsersListSize();
        userWithoutIdToSave.setEmail(existingUser.getEmail());

        assertThrows(ObjectAlreadyExistsException.class, () -> userService.save(userWithoutIdToSave));
        assertEquals(listSizeBeforeSaving, getAllUsersListSize());
    }

    @Test
    void whenUpdateUser_thenReturnUpdatedUser() {
        int listSizeBeforeUpdating = getAllUsersListSize();
        existingUser.getLogin().setPassword(encoder.encode(existingUser.getLogin().getPassword()));
        existingUser.setFirstName("Updated-Firstname");
        User updatedUser = userService.update(existingUser);

        assertEquals(existingUser.getId(), updatedUser.getId());
        assertEquals(existingUser.getFirstName(), updatedUser.getFirstName());
        assertEquals(existingUser.getLastName(), updatedUser.getLastName());
        assertEquals(existingUser.getEmail(), updatedUser.getEmail());
        assertEquals(existingUser.getPhoneNumber(), updatedUser.getPhoneNumber());
        assertEquals(existingUser.getLogin().getId(), updatedUser.getLogin().getId());
        assertEquals(existingUser.getLogin().getUsername(), updatedUser.getLogin().getUsername());
        assertEquals(existingUser.getLogin().getPassword(), updatedUser.getLogin().getPassword());
        assertEquals(listSizeBeforeUpdating, getAllUsersListSize());

        existingUser.setFirstName(TestObjectUtil.getExistingUserInDb().getFirstName());
        userRepository.saveAndFlush(existingUser);
    }

    @Test
    void whenUpdateUserWithExistingUsernameButAnotherId_thenThrowException() {
        existingUser.setId(16L);
        existingUser.setEmail("updated_reader@gmail.com");
        existingUser.setFirstName("Updated-Firstname");

        assertThrows(ObjectAlreadyExistsException.class, () -> userService.update(existingUser));
    }

    @Test
    void whenUpdateUserWithExistingEmailButAnotherId_thenThrowException() {
        existingUser.setId(16L);
        existingUser.getLogin().setUsername("UpdatedReader12345!");
        existingUser.setFirstName("Updated-Firstname");

        assertThrows(ObjectAlreadyExistsException.class, () -> userService.update(existingUser));
    }

    @Test
    void whenUpdateNonExistingUser_thenThrowException() {
        existingUser.setId(100L);
        assertThrows(ServiceException.class, () -> userService.update(existingUser));
    }

    @Test
    void whenDeleteUserWithoutOrders_thenUserIsDeleted() {
        User savedUser = userRepository.saveAndFlush(userWithoutIdToSave);
        int listSizeBeforeDeleting = getAllUsersListSize();

        userService.deleteById(savedUser.getId());
        assertEquals(listSizeBeforeDeleting - 1, getAllUsersListSize());
    }

    @Test
    void whenDeleteUserWithOrders_thenThrowException() {
        Long userWithOrdersId = 8L;
        int listSizeBeforeDeleting = getAllUsersListSize();

        assertThrows(ServiceException.class, () -> userService.deleteById(userWithOrdersId));
        assertEquals(listSizeBeforeDeleting, getAllUsersListSize());
    }

    @Test
    void whenDeleteNonExistingUser_thenThrowException() {
        Long nonExistingUserId = 100L;
        int listSizeBeforeDeleting = getAllUsersListSize();

        assertThrows(NotFoundException.class, () -> userService.deleteById(nonExistingUserId));
        assertEquals(listSizeBeforeDeleting, getAllUsersListSize());
    }

    private int getAllUsersListSize() {
        return userRepository.findAll().size();
    }
}
