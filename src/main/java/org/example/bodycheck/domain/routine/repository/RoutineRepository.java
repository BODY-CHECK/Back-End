package org.example.bodycheck.domain.routine.repository;

import org.example.bodycheck.domain.routine.entity.Routine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {
    List<Routine> findByMemberIdAndWeekId(@Param("memberId") Long memberId, @Param("weekId") Integer weekId);
    List<Routine> findByMemberId(@Param("memberId") Long memberId);
}
