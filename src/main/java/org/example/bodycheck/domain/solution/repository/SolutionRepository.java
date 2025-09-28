package org.example.bodycheck.domain.solution.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.example.bodycheck.domain.enums.ExerciseType;
import org.example.bodycheck.domain.solution.entity.Solution;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SolutionRepository extends JpaRepository<Solution, Long> {

	@Query("SELECT s FROM Solution s WHERE s.member.id = :memberId "
		+ "AND (:exerciseType IS NULL OR s.exercise.type = :exerciseType) "
		+ "AND (:month = 0 AND :year = 0 "
		+ "OR (FUNCTION('YEAR', s.createdAt) = :year AND FUNCTION('MONTH', s.createdAt) = :month)) "
		+ "AND s.createdAt < :cursor "
		+ "ORDER BY s.createdAt DESC")
	List<Solution> findSolutions(@Param("memberId") Long memberId, @Param("exerciseType") ExerciseType exerciseType,
		@Param("year") Integer year,
		@Param("month") Integer month, @Param("cursor") LocalDateTime cursor, Pageable pageable);

	Optional<Solution> findByIdAndMember_Id(Long solutionId, Long memberId);
}
