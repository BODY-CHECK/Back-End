package org.example.bodycheck.domain.solutioncriteria.entity;

import org.example.bodycheck.common.entity.BaseEntity;
import org.example.bodycheck.domain.criteria.entity.Criteria;
import org.example.bodycheck.domain.solution.entity.Solution;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

	public void mappingSolutionAndCriteria(Solution solution, Criteria criteria) {
		if (this.solution != null) {
			solution.getSolutionCriteriaList().remove(this);
		}
		this.solution = solution;

		if (this.criteria != null) {
			criteria.getSolutionCriteriaList().remove(this);
		}
		this.criteria = criteria;
	}
}
