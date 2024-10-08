package org.example.bodycheck.domain.solution.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.apiPayload.ApiResponse;
import org.example.bodycheck.common.validation.annotation.ExistExercise;
import org.example.bodycheck.domain.member.service.MemberService.MemberQueryService;
import org.example.bodycheck.domain.solution.converter.SolutionConverter;
import org.example.bodycheck.domain.solution.dto.SolutionRequestDTO;
import org.example.bodycheck.domain.solution.dto.SolutionResponseDTO;
import org.example.bodycheck.domain.solution.entity.Solution;
import org.example.bodycheck.domain.solution.service.SolutionCommandService;
import org.example.bodycheck.domain.solution.service.SolutionQueryService;
import org.example.bodycheck.domain.solutionVideo.service.SolutionVideoCommandService;
import org.springframework.data.domain.Slice;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/solutions")
public class SolutionRestController {

    private final MemberQueryService memberQueryService;
    private final SolutionCommandService solutionCommandService;
    private final SolutionQueryService solutionQueryService;
    private final SolutionVideoCommandService solutionVideoCommandService;


    @PostMapping(value = "/exercise/{exerciseId}", consumes = "multipart/form-data")
    public ApiResponse<SolutionResponseDTO.SolutionResultDTO> createSolution(@RequestHeader("Authorization") String authorizationHeader,
                                                                             @ExistExercise @PathVariable("exerciseId") Long exerciseId, // 여기에 @ExistExercise 어노테이션 추가
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
                                                                            @RequestParam(name = "page", defaultValue = "0") Integer page) {

        Long memberId = memberQueryService.getMember().getId();

        Slice<Solution> solutionList = solutionQueryService.getSolutionList(memberId, page);
        return ApiResponse.onSuccess(SolutionConverter.solutionListDTO(solutionList));
    }
}
