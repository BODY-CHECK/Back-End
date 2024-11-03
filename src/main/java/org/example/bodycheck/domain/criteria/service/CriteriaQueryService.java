package org.example.bodycheck.domain.criteria.service;


import org.example.bodycheck.domain.criteria.entity.Criteria;

import java.util.List;

public interface CriteriaQueryService {

    List<Criteria> getCriteriaList(Long solutionId);
}
