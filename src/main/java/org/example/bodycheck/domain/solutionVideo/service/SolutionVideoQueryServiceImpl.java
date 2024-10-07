package org.example.bodycheck.domain.solutionVideo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SolutionVideoQueryServiceImpl implements SolutionVideoQueryService {
}
