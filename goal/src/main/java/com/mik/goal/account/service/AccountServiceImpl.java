package com.mik.goal.account.service;

import com.mik.goal.account.dto.request.*;
import com.mik.goal.account.dto.response.GetMyAccountInfoResponseDto;
import com.mik.goal.account.entity.Account;
import com.mik.goal.account.entity.AccountAuth;
import com.mik.goal.account.exception.*;
import com.mik.goal.account.repository.AccountAuthJpaRepository;
import com.mik.goal.account.repository.AccountJpaRepository;
import com.mik.goal.account.utils.PasswordEncrypt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountJpaRepository accountJpaRepository;
    private final AccountAuthJpaRepository accountAuthJpaRepository;
    private final PasswordEncrypt passwordEncrypt;

    // TODO: 중복 확인 api 구현 필요!!
    // ID, NICKNAME, EMAIL

    /**
     * 계정 가입
     *
     * @param signUpDto 가입 요청 DTO
     */
    @Override
    @Transactional
    public void signUp(SignUpRequestDto signUpDto) {
        // 중복 ID 여부 확인
        accountAuthJpaRepository.findByUsername(signUpDto.username())
                .ifPresent(m -> {
                    throw new DuplicateValueCheckingWrongException();
                });
        // 중복 이름 여부 확인
        accountJpaRepository.findByNickname(signUpDto.nickname())
                .ifPresent(m -> {
                    throw new DuplicateValueCheckingWrongException();
                });
        // 중복 이메일 여부 확인
        accountJpaRepository.findByEmail(signUpDto.email())
                .ifPresent(m -> {
                    throw new DuplicateValueCheckingWrongException();
                });

        // Account 생성
        Account createdAccount = Account.builder()
                .nickname(signUpDto.nickname())
                .email(signUpDto.email())
                .build();

        // account 저장
        Account savedAccount = accountJpaRepository.save(createdAccount);

        // AccountAuth 생성
        AccountAuth createdAccountAuth = AccountAuth.builder()
                .account(savedAccount)
                .username(signUpDto.username())
                .password(passwordEncrypt.passwordEncrypt(signUpDto.password()))
                .build();

        // AccountAccessId 중복 여부 확인 및 조치
        AccountAuth checkedAccountAuth = accountAccessIdDuplicateCheck(createdAccountAuth);

        // AccountAuth 저장
        accountAuthJpaRepository.save(checkedAccountAuth);
    }

    /**
     * 계정 접근 ID 중복 확인 및 조치
     *
     * @param accountAuth 계정인증 Entity
     * @return 중복 여부 확인된 AccountAuth
     */
    private AccountAuth accountAccessIdDuplicateCheck(AccountAuth accountAuth) {
        int retryCount = 0;
        while (true) {
            if (accountAuthJpaRepository.findByAccountAccessId(accountAuth.getAccountAccessId()).isPresent()) {
                accountAuth.changeAccountAccessId();
            } else return accountAuth;
            if (retryCount >= 5) throw new CreateFailedAccountAccessIdException();
            retryCount++;
        }
    }

    /**
     * 내 계정 정보 보기 (다른 사용자 사용 X)
     *
     * @param requestAccountAccessId 계정 접근 ID
     * @return 내 계정 정보 응답 DTO
     */
    @Transactional(readOnly = true)
    @Override
    public GetMyAccountInfoResponseDto getMyAccountInfo(UUID requestAccountAccessId) {
        AccountAuth accountAuth = accountAuthJpaRepository.findByAccountAccessId(requestAccountAccessId)
                .orElseThrow(WrongAccountException::new);

        Account account = accountAuth.getAccount();

        // 탈퇴 여부 확인
        if (account.isAccountDeleted()) {
            throw new AlreadyDeletedAccountException();
        }

        return GetMyAccountInfoResponseDto.builder()
                .username(accountAuth.getUsername())
                .nickname(account.getNickname())
                .email(account.getEmail())
                .build();
    }

    /**
     * 계정 정보 변경
     *
     * @param requestAccountAccessId 계정 접근 고유 ID
     * @param updateDto              계정 정보 변경 Request DTO
     */
    @Override
    @Transactional
    public void update(
            UUID requestAccountAccessId,
            UpdateAccountInfoRequestDto updateDto
    ) {
        AccountAuth accountAuth = accountAuthJpaRepository.findByAccountAccessId(requestAccountAccessId)
                .orElseThrow(WrongAccountException::new);

        Account account = accountAuth.getAccount();

        String currentPassword = updateDto.currentPassword();
        String newPassword = updateDto.newPassword();
        String newNickname = updateDto.newNickname();
        String newEmail = updateDto.newEmail();

        // 비밀번호 변경
        if (StringUtils.hasText(newPassword)) {
            // 기존 비밀번호 확인
            if (passwordEncrypt.checkPassword(currentPassword, accountAuth.getPassword())) {
                throw new WrongPasswordException();
            }
            // 비밀번호 변경
            accountAuth.changePassword(() -> passwordEncrypt.passwordEncrypt(newPassword));
        }

        // 이름 변경
        if (StringUtils.hasText(newNickname)) {
            account.updateNickname(newNickname);
        }

        // 이메일 변경
        if (StringUtils.hasText(newEmail)) {
            account.updateEmail(newEmail);
        }
    }

    /**
     * 계정 삭제
     *
     * @param requestAccountAccessId  계정 접근 ID
     * @param deleteAccountRequestDto 계정 삭제 요청 DTO
     */
    @Override
    @Transactional
    public void deleteAccount(
            UUID requestAccountAccessId,
            DeleteAccountRequestDto deleteAccountRequestDto
    ) {
        AccountAuth accountAuth = accountAuthJpaRepository.findByAccountAccessId(requestAccountAccessId)
                .orElseThrow(WrongAccountException::new);
        Account account = accountAuth.getAccount();
        String requestPassword = deleteAccountRequestDto.password();

        // 비밀번호 일치 확인
        if (passwordEncrypt.checkPassword(requestPassword, accountAuth.getPassword())) {
            throw new WrongPasswordException();
        }

        // 계정 삭제 (soft)
        account.delete();
        accountAuth.delete();
    }

    /**
     * 계정 탈퇴 취소
     *
     * @param requestAccountAccessId        계정 접근 ID
     * @param deleteCancelAccountRequestDto 계정 탈퇴 취소 요청 DTO
     */
    @Override
    @Transactional
    public void deleteCancelAccount(
            UUID requestAccountAccessId,
            DeleteCancelAccountRequestDto deleteCancelAccountRequestDto
    ) {
        AccountAuth accountAuth = accountAuthJpaRepository.findByAccountAccessId(requestAccountAccessId)
                .orElseThrow(WrongAccountException::new);
        Account account = accountAuth.getAccount();
        String requestPassword = deleteCancelAccountRequestDto.password();

        // 비밀번호 일치 여부 확인
        if (passwordEncrypt.checkPassword(requestPassword, accountAuth.getPassword())) {
            throw new WrongPasswordException();
        }

        // 계정 삭제 취소 (soft)
        account.deleteCancel();
        accountAuth.deleteCancel();
    }

    /**
     * 계정 로그인
     *
     * @param signInRequestDto 계정 로그인 요청 DTO
     * @return 계정 접근 ID
     */
    @Override
    @Transactional(readOnly = true)
    public UUID signIn(SignInRequestDto signInRequestDto) {
        String requestUsername = signInRequestDto.username();
        String requestPassword = signInRequestDto.password();

        // AccountAuth username 조회
        AccountAuth accountAuth = accountAuthJpaRepository.findByUsername(requestUsername)
                .orElseThrow(WrongAccountException::new);

        Account account = accountAuth.getAccount();

        // 탈퇴 여부 확인
        if (account.isAccountDeleted() || accountAuth.isAccountAuthDeleted()) {
            throw new AlreadyDeletedAccountException();
        }

        // 비밀번호 일치 여부 확인
        if (passwordEncrypt.checkPassword(requestPassword, accountAuth.getPassword())) {
            throw new WrongAccountException();
        }

        // 계정의 accountAccessId 리턴
        return accountAuth.getAccountAccessId();
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean duplicateCheck(DuplicateCheckRequestDto duplicateCheckRequest) {
        // username(ID) 중복 확인
        if (StringUtils.hasText(duplicateCheckRequest.username())) {
            if (accountAuthJpaRepository.findByUsername(duplicateCheckRequest.username()).isPresent()) return true;
        }
        // nickname 중복 확인
        if (StringUtils.hasText(duplicateCheckRequest.nickname())) {
            if (accountJpaRepository.findByNickname(duplicateCheckRequest.nickname()).isPresent()) return true;
        }
        // email 중복 확인
        if (StringUtils.hasText(duplicateCheckRequest.email())) {
            if (accountJpaRepository.findByEmail(duplicateCheckRequest.email()).isPresent()) return true;

        }
        return false;
    }
}
