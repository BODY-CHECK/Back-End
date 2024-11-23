package org.example.bodycheck.domain.mapping.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.bodycheck.common.entity.BaseEntity;
import org.example.bodycheck.domain.criteria.entity.Criteria;
import org.example.bodycheck.domain.solution.entity.Solution;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SolutionCriteria extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solution_id")
    private Solution solution;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "criteria_id")
    private Criteria criteria;

    private Integer score;

    public void setSolution(Solution solution) {
        if(this.solution != null)
            solution.getSolutionCriteriaList().remove(this);
        this.solution = solution;
    }

    public void setCriteria(Criteria criteria) {
        if(this.criteria != null)
            criteria.getSolutionCriteriaList().remove(this);
        this.criteria = criteria;
    }
}
