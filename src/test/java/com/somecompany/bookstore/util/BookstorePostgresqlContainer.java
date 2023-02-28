package com.somecompany.bookstore.util;

import org.testcontainers.containers.PostgreSQLContainer;

public class BookstorePostgresqlContainer extends PostgreSQLContainer<BookstorePostgresqlContainer> {

    private static final String IMAGE_VERSION = "postgres:15.2";
    private static BookstorePostgresqlContainer container;

    private BookstorePostgresqlContainer() {
        super(IMAGE_VERSION);
    }

    public static BookstorePostgresqlContainer getInstance() {
        if (container == null) {
            container = new BookstorePostgresqlContainer();
        }
        return container;
    }
}
