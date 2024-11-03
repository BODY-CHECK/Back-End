package org.example.bodycheck.domain.solution.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.bodycheck.common.entity.BaseEntity;
import org.example.bodycheck.domain.criteria.entity.Criteria;
import org.example.bodycheck.domain.exercise.entity.Exercise;
import org.example.bodycheck.domain.mapping.entity.SolutionCriteria;
import org.example.bodycheck.domain.member.entity.Member;
import org.example.bodycheck.domain.solutionVideo.entity.SolutionVideo;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Solution extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @OneToMany(mappedBy = "solution", cascade = CascadeType.ALL)
    private List<Criteria> criteriaList = new ArrayList<>();

//    @OneToMany(mappedBy = "solution", cascade = CascadeType.ALL)
//    private List<SolutionCriteria> solutionCriteriaList = new ArrayList<>();

    @OneToMany(mappedBy = "solution", cascade = CascadeType.ALL) // 원래는 OneToOne
    private List<SolutionVideo> solutionVideoList = new ArrayList<>();

    public void setMember(Member member) {
        if(this.member != null)
            member.getSolutionList().remove(this);
        this.member = member;
        member.getSolutionList().add(this);
    }

    public void setExercise(Exercise exercise) {
        if(this.exercise != null)
            exercise.getSolutionList().remove(this);
        this.exercise = exercise;
        exercise.getSolutionList().add(this);
    }
}
