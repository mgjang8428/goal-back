package com.mik.goal.mission.entity;

import com.mik.goal.global.entity.BaseEntityImpl;
import com.mik.goal.global.entity.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mission extends BaseEntityImpl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mission_id")
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 3000)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String content;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.CHAR)
    private MissionType type;

    @OneToMany(mappedBy = "mission")
    private List<MissionAchieve> missionAchieves;

    /**
     * 미션 생성 (USER)
     *
     * @param title   미션 제목
     * @param content 미션 내용
     * @param type    미션 종류
     * @param role    history
     */
    @Builder
    public Mission(@NonNull String title, String content, @NonNull MissionType type, Role role) {
        this.title = title;
        this.content = content;
        this.type = type;
        super.createHistory(role);
    }

    /**
     * 미션 제목 수정 (USER)
     *
     * @param title 변경할 미션 제목
     */
    public void updateMissionTitle(@NonNull String title) {
        this.title = title;
        super.updateHistory();
    }

    /**
     * 미션 내용 수정 (USER)
     *
     * @param content 변경할 미션 내용
     */
    public void updateMissionContent(String content) {
        this.content = content;
        super.updateHistory();
    }

    /**
     * 미션 종류 수정 (USER)
     *
     * @param type 변경할 미션 종류
     */
    public void updateMissionType(@NonNull MissionType type) {
        this.type = type;
        super.updateHistory();
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
