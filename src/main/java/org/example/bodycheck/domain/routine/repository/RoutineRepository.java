package org.example.bodycheck.domain.routine.repository;

import java.util.List;
import java.util.Optional;

import org.example.bodycheck.domain.routine.entity.Routine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {

	@Query("SELECT r FROM Routine r LEFT JOIN FETCH r.exercise WHERE r.member.id = :memberId AND r.weekId = :weekId")
	List<Routine> findByMemberIdAndWeekIdWithExercise(@Param("memberId") Long memberId,
		@Param("weekId") Integer weekId);

	List<Routine> findByMember_IdAndWeekIdAndExercise_Id(Long memberId, Integer weekId, Long exerciseId);

	List<Routine> findByMemberId(@Param("memberId") Long memberId);

	Optional<Routine> findByIdAndMember_id(Long id, Long memberId);
}
