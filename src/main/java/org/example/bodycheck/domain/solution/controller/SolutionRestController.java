package org.example.bodycheck.domain.solution.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.apiPayload.ApiResponse;
import org.example.bodycheck.common.openai.OpenAIService;
import org.example.bodycheck.common.validation.annotation.ExistExercise;
import org.example.bodycheck.common.validation.annotation.ExistSolution;
import org.example.bodycheck.domain.mapping.entity.SolutionCriteria;
import org.example.bodycheck.domain.mapping.service.SolutionCriteriaCommandService;
import org.example.bodycheck.domain.mapping.service.SolutionCriteriaQueryService;
import org.example.bodycheck.domain.member.annotation.AuthUser;
import org.example.bodycheck.domain.member.entity.Member;
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

    private final SolutionCommandService solutionCommandService;
    private final SolutionQueryService solutionQueryService;
    private final SolutionCriteriaCommandService solutionCriteriaCommandService;
    private final SolutionCriteriaQueryService solutionCriteriaQueryService;
    private final SolutionVideoCommandService solutionVideoCommandService;
    private final SolutionVideoQueryService solutionVideoQueryService;
    private final OpenAIService openAIService;


    @PostMapping("/generation/exercise/{exerciseId}")
    @Operation(summary = "솔루션 생성 API", description = "운동에 대한 텍스트 솔루션을 생성하는 API 입니다.")
    @Parameters({
            @Parameter(name = "exerciseId", description = "어떤 운동인지 판단하는 운동 아이디, path variable 입니다!")
    })
    public ApiResponse<String> generateSolution(@AuthUser Member member,
                                                @ExistExercise @PathVariable("exerciseId") Long exerciseId,
                                                @RequestBody SolutionRequestDTO.PromptDTO request) {

        Long memberId = member.getId();

        String prompt = solutionCommandService.generateSolution(memberId, exerciseId, request);

        String response = openAIService.chat(prompt);

        return ApiResponse.onSuccess(response);
    }

    @PostMapping(value = "/exercise/{exerciseId}", consumes = "multipart/form-data")
    @Operation(summary = "솔루션 저장 API", description = "사용자가 솔루션을 저장하는 API 입니다. application/json이 아닌 multipart/form-data 형태입니다.")
    @Parameters({
            @Parameter(name = "exerciseId", description = "솔루션이 어떤 운동인지 판단하는 운동 아이디, path variable 입니다!")
    })
    public ApiResponse<?> createSolution(@AuthUser Member member,
                                                                             @ExistExercise @PathVariable("exerciseId") Long exerciseId,
                                                                             @RequestPart(value = "solutionVideo", required = false) MultipartFile file,
                                                                             @RequestPart(value = "data") @Valid String requestJson) throws JsonProcessingException {

        SolutionRequestDTO.SaveDTO request;
        try {
            request = new ObjectMapper().readValue(requestJson, SolutionRequestDTO.SaveDTO.class);
        } catch (JsonProcessingException e) {
            return ApiResponse.onFailure("400", "잘못된 JSON 형식입니다.", "잘못된 JSON 형식입니다.");
        }

        Long memberId = member.getId();

        Solution solution = solutionCommandService.saveSolution(memberId, exerciseId, request);
        solutionCriteriaCommandService.saveSolutionCriteria(solution, exerciseId, request);
        solutionVideoCommandService.uploadFile(solution, file);

        return ApiResponse.onSuccess(SolutionConverter.toSolutionResultDTO(solution));
    }

    @GetMapping("")
    @Operation(summary = "솔루션 목록 조회 API", description = "사용자의 솔루션 목록을 조회하는 API이며, 페이징을 포함합니다. query String으로 page 번호를 주세요.")
    @Parameters({
            @Parameter(name = "targetBody", description = "카테고리로, 운동 부위, query parameter 입니다! NULL, UPPER_BODY, LOWER_BODY 이렇게 셋 중 하나의 값만 입력해주세요."),
            @Parameter(name = "period", description = "카테고리로, 지정 기간, query parameter 입니다! 0, 1, 2, 3, 4 중 하나의 값만 입력해주세요."),
            @Parameter(name = "page", description = "현재 페이지, query parameter 입니다!")
    })
    public ApiResponse<SolutionResponseDTO.SolutionListDTO> getSolutionList(@AuthUser Member member,
                                                                            @RequestParam(name = "targetBody", defaultValue = "NULL") String exerciseType,
                                                                            @RequestParam(name = "period", defaultValue = "0") Integer period,
                                                                            @RequestParam(name = "page", defaultValue = "0") Integer page) {

        Long memberId = member.getId();

        Slice<Solution> solutionList = solutionQueryService.getSolutionList(memberId, exerciseType, period, page);
        return ApiResponse.onSuccess(SolutionConverter.solutionListDTO(solutionList));
    }

    @GetMapping("/{solutionId}")
    @Operation(summary = "솔루션 단건 조회 API", description = "솔루션 단건 조회하는 API입니다.")
    @Parameters({
            @Parameter(name = "solutionId", description = "어떤 솔루션인지 판단하는 솔루션 아이디, path variable 입니다!")
    })
    public ApiResponse<SolutionResponseDTO.SolutionDetailDTO> getSolutionDetail(@AuthUser Member member,
                                                                                @ExistSolution @PathVariable("solutionId") Long solutionId) {

        Long memberId = member.getId();

        String url = solutionVideoQueryService.getUrl(solutionId);

        List<SolutionCriteria> solutionCriteriaList = solutionCriteriaQueryService.getSolutionCriteriaList(solutionId);

        String content = solutionQueryService.getSolutionContent(solutionId, memberId);

        return ApiResponse.onSuccess(SolutionConverter.toSolutionDetailDTO(url, solutionCriteriaList, content));
    }

    @GetMapping("/expert/{solutionId}")
    @Operation(summary = "전문가 영상 조회 API", description = "전문가 영상을 조회하는 API입니다.")
    @Parameters({
            @Parameter(name = "solutionId", description = "운동 전문가에 대한 솔루션 id, path variable 입니다!")
    })
    public ApiResponse<SolutionResponseDTO.ExpertSolutionDTO> getExpertExerciseVideoDetail(@AuthUser Member member,
                                                                                @ExistSolution @PathVariable("solutionId") Long solutionId) {
        String url = solutionVideoQueryService.getUrl(solutionId);

        return ApiResponse.onSuccess(SolutionConverter.toExpertSolutionDTO(url));
    }
}
