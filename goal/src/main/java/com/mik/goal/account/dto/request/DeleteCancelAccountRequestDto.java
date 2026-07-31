package com.mik.goal.account.dto.request;

import jakarta.validation.constraints.NotNull;

public record DeleteCancelAccountRequestDto(
        @NotNull
        String password
) {
}
