package com.somecompany.bookstore.constant;

public class RegExpConstant {
    public static final String NAME = "^[A-Za-z-А-Яа-я ]+";
    public static final String USERNAME = "^[A-Za-z]\\w{5,29}$";
    public static final String PASSWORD = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{8,}$";
    public static final String PHONE = "\\+[0-9]{10,}";
}
