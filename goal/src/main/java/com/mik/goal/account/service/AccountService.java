package com.mik.goal.account.service;

import com.mik.goal.account.dto.request.*;
import com.mik.goal.account.dto.response.GetMyAccountInfoResponseDto;

import java.util.UUID;

public interface AccountService {
    void signUp(SignUpRequestDto signUpRequestDto);

    GetMyAccountInfoResponseDto getMyAccountInfo(UUID requestAccountAccessId);

    void update(UUID requestAccountAccessId, UpdateAccountInfoRequestDto updateAccountInfoRequestDto);

    void deleteAccount(UUID requestAccountAccessId, DeleteAccountRequestDto deleteAccountRequestDto);

    void deleteCancelAccount(UUID requestAccountAccessId, DeleteCancelAccountRequestDto deleteCancelAccountRequestDto);

    UUID signIn(SignInRequestDto signInRequestDto);

    Boolean duplicateCheck(DuplicateCheckRequestDto duplicateCheckRequestDto);
}
