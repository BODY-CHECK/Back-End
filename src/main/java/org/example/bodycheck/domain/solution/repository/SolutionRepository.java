package org.example.bodycheck.domain.solution.repository;

import org.example.bodycheck.domain.solution.entity.Solution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolutionRepository extends JpaRepository<Solution, Long> {
}
