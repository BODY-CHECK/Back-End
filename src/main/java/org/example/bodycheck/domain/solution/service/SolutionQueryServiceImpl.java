package org.example.bodycheck.domain.solution.service;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.apiPayload.code.status.ErrorStatus;
import org.example.bodycheck.common.exception.handler.GeneralHandler;
import org.example.bodycheck.domain.solution.entity.Solution;
import org.example.bodycheck.domain.solution.repository.SolutionRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SolutionQueryServiceImpl implements SolutionQueryService {

    private final SolutionRepository solutionRepository;

    @Override
    public Slice<Solution> getSolutionList(Long memberId, Integer page) {
        Slice<Solution> solutionList = solutionRepository.findByMember_IdOrderByCreatedAt(memberId, PageRequest.of(page, 10));

        if (solutionList.isEmpty() && page != 0) {
            throw new GeneralHandler(ErrorStatus.SOLUTION_PAGE_NOT_FOUND);
        }

        return solutionList;
    }
}
