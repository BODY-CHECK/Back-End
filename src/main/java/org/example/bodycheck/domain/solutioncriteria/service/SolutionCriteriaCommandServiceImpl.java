package org.example.bodycheck.domain.solutioncriteria.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.example.bodycheck.domain.criteria.dto.CriteriaRequestDto;
import org.example.bodycheck.domain.criteria.entity.Criteria;
import org.example.bodycheck.domain.criteria.repository.CriteriaRepository;
import org.example.bodycheck.domain.solution.dto.SolutionRequestDto;
import org.example.bodycheck.domain.solution.entity.Solution;
import org.example.bodycheck.domain.solutioncriteria.converter.SolutionCriteriaConverter;
import org.example.bodycheck.domain.solutioncriteria.entity.SolutionCriteria;
import org.example.bodycheck.domain.solutioncriteria.repository.SolutionCriteriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SolutionCriteriaCommandServiceImpl implements SolutionCriteriaCommandService {
	private final SolutionCriteriaRepository solutionCriteriaRepository;
	private final CriteriaRepository criteriaRepository;

	@Override
	@Transactional
	public void saveSolutionCriteria(Solution solution, Long exerciseId, SolutionRequestDto.SaveDto request) {
		if (solution.getSolutionCriteriaList() == null) {
			solution.initSolutionCriteriaList(new ArrayList<>()); // `criteriaList`가 `null`이면 초기화
		}
		List<Criteria> criterias = criteriaRepository.findByExercise_Id(exerciseId);

		IntStream.range(0, criterias.size())
			.forEach(i -> {
				Criteria criteria = criterias.get(i); // 인덱스를 기준으로 Criteria 가져오기
				CriteriaRequestDto.CriteriaDto criteriaDto = request.getCriteria().get(i); // 동일 인덱스의 CriteriaDto 가져오기

				// SolutionCriteria 변환 및 설정
				SolutionCriteria solutionCriteria = SolutionCriteriaConverter.toSolutionCriteria(criteriaDto);
				solutionCriteria.mappingSolutionAndCriteria(solution, criteria); // Solution과 Criteria 매핑

				// Repository에 저장
				solutionCriteriaRepository.save(solutionCriteria);
			});
	}
}
