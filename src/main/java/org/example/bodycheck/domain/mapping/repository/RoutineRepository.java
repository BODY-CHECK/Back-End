package org.example.bodycheck.domain.mapping.repository;

import org.example.bodycheck.domain.mapping.entity.Routine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {
    List<Routine> findByMemberIdAndWeekId(@Param("memberId") Long memberId, @Param("weekId") Integer weekId);
    Optional<Routine> findByWeekIdAndRoutineIdx(@Param("weekId") Integer weekId, @Param("routineIdx") Integer routineIdx);
    List<Routine> findByMemberId(@Param("memberId") Long memberId);
}
