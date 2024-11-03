package org.example.bodycheck.domain.solution.repository;

import org.example.bodycheck.domain.enums.ExerciseType;
import org.example.bodycheck.domain.solution.entity.Solution;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SolutionRepository extends JpaRepository<Solution, Long> {

    @Query("SELECT s FROM Solution s WHERE s.member.id = :memberId " +
            "AND (:exerciseType IS NULL OR s.exercise.type = :exerciseType) " +
            "AND (:month = 0 AND :year = 0 OR (FUNCTION('YEAR', s.createdAt) = :year AND FUNCTION('MONTH', s.createdAt) = :month)) " +
            "ORDER BY s.createdAt DESC")
    Slice<Solution> findSolutions(@Param("memberId") Long memberId, @Param("exerciseType") ExerciseType exerciseType, @Param("year") Integer year,
                                  @Param("month") Integer month, PageRequest pageRequest);

    Optional<Solution> findByIdAndMember_Id(Long solutionId, Long memberId);
}
