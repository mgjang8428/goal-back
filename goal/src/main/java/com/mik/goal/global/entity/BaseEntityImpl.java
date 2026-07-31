package com.mik.goal.global.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntityImpl {


    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    /**
     * soft 삭제: deleteAt, deleteBy, updateAt, updateBy 기록 (USER)
     */
    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }

    /**
     * soft 삭제 취소: deleteAt, deleteBy 내용 삭제 / updateAt, updateBy 기록 (USER)
     */
    public void softDeleteCancel() {
        this.deletedAt = null;
    }
}
