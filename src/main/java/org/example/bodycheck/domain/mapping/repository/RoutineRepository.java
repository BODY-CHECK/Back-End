package org.example.bodycheck.domain.mapping.repository;

import org.example.bodycheck.domain.mapping.entity.Routine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {
    boolean existsByMember_IdAndWeekIdAndRoutineIdx(Long memberId, Integer weekId, Integer routineIdx);
}
