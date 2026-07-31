package com.mik.goal.account.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SignInRequestDto(
        @NotNull
        @Size(min = 2, max = 30)
        String username,

        @NotNull
        @Size(min = 2, max = 30)
        String password
) {
}
