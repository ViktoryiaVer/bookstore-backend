package com.somecompany.bookstore.constant;

public class QueryConstant {
    public static final String SQL_CHECK_IF_AUTHOR_HAS_BOOK =
            "SELECT CASE WHEN EXISTS (SELECT a.id " +
            "FROM authors a " +
            "RIGHT JOIN books_authors ba ON a.id = ba.author_id " +
            "WHERE a.id = ? AND a.id IS NOT NULL)" +
            "THEN CAST(1 AS BIT) " +
            "ELSE CAST(0 AS BIT) END";
}
