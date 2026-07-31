package com.mik.goal.account.entity;

import com.mik.goal.global.entity.BaseEntityImpl;
import com.mik.goal.goal.entity.Goal;
import com.mik.goal.goal.entity.GoalAchieve;
import com.mik.goal.mission.entity.MissionAchieve;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 계정 Entity
 */

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseEntityImpl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String nickname;

    @Column(nullable = false, unique = true, length = 60)
    private String email;

    @OneToMany(mappedBy = "account")
    private final List<Goal> goals = new ArrayList<>();

    @OneToMany(mappedBy = "account")
    private final List<GoalAchieve> goalAchieves = new ArrayList<>();

    @OneToMany(mappedBy = "account")
    private final List<MissionAchieve> missionAchieves = new ArrayList<>();

    /**
     * 계정 생성:
     * id, accountAccessId 자동 생성, 생성 주체 지정
     *
     * @param nickname 생성할 계정의 nickname 값 (별명)
     * @param email    생성할 계정의 이메일 값
     */
    @Builder
    public Account(
            @NonNull String nickname,
            @NonNull String email
    ) {
        this.nickname = nickname;
        this.email = email;
    }


    /**
     * 계정 nickname 변경 (USER)
     *
     * @param nickname 변경할 계정 nickname(별명) 값
     */
    public void updateNickname(@NonNull String nickname) {
        this.nickname = nickname;
    }

    /**
     * 계정 email 변경 (USER)
     *
     * @param email 변경할 계정 email 값
     */
    public void updateEmail(@NonNull String email) {
        this.email = email;
    }

    /**
     * 계정 삭제 (soft) (USER)
     */
    public void delete() {
        super.softDelete();
    }

    /**
     * 계정 삭제 취소 (soft) (USER)
     */
    public void deleteCancel() {
        super.softDeleteCancel();
    }

    /**
     * 계정 탈퇴 여부 확인
     *
     * @return 탈퇴 여부
     */
    public boolean isAccountDeleted() {
        return !Objects.isNull(getDeletedAt());
    }
}
