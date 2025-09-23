package org.example.bodycheck.domain.solutioncriteria.repository;

import java.util.List;

import org.example.bodycheck.domain.solutioncriteria.entity.SolutionCriteria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolutionCriteriaRepository extends JpaRepository<SolutionCriteria, Long> {

	List<SolutionCriteria> findBySolution_Id(Long solutionId);
}
