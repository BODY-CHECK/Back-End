package org.example.bodycheck.domain.routine.entity;

import org.example.bodycheck.common.entity.BaseEntity;
import org.example.bodycheck.domain.exercise.entity.Exercise;
import org.example.bodycheck.domain.member.entity.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
		if (this.member != null) {
			member.getRoutineList().remove(this);
		}
		this.member = member;
		member.getRoutineList().add(this);
	}

	public void updateRoutineInfo(Exercise exercise, Boolean routineCheck) {
		this.exercise = exercise;
		this.routineCheck = routineCheck;
	}

	public void updateRoutineCheck(Boolean routineCheck) { // setter 메서드 추가
		this.routineCheck = routineCheck;
	}
}
