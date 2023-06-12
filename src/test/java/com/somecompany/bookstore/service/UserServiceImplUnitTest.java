package com.somecompany.bookstore.service;

import com.somecompany.bookstore.exception.NotFoundException;
import com.somecompany.bookstore.exception.ObjectAlreadyExistsException;
import com.somecompany.bookstore.exception.ServiceException;
import com.somecompany.bookstore.model.entity.User;
import com.somecompany.bookstore.model.repository.OrderRepository;
import com.somecompany.bookstore.model.repository.UserRepository;
import com.somecompany.bookstore.service.api.UserService;
import com.somecompany.bookstore.util.TestObjectUtil;
import com.somecompany.bookstore.util.constant.TestObjectConstant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
class UserServiceImplUnitTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private MessageSource messageSource;
    private UserService userService;
    private User userWithoutId;
    private User userWithId;

    @BeforeEach
    public void setup() {
        userService = new UserServiceImpl(userRepository, orderRepository, messageSource);
        userWithoutId = TestObjectUtil.getUserWithoutId();
        userWithId = TestObjectUtil.getUserWithId();
    }

    @Test
    void whenFindAllUsers_thenReturnUsers() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<User> pageUser = new PageImpl<>(TestObjectUtil.getUserList());

        when(userRepository.findAll(pageable)).thenReturn(pageUser);
        Page<User> foundPage = userService.getAll(pageable);

        assertEquals(pageUser, foundPage);
        verify(userRepository, times(1)).findAll(pageable);
    }

    @Test
    void whenFindExitingUserById_thenReturnUser() {
        Long userId = TestObjectConstant.USER_ID;
        when(userRepository.findByIdOrException(userId)).thenCallRealMethod();
        when(userRepository.findById(userId)).thenReturn(Optional.of(userWithId));
        User foundUser = userService.getById(userId);

        assertEquals(userWithId, foundUser);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void whenFindNonExitingUserById_thenThrowException() {
        Long userId = TestObjectConstant.USER_ID;
        when(userRepository.findByIdOrException(userId)).thenCallRealMethod();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getById(userId));
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void whenSaveNewUser_thenReturnSavedUser() {
        when(userRepository.existsByLoginUsername(userWithoutId.getLogin().getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(userWithoutId.getEmail())).thenReturn(false);
        when(userRepository.save(userWithoutId)).thenReturn(userWithId);

        User savedUser = userService.save(userWithoutId);

        assertEquals(userWithId, savedUser);
        verify(userRepository, times(1)).existsByLoginUsername(anyString());
        verify(userRepository, times(1)).existsByEmail(anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void whenSaveUserWithExistingUserName_thenThrowException() {
        when(userRepository.existsByLoginUsername(userWithoutId.getLogin().getUsername())).thenReturn(true);

        assertThrows(ObjectAlreadyExistsException.class, () -> userService.save(userWithoutId));
        verify(userRepository, times(1)).existsByLoginUsername(anyString());
        verify(userRepository, never()).existsByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void whenSaveUserWithExistingEmail_thenThrowException() {
        when(userRepository.existsByLoginUsername(userWithoutId.getLogin().getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(userWithoutId.getEmail())).thenReturn(true);

        assertThrows(ObjectAlreadyExistsException.class, () -> userService.save(userWithoutId));
        verify(userRepository, times(1)).existsByLoginUsername(anyString());
        verify(userRepository, times(1)).existsByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }


    @Test
    void whenUpdateUser_thenReturnUpdatedUser() {
        when(userRepository.existsById(userWithId.getId())).thenReturn(true);
        when(userRepository.findByLoginUsername(userWithoutId.getLogin().getUsername())).thenReturn(Optional.of(userWithId));
        when(userRepository.findByEmail(userWithoutId.getEmail())).thenReturn(Optional.of(userWithId));

        when(userRepository.save(userWithId)).thenReturn(userWithId);

        userWithId.setFirstName("Updated Test");
        userWithId.setLastName("Updated Test");

        User updatedUser = userService.update(userWithId);
        assertEquals(userWithId, updatedUser);
        verify(userRepository, times(1)).existsById(userWithId.getId());
        verify(userRepository, times(1)).findByLoginUsername(anyString());
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void whenUpdateUserWithSameUsernameButAnotherId_thenThrowException() {
        User existingUserWithAnotherId = TestObjectUtil.getUserWithId();
        existingUserWithAnotherId.setId(2L);

        when(userRepository.existsById(userWithId.getId())).thenReturn(true);
        when(userRepository.findByLoginUsername(userWithoutId.getLogin().getUsername())).thenReturn(Optional.of(existingUserWithAnotherId));
        when(userRepository.findByEmail(userWithoutId.getEmail())).thenReturn(Optional.of(userWithId));

        assertThrows(ObjectAlreadyExistsException.class, () -> userService.update(userWithId));
        verify(userRepository, times(1)).existsById(userWithId.getId());
        verify(userRepository, times(1)).findByLoginUsername(anyString());
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void whenUpdateUserWithSameEmailButAnotherId_thenThrowException() {
        User existingUserWithAnotherId = TestObjectUtil.getUserWithId();
        existingUserWithAnotherId.setId(2L);

        when(userRepository.existsById(userWithId.getId())).thenReturn(true);
        when(userRepository.findByLoginUsername(userWithoutId.getLogin().getUsername())).thenReturn(Optional.of(userWithId));
        when(userRepository.findByEmail(userWithoutId.getEmail())).thenReturn(Optional.of(existingUserWithAnotherId));

        assertThrows(ObjectAlreadyExistsException.class, () -> userService.update(userWithId));
        verify(userRepository, times(1)).existsById(userWithId.getId());
        verify(userRepository, times(1)).findByLoginUsername(anyString());
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void whenUpdateNonExistingUser_thenThroeException() {
        when(userRepository.existsById(userWithId.getId())).thenReturn(false);

        assertThrows(ServiceException.class, () -> userService.update(userWithId));
        verify(userRepository, never()).findByLoginUsername(anyString());
        verify(userRepository, never()).findByEmail(anyString());
        verify(userRepository, never()).save(userWithId);
    }

    @Test
    void whenDeleteUserWithoutOrders_thenUserIsDeleted() {
        Long userId = TestObjectConstant.USER_ID;

        when(userRepository.existsById(userId)).thenReturn(true);
        when(orderRepository.existsOrderByUserId(userId)).thenReturn(false);
        doNothing().when(userRepository).deleteById(userId);

        userService.deleteById(userId);
        verify(orderRepository, times(1)).existsOrderByUserId(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void whenDeleteUserWithOrders_thenThrowException() {
        Long userId = TestObjectConstant.USER_ID;
        when(userRepository.existsById(userId)).thenReturn(true);
        when(orderRepository.existsOrderByUserId(userId)).thenReturn(true);

        assertThrows(ServiceException.class, () -> userService.deleteById(userId));
        verify(orderRepository, times(1)).existsOrderByUserId(userId);
        verify(userRepository, never()).deleteById(userId);
    }

    @Test
    void whenDeleteNonExistingUser_thenThroeException() {
        Long userId = TestObjectConstant.USER_ID;

        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> userService.deleteById(userId));
        verify(orderRepository, never()).existsOrderByUserId(userId);
        verify(userRepository, never()).deleteById(userId);
    }
}
