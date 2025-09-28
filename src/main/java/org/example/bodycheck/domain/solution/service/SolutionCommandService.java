package org.example.bodycheck.domain.solution.service;

import org.example.bodycheck.domain.member.entity.Member;
import org.example.bodycheck.domain.solution.dto.SolutionRequestDto;
import org.example.bodycheck.domain.solution.entity.Solution;
import org.springframework.web.multipart.MultipartFile;

public interface SolutionCommandService {

	String generateSolution(Long memberId, Long exerciseId, SolutionRequestDto.PromptDto request);

	Solution saveSolution(Member member, Long exerciseId, SolutionRequestDto.SaveDto request, MultipartFile file);
}
