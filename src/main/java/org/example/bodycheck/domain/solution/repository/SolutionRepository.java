package org.example.bodycheck.domain.solution.repository;

import org.example.bodycheck.domain.solution.entity.Solution;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SolutionRepository extends JpaRepository<Solution, Long> {

    Slice<Solution> findByMember_IdOrderByCreatedAt(Long memberId, PageRequest pageRequest);

    Optional<Solution> findByIdAndMember_Id(Long solutionId, Long memberId);
}
