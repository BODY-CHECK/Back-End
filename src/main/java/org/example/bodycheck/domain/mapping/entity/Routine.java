package org.example.bodycheck.domain.mapping.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.bodycheck.common.entity.BaseEntity;
import org.example.bodycheck.domain.exercise.entity.Exercise;
import org.example.bodycheck.domain.member.entity.Member;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "routine")
public class Routine extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer weekId; // 1 ~ 7 (월 ~ 금)

    private Integer routineIdx; // 1 ~ 3

    @Column(name = "routine_check")
    private boolean routineCheck = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    public void setMember(Member member) {
        if(this.member != null)
            member.getRoutineList().remove(this);
        this.member = member;
        member.getRoutineList().add(this);
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public void setRoutineCheck(Boolean routineCheck) { // setter 메서드 추가
        this.routineCheck = routineCheck;
    }
}
