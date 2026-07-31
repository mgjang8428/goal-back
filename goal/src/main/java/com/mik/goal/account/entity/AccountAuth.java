package com.mik.goal.account.entity;

import com.mik.goal.global.entity.BaseEntityImpl;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Objects;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class AccountAuth extends BaseEntityImpl {
    @Id
    @Column(name = "account_auth_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(nullable = false, name = "account_id")
    private Account account;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(nullable = false, unique = true, length = 36)
    private UUID accountAccessId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private AccountRole role;

    @Column(nullable = false, unique = true, length = 30)
    private String username;

    @Column(nullable = false, length = 60)
    private String password;

    @Builder
    public AccountAuth(
            @NonNull Account account,
            @NonNull String username,
            @NonNull String password
    ) {
        this.account = account;
        this.accountAccessId = UUID.randomUUID();
        this.role = AccountRole.ROLE_USER;
        this.username = username;
        this.password = password;
    }

    public boolean isAccountAuthDeleted() {
        return !Objects.isNull(getDeletedAt());
    }

    @FunctionalInterface
    public interface EncryptPasswordLogic {
        String encrypt();
    }

    /**
     * 비밀번호 변경
     *
     * @param logic 비밀번호 암호화 람다
     */
    public void changePassword(EncryptPasswordLogic logic) {
        this.password = logic.encrypt();
    }

    /**
     * 계정 접근 ID 변경
     */
    public void changeAccountAccessId() {
        this.accountAccessId = UUID.randomUUID();
    }

    /**
     * 계정 삭제 (soft)
     */
    public void delete() {
        this.softDelete();
    }

    /**
     * 계정 삭제 복구 (soft)
     */
    public void deleteCancel() {
        this.softDeleteCancel();
    }
}
