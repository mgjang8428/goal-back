package com.mik.goal.mission.entity;

import com.mik.goal.account.entity.Account;
import com.mik.goal.global.entity.BaseEntityImpl;
import com.mik.goal.global.entity.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MissionAchieve extends BaseEntityImpl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mission_achieve_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "mission_id")
    private Mission mission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "account_id")
    private Account account;

    @Column(nullable = false)
    private LocalDateTime achieveAt;

    /**
     * 미션 성취(MissionAchieve) 생성
     *
     * @param mission 미션
     * @param account 계정
     * @param role    history
     */
    @Builder
    public MissionAchieve(@NonNull Mission mission, @NonNull Account account, Role role) {
        this.mission = mission;
        this.account = account;
        this.achieveAt = LocalDateTime.now();
        super.createHistory(role);
    }

    /**
     * 미션 삭제 (Soft) (USER)
     */
    public void delete() {
        super.softDelete();
    }

    /**
     * 미션 삭제 취소 (Soft) (USER)
     */
    public void deleteCancel() {
        super.softDeleteCancel();
    }
}
