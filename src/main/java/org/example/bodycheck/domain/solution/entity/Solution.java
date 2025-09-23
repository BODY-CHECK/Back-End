package org.example.bodycheck.domain.solution.entity;

import java.util.ArrayList;
import java.util.List;

import org.example.bodycheck.common.entity.BaseEntity;
import org.example.bodycheck.domain.exercise.entity.Exercise;
import org.example.bodycheck.domain.member.entity.Member;
import org.example.bodycheck.domain.solutioncriteria.entity.SolutionCriteria;
import org.example.bodycheck.domain.solutionvideo.entity.SolutionVideo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
	private List<SolutionCriteria> solutionCriteriaList = new ArrayList<>();

	@OneToMany(mappedBy = "solution", cascade = CascadeType.ALL) // 원래는 OneToOne
	private List<SolutionVideo> solutionVideoList = new ArrayList<>();

	public void initSolutionCriteriaList(List<SolutionCriteria> solutionCriteriaList) {
		this.solutionCriteriaList = solutionCriteriaList;
	}
}
