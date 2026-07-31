package com.mik.goal.account.controller;

import com.mik.goal.account.dto.request.*;
import com.mik.goal.account.dto.response.GetMyAccountInfoResponseDto;
import com.mik.goal.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@SuppressWarnings("unused")
@Tag(name = "계정 API")
public interface AccountControllerDocs {

    @Operation(summary = "계정 가입", description = "유저가 계정에 가입합니다.")
    ResponseEntity<ApiResponse<Void>> signUp(
            @Valid
            SignUpRequestDto signUpRequestDto
    );

    @Operation(summary = "내 정보 조회", description = "유저가 자신의 정보를 조회합니다.")
    ResponseEntity<ApiResponse<GetMyAccountInfoResponseDto>> getMyAccountInfo(
            @Valid @NotNull
            UUID requestAccountAccessId
    );

    @Operation(summary = "계정 정보 수정", description = "유저가 자신의 정보를 수정합니다.")
    ResponseEntity<ApiResponse<Void>> update(
            @Valid @NotNull
            UUID requestAccountAccessId,
            @Valid
            UpdateAccountInfoRequestDto updateAccountInfoRequestDto
    );

    @Operation(summary = "계정 삭제", description = "유저가 서비스에서 탈퇴합니다.")
    ResponseEntity<ApiResponse<Void>> deleteAccount(
            @Valid @NotNull
            UUID requestAccountAccessId,
            @Valid
            DeleteAccountRequestDto deleteAccountRequestDto
    );

    @Operation(summary = "계정 삭제 복구", description = "유저가 서비스에서 탈퇴한 계정을 복구합니다.")
    ResponseEntity<ApiResponse<Void>> deleteAccountCancel(
            @Valid @NotNull
            UUID requestAccountAccessId,
            @Valid
            DeleteCancelAccountRequestDto deleteCancelAccountRequestDto
    );

    @Operation(summary = "계정 로그인", description = "유저가 서비스에 로그인합니다.")
    ResponseEntity<ApiResponse<Void>> signIn(
            @Valid
            SignInRequestDto signInRequestDto
    );

    @Operation(summary = "중복값 확인", description = "중복값을 확인합니다.")
    ResponseEntity<ApiResponse<Boolean>> duplicateCheck(
            @Valid
            DuplicateCheckRequestDto duplicateCheckRequestDto
    );
}
