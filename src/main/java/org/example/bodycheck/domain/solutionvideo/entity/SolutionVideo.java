package org.example.bodycheck.domain.solutionvideo.entity;

import org.example.bodycheck.common.entity.BaseEntity;
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
public class SolutionVideo extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String videoUrl;

	@ManyToOne(fetch = FetchType.LAZY) // 원래는 OneToOne
	@JoinColumn(name = "solution_id")
	private Solution solution;
}
