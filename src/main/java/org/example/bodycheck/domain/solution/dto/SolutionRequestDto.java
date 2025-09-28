package org.example.bodycheck.domain.solution.dto;

import java.util.List;

import org.example.bodycheck.domain.criteria.dto.CriteriaRequestDto;

import lombok.Getter;

public class SolutionRequestDto {

	@Getter
	public static class PromptDto {
		List<CriteriaRequestDto.CriteriaDto> criteria;
	}

	@Getter
	public static class SaveDto {
		List<CriteriaRequestDto.CriteriaDto> criteria;
		String content;
	}
}
