package com.somecompany.bookstore.service.api;

import com.somecompany.bookstore.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<User> getAll(Pageable pageable);

    User getById(Long id);

    User save(User user);

    User update(User user);

    void deleteById(Long id);
}
