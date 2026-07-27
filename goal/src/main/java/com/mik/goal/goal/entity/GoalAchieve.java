package com.mik.goal.goal.entity;

import com.mik.goal.account.entity.Account;
import com.mik.goal.global.entity.BaseEntityImpl;
import com.mik.goal.global.entity.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GoalAchieve extends BaseEntityImpl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goal_achieve_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "goal_id")
    private Goal goal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "account_id")
    private Account account;

    @Column(nullable = false)
    private LocalDateTime achieveAt;

    /**
     * 목표성취(GoalAchieve) 생성 (USER)
     *
     * @param goal    목표(Goal) 고유번호
     * @param account 계정 고유번호
     * @param role    history
     */
    @Builder
    public GoalAchieve(@NonNull Goal goal, @NonNull Account account, Role role) {
        this.goal = goal;
        this.account = account;
        this.achieveAt = LocalDateTime.now();
        super.createHistory(role);
    }

    /**
     * 목표성취(GoalAchieve) 삭제 (Soft) (USER)
     */
    public void delete() {
        super.softDelete();
    }

    /**
     * 목표성취(GoalAchieve) 삭제 취소 (Soft) (USER)
     */
    public void deleteCancel() {
        super.softDeleteCancel();
    }
}
