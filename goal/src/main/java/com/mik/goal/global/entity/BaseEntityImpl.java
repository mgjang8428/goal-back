package com.mik.goal.global.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class BaseEntityImpl implements BaseEntity {

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private Role createdBy;

    private LocalDateTime updatedAt;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private Role updatedBy;

    private LocalDateTime deletedAt;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private Role deletedBy;

    /**
     * createAt, createBy 기록 (ROLE 지정)
     */
    @Override
    public void createHistory(Role role) {
        create(role == null ? Role.USER : role);
    }

    /**
     * updateAt, updateBy 기록 (USER)
     */
    @Override
    public void updateHistory() {
        update(Role.USER);
    }

    /**
     * updateAt, updateBy 기록 (ROLE 지정)
     */
    @Override
    public void updateHistory(Role role) {
        update(role);
    }

    /**
     * soft 삭제: deleteAt, deleteBy, updateAt, updateBy 기록 (USER)
     */
    @Override
    public void softDelete() {
        delete(Role.USER);
        update(Role.USER);
    }

    /**
     * soft 삭제: deleteAt, deleteBy, updateAt, updateBy 기록 (ROLE 지정)
     */
    @Override
    public void softDelete(Role role) {
        delete(role);
        update(role);
    }

    /**
     * soft 삭제 취소: deleteAt, deleteBy 내용 삭제 / updateAt, updateBy 기록 (USER)
     */
    @Override
    public void softDeleteCancel() {
        deleteCancel();
        update(Role.USER);
    }

    /**
     * soft 삭제 취소: deleteAt, deleteBy 내용 삭제 / updateAt, updateBy 기록 (ROLE 지정)
     */
    @Override
    public void softDeleteCancel(Role role) {
        deleteCancel();
        update(role);
    }

    private void create(Role role) {
        this.createdAt = LocalDateTime.now();
        this.createdBy = role;
    }

    private void update(Role role) {
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = role;
    }

    private void delete(Role role) {
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = role;
    }

    private void deleteCancel() {
        this.deletedAt = null;
        this.deletedBy = null;
    }
}
