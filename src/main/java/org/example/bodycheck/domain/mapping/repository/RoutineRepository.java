package org.example.bodycheck.domain.mapping.repository;

import org.example.bodycheck.domain.mapping.dto.RoutineDTO;
import org.example.bodycheck.domain.mapping.entity.Routine;
import org.example.bodycheck.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {
    boolean existsByMemberAndWeekIdAndRoutineIdx(Member member, Integer weekId, Integer routineIdx);
}
