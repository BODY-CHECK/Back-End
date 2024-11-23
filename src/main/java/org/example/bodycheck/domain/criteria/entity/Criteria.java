package org.example.bodycheck.domain.criteria.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.bodycheck.common.entity.BaseEntity;
import org.example.bodycheck.domain.exercise.entity.Exercise;
import org.example.bodycheck.domain.mapping.entity.SolutionCriteria;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Criteria extends BaseEntity {
    // 평가 기준 테이블

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer criteriaIdx;

    private String criteriaName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @OneToMany(mappedBy = "criteria", cascade = CascadeType.ALL)
    private List<SolutionCriteria> solutionCriteriaList = new ArrayList<>();

    public void setExercise(Exercise exercise) {
        if(this.exercise != null)
            exercise.getCriteriaList().remove(this);
        this.exercise = exercise;
        exercise.getCriteriaList().add(this);
    }
}
