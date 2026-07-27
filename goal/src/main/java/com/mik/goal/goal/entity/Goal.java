package com.mik.goal.goal.entity;

import com.mik.goal.account.entity.Account;
import com.mik.goal.global.entity.BaseEntityImpl;
import com.mik.goal.global.entity.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.List;

/**
 * 목표 Entity
 */

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Goal extends BaseEntityImpl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goal_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "account_id")
    private Account account;

    @Column(nullable = false, length = 200)
    private String title;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(length = 3000)
    private String content;

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;

    @OneToMany(mappedBy = "goal")
    private List<GoalRepeat> goalRepeats;

    @OneToMany(mappedBy = "goal")
    private List<GoalAchieve> goalAchieves;

    /**
     * 목표 생성
     *
     * @param account   목표 생성 계정 고유번호
     * @param title     목표 제목
     * @param content   목표 내용
     * @param startDate 목표 시작일
     * @param endDate   목표 끝일
     * @param role      history
     */
    @Builder
    public Goal(
            @NonNull Account account,
            @NonNull String title,
            String content,
            @NonNull LocalDate startDate,
            LocalDate endDate,
            Role role
    ) {
        validateStartDateEndDate(startDate, endDate);
        this.account = account;
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        super.createHistory(role);
    }

    /**
     * 목표(Goal) 제목 변경 (USER)
     *
     * @param title 변경할 목표 제목
     */
    public void updateTitle(@NonNull String title) {
        this.title = title;
        super.updateHistory();
    }

    /**
     * 목표(Goal) 내용 변경 (USER)
     *
     * @param content 변경할 목표 내용
     */
    public void updateContent(String content) {
        this.content = content;
        super.updateHistory();
    }

    /**
     * 목표(Goal) 시작일 변경 (USER)
     *
     * @param startDate 변경할 목표 시작일
     */
    public void updateStartDate(@NonNull LocalDate startDate) {
        validateStartDateEndDate(startDate, this.endDate);
        this.startDate = startDate;
        super.updateHistory();
    }

    /**
     * 목표(Goal) 끝일 변경 (USER)
     *
     * @param endDate 변경할 목표 끝일
     */
    public void updateEndDate(LocalDate endDate) {
        validateStartDateEndDate(this.startDate, endDate);
        this.endDate = endDate;
        super.updateHistory();
    }

    /**
     * 목표 삭제 (Soft) (USER)
     */
    public void delete() {
        super.softDelete();
    }

    /**
     * 목표 삭제 취소 (Soft) (USER)
     */
    public void deleteCancel() {
        super.softDeleteCancel();
    }

    /**
     * 시작일 끝일 조건(endDate는 startDate 이후 날짜) 검증
     *
     * @param startDate 시작일
     * @param endDate   끝일
     */
    private void validateStartDateEndDate(@NonNull LocalDate startDate, LocalDate endDate) {
        if (endDate == null) return;
        if (!endDate.isAfter(startDate)) {
            throw new IllegalArgumentException("endDate는 startDate보다 이후 날짜여야 합니다.");
        }
    }
}
