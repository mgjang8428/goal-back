package com.mik.goal.account.controller;

import com.mik.goal.account.dto.request.*;
import com.mik.goal.account.dto.response.GetMyAccountInfoResponseDto;
import com.mik.goal.account.service.AccountService;
import com.mik.goal.global.dto.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.UUID;

@Validated
@Slf4j
@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController implements AccountControllerDocs {

    private final AccountService accountService;

    @Override
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> signUp(
            @Valid
            @RequestBody
            SignUpRequestDto signupRequestDto
    ) {
        accountService.signUp(signupRequestDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.ok(null));
    }

    @Override
    @GetMapping
    public ResponseEntity<ApiResponse<GetMyAccountInfoResponseDto>> getMyAccountInfo(
            @Valid @NotNull
            @CookieValue(name = "accountAccessId")
            UUID requestAccountAccessId
    ) {
        GetMyAccountInfoResponseDto getMyAccountInfoResponseDto = accountService.getMyAccountInfo(requestAccountAccessId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.ok(getMyAccountInfoResponseDto));
    }

    @Override
    @PatchMapping
    public ResponseEntity<ApiResponse<Void>> update(
            @Valid @NotNull
            @CookieValue(name = "accountAccessId")
            UUID requestAccountAccessId,
            @Valid
            @RequestBody
            UpdateAccountInfoRequestDto updateAccountInfoRequestDto
    ) {
        accountService.update(requestAccountAccessId, updateAccountInfoRequestDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.ok(null));
    }

    @Override
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteAccount(
            @Valid @NotNull
            @CookieValue(name = "accountAccessId")
            UUID requestAccountAccessId,
            @Valid
            @RequestBody
            DeleteAccountRequestDto deleteAccountRequestDto
    ) {
        accountService.deleteAccount(requestAccountAccessId, deleteAccountRequestDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.ok(null));
    }

    @Override
    @PatchMapping("/delete-cancel")
    public ResponseEntity<ApiResponse<Void>> deleteAccountCancel(
            @Valid @NotNull
            @CookieValue(name = "accountAccessId")
            UUID requestAccountAccessId,
            @Valid
            @RequestBody
            DeleteCancelAccountRequestDto deleteCancelAccountRequestDto
    ) {
        accountService.deleteCancelAccount(requestAccountAccessId, deleteCancelAccountRequestDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.ok(null));
    }

    @Override
    @PostMapping("/auth/signin")
    public ResponseEntity<ApiResponse<Void>> signIn(
            @Valid
            @RequestBody
            SignInRequestDto signInRequestDto
    ) {
        UUID accountAccessId = accountService.signIn(signInRequestDto);
        ResponseCookie accountAccessIdCookie = ResponseCookie.from("accountAccessId", accountAccessId.toString())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofDays(14))
                .sameSite("Strict")
                .build();
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, accountAccessIdCookie.toString())
                .body(ApiResponse.ok(null));
    }

    @Override
    @GetMapping("/duplicate")
    public ResponseEntity<ApiResponse<Boolean>> duplicateCheck(
            @Valid
            @RequestParam
            DuplicateCheckRequestDto duplicateCheckRequestDto
    ) {
        Boolean duplicateResult = accountService.duplicateCheck(duplicateCheckRequestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.ok(duplicateResult));
    }
}
