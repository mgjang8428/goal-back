package com.mik.goal.global.dto;

import jakarta.annotation.Nullable;

import java.time.LocalDateTime;

public record ApiResponse<T>(
        boolean isSuccess,
        String code,
        String message,
        LocalDateTime timestamp,
        @Nullable
        T data,
        @Nullable
        ErrorDto error
) {
    public static <T> ApiResponse<T> ok(@Nullable final T data) {
        return new ApiResponse<>(
                true,
                "200",
                "성공",
                LocalDateTime.now(),
                data,
                null
        );
    }

    public static <T> ApiResponse<T> clientError(ErrorDto error) {
        return new ApiResponse<>(
                false,
                "400",
                "클라이언트 에러",
                LocalDateTime.now(),
                null,
                error
        );
    }

    public static <T> ApiResponse<T> serverError(ErrorDto error) {
        return new ApiResponse<>(
                false,
                "500",
                "서버 에러",
                LocalDateTime.now(),
                null,
                error
        );
    }

}
