package org.example.bodycheck.domain.solution.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.apiPayload.ApiResponse;
import org.example.bodycheck.common.validation.annotation.ExistExercise;
import org.example.bodycheck.common.validation.annotation.ExistSolution;
import org.example.bodycheck.domain.criteria.entity.Criteria;
import org.example.bodycheck.domain.criteria.service.CriteriaQueryService;
import org.example.bodycheck.domain.member.service.MemberService.MemberQueryService;
import org.example.bodycheck.domain.solution.converter.SolutionConverter;
import org.example.bodycheck.domain.solution.dto.SolutionRequestDTO;
import org.example.bodycheck.domain.solution.dto.SolutionResponseDTO;
import org.example.bodycheck.domain.solution.entity.Solution;
import org.example.bodycheck.domain.solution.service.SolutionCommandService;
import org.example.bodycheck.domain.solution.service.SolutionQueryService;
import org.example.bodycheck.domain.solutionVideo.service.SolutionVideoCommandService;
import org.example.bodycheck.domain.solutionVideo.service.SolutionVideoQueryService;
import org.springframework.data.domain.Slice;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/solutions")
public class SolutionRestController {

    private final MemberQueryService memberQueryService;
    private final SolutionCommandService solutionCommandService;
    private final SolutionQueryService solutionQueryService;
    private final SolutionVideoCommandService solutionVideoCommandService;
    private final SolutionVideoQueryService solutionVideoQueryService;
    private final CriteriaQueryService criteriaQueryService;


    @PostMapping(value = "/exercise/{exerciseId}", consumes = "multipart/form-data")
    public ApiResponse<SolutionResponseDTO.SolutionResultDTO> createSolution(@RequestHeader("Authorization") String authorizationHeader,
                                                                             @ExistExercise @PathVariable("exerciseId") Long exerciseId,
                                                                             @RequestPart(value = "solutionVideo", required = false) MultipartFile file,
                                                                             @RequestPart(value = "data") @Valid String requestJson) throws JsonProcessingException {

        SolutionRequestDTO.SaveDTO request = new ObjectMapper().readValue(requestJson, SolutionRequestDTO.SaveDTO.class);

        Long memberId = memberQueryService.getMember().getId();
        Solution solution = solutionCommandService.saveSolution(memberId, exerciseId, request);
        solutionVideoCommandService.uploadFile(solution, file);

        return ApiResponse.onSuccess(SolutionConverter.toSolutionResultDTO(solution));
    }

    @GetMapping("")
    public ApiResponse<SolutionResponseDTO.SolutionListDTO> getSolutionList(@RequestHeader("Authorization") String authorizationHeader,
                                                                            @RequestParam(name = "bound", defaultValue = "NULL") String exerciseType,
                                                                            @RequestParam(name = "month", defaultValue = "0") Integer period,
                                                                            @RequestParam(name = "page", defaultValue = "0") Integer page) {

        Long memberId = memberQueryService.getMember().getId();

        Slice<Solution> solutionList = solutionQueryService.getSolutionList(memberId, exerciseType, period, page);
        return ApiResponse.onSuccess(SolutionConverter.solutionListDTO(solutionList));
    }

    @GetMapping("/{solutionId}")
    public ApiResponse<SolutionResponseDTO.SolutionDetailDTO> getSolutionDetail(@RequestHeader("Authorization") String authorizationHeader,
                                                                                @ExistSolution @PathVariable("solutionId") Long solutionId) {

        Long memberId = memberQueryService.getMember().getId();

        String url = solutionVideoQueryService.getUrl(solutionId);

        List<Criteria> criteriaList = criteriaQueryService.getCriteriaList(solutionId);

        String content = solutionQueryService.getSolutionContent(solutionId, memberId);

        return ApiResponse.onSuccess(SolutionConverter.toSolutionDetailDTO(url, criteriaList, content));
    }
}
