package com.mik.goal.account.dto.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

public record GetMyAccountInfoResponseDto(
        @NotNull
        @Size(min = 2, max = 30)
        String username,
        @NotNull
        @Size(min = 2, max = 30)
        String nickname,
        @NotNull
        @Email
        @Size(min = 2, max = 60)
        String email
) {
    @Builder
    public GetMyAccountInfoResponseDto(String username, String nickname, String email) {
        this.username = username;
        this.nickname = nickname;
        this.email = email;
    }
}
