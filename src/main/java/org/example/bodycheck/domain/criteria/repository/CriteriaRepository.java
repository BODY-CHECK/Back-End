package org.example.bodycheck.domain.criteria.repository;

import org.example.bodycheck.domain.criteria.entity.Criteria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CriteriaRepository extends JpaRepository<Criteria, Long> {
}
