package com.mik.goal.account.utils;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncryptImpl implements PasswordEncrypt {
    @Override
    public String passwordEncrypt(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }

    @Override
    public boolean checkPassword(String rawPassword, String encryptedPassword) {
        return !BCrypt.checkpw(rawPassword, encryptedPassword);
    }
}
