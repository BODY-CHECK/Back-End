package org.example.bodycheck.domain.criteria.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CriteriaResponseDto {

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CriteriaDetailDto {
		Integer criteriaIdx;
		String criteriaName;
		Integer score;
	}
}
