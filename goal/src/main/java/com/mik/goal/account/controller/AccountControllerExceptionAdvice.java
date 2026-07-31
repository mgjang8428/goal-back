package com.mik.goal.account.controller;

import com.mik.goal.account.exception.*;
import com.mik.goal.global.dto.ApiResponse;
import com.mik.goal.global.dto.ErrorDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@SuppressWarnings("unused")
@RequiredArgsConstructor
@RestControllerAdvice(assignableTypes = {AccountController.class})
public class AccountControllerExceptionAdvice {

    private final MessageSource ms;

    @ExceptionHandler(WrongAccountException.class)
    ResponseEntity<ApiResponse<Void>> wrongAccountException(WrongAccountException e) {
        String message = ms.getMessage("error.account.wrongAccount", null, null);
        ErrorDto errorDto = new ErrorDto(message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.clientError(errorDto));
    }

    @ExceptionHandler(AlreadyDeletedAccountException.class)
    ResponseEntity<ApiResponse<Void>> alreadyDeletedAccountException(AlreadyDeletedAccountException e) {
        String message = ms.getMessage("error.account.alreadyDeleteAccount", null, null);
        ErrorDto errorDto = new ErrorDto(message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.clientError(errorDto));
    }

    @ExceptionHandler(WrongPasswordException.class)
    ResponseEntity<ApiResponse<Void>> wrongPasswordException(WrongPasswordException e) {
        String message = ms.getMessage("error.account.wrongPassword", null, null);
        ErrorDto errorDto = new ErrorDto(message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.clientError(errorDto));
    }

    @ExceptionHandler(CreateFailedAccountAccessIdException.class)
    ResponseEntity<ApiResponse<Void>> createFailedAccountAccessIdException(CreateFailedAccountAccessIdException e) {
        String message = ms.getMessage("error.account.createdFailedAccountAccessId", null, null);
        ErrorDto errorDto = new ErrorDto(message);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.serverError(errorDto));
    }

    @ExceptionHandler(DuplicateValueCheckingWrongException.class)
    ResponseEntity<ApiResponse<Void>> duplicateValueCheckingWrongException(DuplicateValueCheckingWrongException e) {
        String message = ms.getMessage("error.account.duplicateValueCheckingWrong", null, null);
        ErrorDto errorDto = new ErrorDto(message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.clientError(errorDto));
    }
}
