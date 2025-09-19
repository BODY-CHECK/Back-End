package org.example.bodycheck.domain.solution.converter;

import org.example.bodycheck.domain.criteria.dto.CriteriaResponseDto;
import org.example.bodycheck.domain.solutioncriteria.converter.SolutionCriteriaConverter;
import org.example.bodycheck.domain.solutioncriteria.entity.SolutionCriteria;
import org.example.bodycheck.domain.solution.dto.SolutionRequestDto;
import org.example.bodycheck.domain.solution.dto.SolutionResponseDto;
import org.example.bodycheck.domain.solution.entity.Solution;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class SolutionConverter {

    public static Solution toSolution(SolutionRequestDto.SaveDto request) {
        return Solution.builder()
                .content(request.getContent())
                .build();
    }

    public static SolutionResponseDto.SolutionResultDto toSolutionResultDTO(Solution solution) {
        return SolutionResponseDto.SolutionResultDto.builder()
                .id(solution.getId())
                .build();
    }

    public static SolutionResponseDto.SolutionInfoDto toSolutionInfoDTO(Solution solution) {

        LocalDateTime localDateTime = solution.getCreatedAt();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.MM.dd");
        String formattedDate = localDateTime.format(formatter);

        return SolutionResponseDto.SolutionInfoDto.builder()
                .id(solution.getId())
                .exerciseId(solution.getExercise().getId())
                .exerciseName(solution.getExercise().getName())
                .exerciseDate(formattedDate)
                .build();
    }

    public static SolutionResponseDto.SolutionListDto solutionListDTO(List<Solution> solutionList, Integer page) {

        List<SolutionResponseDto.SolutionInfoDto> solutionInfoDtoList = solutionList.stream()
                .map(SolutionConverter::toSolutionInfoDTO).collect(Collectors.toList());

        boolean isFirst = page.equals(0);
        boolean hasNext = solutionInfoDtoList.size() > 10;
        boolean isLast = !hasNext;

        if (hasNext) {
            solutionInfoDtoList = solutionInfoDtoList.subList(0, 10);
        }

        return SolutionResponseDto.SolutionListDto.builder()
                .solutionList(solutionInfoDtoList)
                .isFirst(isFirst)
                .isLast(isLast)
                .listSize(solutionInfoDtoList.size())
                .hasNext(hasNext)
                .build();
    }

    public static SolutionResponseDto.SolutionDetailDto toSolutionDetailDTO(String url, List<SolutionCriteria> solutionCriteriaList, String content) {
        List<CriteriaResponseDto.CriteriaDetailDto> criteriaDetailDtoList = solutionCriteriaList.stream()
                .map(SolutionCriteriaConverter::toCriteriaDetailDTO).collect(Collectors.toList());

        return SolutionResponseDto.SolutionDetailDto.builder()
                .solutionVideoUrl(url)
                .criteriaDetailList(criteriaDetailDtoList)
                .content(content).build();
    }

    public static SolutionResponseDto.ExpertSolutionDto toExpertSolutionDTO(String url) {
        return SolutionResponseDto.ExpertSolutionDto.builder()
                .solutionVideoUrl(url)
                .build();
    }
}
