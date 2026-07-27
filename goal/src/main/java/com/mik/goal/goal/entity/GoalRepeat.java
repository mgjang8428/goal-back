package com.mik.goal.goal.entity;

import com.mik.goal.global.entity.BaseEntityImpl;
import com.mik.goal.global.entity.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity(name = "goal_repeat")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GoalRepeat extends BaseEntityImpl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goal_repeat_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "goal_id")
    private Goal goal;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.CHAR)
    private GoalRepeatType type;

    @Column(length = 5)
    @JdbcTypeCode(SqlTypes.CHAR)
    private String repeatDate;

    private Integer repeatDayNum;

    /**
     * 목표반복(GoalRepeat) 생성 (USER)
     *
     * @param goal         목표 정보
     * @param type         반복 타입
     * @param repeatDate   연간 반복 일자
     * @param repeatDayNum 월간 반복 일자
     * @param role         history
     */
    @Builder
    public GoalRepeat(@NonNull Goal goal, @NonNull GoalRepeatType type, String repeatDate, Integer repeatDayNum, Role role) {
        this.goal = goal;
        this.type = type;
        this.repeatDate = repeatDate;
        this.repeatDayNum = repeatDayNum;
        super.createHistory(role);
    }

    /**
     * 연간 반복 정보 수정 (USER)
     *
     * @param repeatDate 변경할 연간 반복 정보
     */
    public void updateRepeatDate(String repeatDate) {
        this.repeatDate = repeatDate;
        super.updateHistory();
    }

    /**
     * 월간 반복 정보 수정 (USER)
     *
     * @param repeatDayNum 변경할 월간 반복 정보
     */
    public void updateRepeatDayNum(Integer repeatDayNum) {
        this.repeatDayNum = repeatDayNum;
        super.updateHistory();
    }

    /**
     * 반복 정보 삭제 (Soft) (USER)
     */
    public void delete() {
        super.softDelete();
    }

    /**
     * 반복 정보 삭제 취소 (Soft) (USER)
     */
    public void cancelDelete() {
        super.softDeleteCancel();
    }
}
