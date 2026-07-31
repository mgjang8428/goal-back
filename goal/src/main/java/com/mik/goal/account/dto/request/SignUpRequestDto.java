package com.mik.goal.account.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SignUpRequestDto(
        @NotNull
        @Size(min = 2, max = 30)
        String username,

        @NotNull
        @Size(min = 2, max = 30)
        String password,

        @NotNull
        @Size(min = 2, max = 30)
        String nickname,

        @NotNull
        @Size(min = 2, max = 60)
        String email
) {
}
