package com.somecompany.bookstore.service;

import com.somecompany.bookstore.exception.user.UserAlreadyExistsException;
import com.somecompany.bookstore.exception.user.UserNotFoundException;
import com.somecompany.bookstore.exception.user.UserServiceException;
import com.somecompany.bookstore.model.entity.User;
import com.somecompany.bookstore.model.repository.OrderRepository;
import com.somecompany.bookstore.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final MessageSource messageSource;

    public Page<User> getAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> {
            throw new UserNotFoundException(messageSource.getMessage("msg.error.user.find.by.id", null,
                    LocaleContextHolder.getLocale()));
        });
    }

    @Transactional
    public User save(User user) {
        if (userRepository.existsByLoginUsername(user.getLogin().getUsername())) {
            throw new UserAlreadyExistsException(messageSource.getMessage("msg.error.user.exists.username", null,
                    LocaleContextHolder.getLocale()));
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException(messageSource.getMessage("msg.error.user.exists.email", null,
                    LocaleContextHolder.getLocale()));
        }
        return userRepository.save(user);
    }

    @Transactional
    public User update(User user) {
        if (!userRepository.existsById(user.getId())) {
            throw new UserServiceException(messageSource.getMessage("msg.error.user.update", null,
                    LocaleContextHolder.getLocale()));
        }

        Optional<User> existingUserByEmail = userRepository.findByEmail(user.getEmail());
        Optional<User> existingUserByUserName = userRepository.findByLoginUsername(user.getLogin().getUsername());

        if (existingUserByUserName.isPresent() && !Objects.equals(existingUserByUserName.get().getId(), user.getId())) {
            throw new UserAlreadyExistsException(messageSource.getMessage("msg.error.user.exists.username", null,
                    LocaleContextHolder.getLocale()));
        }

        if (existingUserByEmail.isPresent() && !Objects.equals(existingUserByEmail.get().getId(), user.getId())) {
            throw new UserAlreadyExistsException(messageSource.getMessage("msg.error.user.exists.email", null,
                    LocaleContextHolder.getLocale()));
        }
        return userRepository.save(user);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(messageSource.getMessage("msg.error.user.find.by.id", null,
                    LocaleContextHolder.getLocale()));
        }

        if (orderRepository.existsOrderByUserId(id)) {
            throw new UserServiceException(messageSource.getMessage("msg.error.user.delete", null,
                    LocaleContextHolder.getLocale()));
        }

        userRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }
}
