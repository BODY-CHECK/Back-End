package org.example.bodycheck.domain.solutioncriteria.repository;

import org.example.bodycheck.domain.solutioncriteria.entity.SolutionCriteria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolutionCriteriaRepository extends JpaRepository<SolutionCriteria, Long> {

    List<SolutionCriteria> findBySolution_Id(Long solutionId);
}
