package com.somecompany.bookstore.model.repository;

import com.somecompany.bookstore.model.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {
    @Override
    @EntityGraph("Person.withLogin")
    List<User> findAll();
}
