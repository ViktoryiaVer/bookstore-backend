package com.somecompany.bookstore.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AbstractRepository {
    <T extends JpaRepository<V, Long>, V> V findByIdOrException(T repository, Long id);
}
