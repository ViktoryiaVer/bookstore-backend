package com.somecompany.bookstore.model.repository;

import com.somecompany.bookstore.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AbstractRepositoryImpl implements AbstractRepository {
    private final MessageSource messageSource;

    public <T extends JpaRepository<V, Long>, V> V findByIdOrException(T repository, Long id) {
        return repository.findById(id).orElseThrow(() ->
                new NotFoundException(messageSource.getMessage("msg.error.object.find.by.id", null,
                        LocaleContextHolder.getLocale())));
    }
}
