package org.example.bodycheck.domain.solution.converter;

import org.example.bodycheck.domain.criteria.dto.CriteriaResponseDTO;
import org.example.bodycheck.domain.mapping.converter.SolutionCriteriaConverter;
import org.example.bodycheck.domain.mapping.entity.SolutionCriteria;
import org.example.bodycheck.domain.solution.dto.SolutionRequestDTO;
import org.example.bodycheck.domain.solution.dto.SolutionResponseDTO;
import org.example.bodycheck.domain.solution.entity.Solution;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class SolutionConverter {

    public static Solution toSolution(SolutionRequestDTO.SaveDTO request) {
        return Solution.builder()
                .content(request.getContent())
                .build();
    }

    public static SolutionResponseDTO.SolutionResultDTO toSolutionResultDTO(Solution solution) {
        return SolutionResponseDTO.SolutionResultDTO.builder()
                .id(solution.getId())
                .build();
    }

    public static SolutionResponseDTO.SolutionInfoDTO toSolutionInfoDTO(Solution solution) {

        LocalDateTime localDateTime = solution.getCreatedAt();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.MM.dd");
        String formattedDate = localDateTime.format(formatter);

        return SolutionResponseDTO.SolutionInfoDTO.builder()
                .id(solution.getId())
                .exerciseId(solution.getExercise().getId())
                .exerciseName(solution.getExercise().getName())
                .exerciseDate(formattedDate)
                .build();
    }

    public static SolutionResponseDTO.SolutionListDTO solutionListDTO(List<Solution> solutionList, Integer page) {

        List<SolutionResponseDTO.SolutionInfoDTO> solutionInfoDTOList = solutionList.stream()
                .map(SolutionConverter::toSolutionInfoDTO).collect(Collectors.toList());

        boolean isFirst = page.equals(0);
        boolean hasNext = solutionInfoDTOList.size() > 10;
        boolean isLast = !hasNext;

        if (hasNext) {
            solutionInfoDTOList = solutionInfoDTOList.subList(0, 10);
        }

        return SolutionResponseDTO.SolutionListDTO.builder()
                .solutionList(solutionInfoDTOList)
                .isFirst(isFirst)
                .isLast(isLast)
                .listSize(solutionInfoDTOList.size())
                .hasNext(hasNext)
                .build();
    }

    public static SolutionResponseDTO.SolutionDetailDTO toSolutionDetailDTO(String url, List<SolutionCriteria> solutionCriteriaList, String content) {
        List<CriteriaResponseDTO.CriteriaDetailDTO> criteriaDetailDTOList = solutionCriteriaList.stream()
                .map(SolutionCriteriaConverter::toCriteriaDetailDTO).collect(Collectors.toList());

        return SolutionResponseDTO.SolutionDetailDTO.builder()
                .solutionVideoUrl(url)
                .criteriaDetailList(criteriaDetailDTOList)
                .content(content).build();
    }

    public static SolutionResponseDTO.ExpertSolutionDTO toExpertSolutionDTO(String url) {
        return SolutionResponseDTO.ExpertSolutionDTO.builder()
                .solutionVideoUrl(url)
                .build();
    }
}
