package com.mik.goal.account.utils;

public interface PasswordEncrypt {
    String passwordEncrypt(String rawPassword);

    boolean checkPassword(String rawPassword, String encryptedPassword);
}
