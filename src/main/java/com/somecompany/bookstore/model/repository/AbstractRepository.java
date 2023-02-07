package com.somecompany.bookstore.model.repository;

import com.somecompany.bookstore.constant.DefaultExceptionMessageConstant;
import com.somecompany.bookstore.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AbstractRepository<T> extends JpaRepository<T, Long> {
    default T findByIdOrException(Long id) {
        return findById(id).orElseThrow(() ->
                new NotFoundException(DefaultExceptionMessageConstant.OBJECT_NOT_FOUND));
    }
}
