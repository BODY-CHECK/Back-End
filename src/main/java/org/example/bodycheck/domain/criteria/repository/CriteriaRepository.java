package org.example.bodycheck.domain.criteria.repository;

import java.util.List;

import org.example.bodycheck.domain.criteria.entity.Criteria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CriteriaRepository extends JpaRepository<Criteria, Long> {

	List<Criteria> findByExercise_Id(Long exerciseId);
}
