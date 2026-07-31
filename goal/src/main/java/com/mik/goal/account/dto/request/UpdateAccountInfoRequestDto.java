package com.mik.goal.account.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateAccountInfoRequestDto(
        // password
        @Size(max = 30)
        String currentPassword,
        @Size(max = 30)
        String newPassword,
        // nickname
        @Size(max = 30)
        String newNickname,
        //email
        @Email
        @Size(max = 60)
        String newEmail
) {
}
