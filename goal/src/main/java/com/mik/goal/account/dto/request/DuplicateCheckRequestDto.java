package com.mik.goal.account.dto.request;

public record DuplicateCheckRequestDto(
        String username,
        String nickname,
        String email
) {
}
