package org.example.bodycheck.domain.solution.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.apiPayload.ApiResponse;
import org.example.bodycheck.external.openai.service.OpenAIService;
import org.example.bodycheck.common.validation.annotation.ExistExercise;
import org.example.bodycheck.common.validation.annotation.ExistSolution;
import org.example.bodycheck.domain.solutioncriteria.entity.SolutionCriteria;
import org.example.bodycheck.domain.solutioncriteria.service.SolutionCriteriaCommandService;
import org.example.bodycheck.domain.solutioncriteria.service.SolutionCriteriaQueryService;
import org.example.bodycheck.domain.member.annotation.AuthUser;
import org.example.bodycheck.domain.member.entity.Member;
import org.example.bodycheck.domain.solution.converter.SolutionConverter;
import org.example.bodycheck.domain.solution.dto.SolutionRequestDto;
import org.example.bodycheck.domain.solution.dto.SolutionResponseDto;
import org.example.bodycheck.domain.solution.entity.Solution;
import org.example.bodycheck.domain.solution.service.SolutionCommandService;
import org.example.bodycheck.domain.solution.service.SolutionQueryService;
import org.example.bodycheck.domain.solutionvideo.service.SolutionVideoCommandService;
import org.example.bodycheck.domain.solutionvideo.service.SolutionVideoQueryService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/solutions")
public class SolutionController {

    private final SolutionCommandService solutionCommandService;
    private final SolutionQueryService solutionQueryService;
    private final SolutionVideoQueryService solutionVideoQueryService;


    @PostMapping("/generation/exercise/{exerciseId}")
    @Operation(summary = "솔루션 생성 API", description = "운동에 대한 텍스트 솔루션을 생성하는 API 입니다.")
    @Parameters({
            @Parameter(name = "exerciseId", description = "어떤 운동인지 판단하는 운동 아이디, path variable 입니다!")
    })
    public ApiResponse<String> generateSolution(@AuthUser Member member,
                                                @ExistExercise @PathVariable("exerciseId") Long exerciseId,
                                                @RequestBody SolutionRequestDto.PromptDto request) {
        String response = solutionCommandService.generateSolution(member.getId(), exerciseId, request);

        return ApiResponse.onSuccess(response);
    }

    @PostMapping(value = "/exercise/{exerciseId}", consumes = "multipart/form-data")
    @Operation(summary = "솔루션 저장 API", description = "사용자가 솔루션을 저장하는 API 입니다. application/json이 아닌 multipart/form-data 형태입니다.")
    @Parameters({
            @Parameter(name = "exerciseId", description = "솔루션이 어떤 운동인지 판단하는 운동 아이디, path variable 입니다!")
    })
    public ApiResponse<?> createSolution(@AuthUser Member member,
                                                                             @ExistExercise @PathVariable("exerciseId") Long exerciseId,
                                                                             @RequestPart(value = "solutionvideo", required = false) MultipartFile file,
                                                                             @RequestPart(value = "data") @Valid String requestJson) throws JsonProcessingException {

        SolutionRequestDto.SaveDto request;
        try {
            request = new ObjectMapper().readValue(requestJson, SolutionRequestDto.SaveDto.class);
        } catch (JsonProcessingException e) {
            return ApiResponse.onFailure("400", "잘못된 JSON 형식입니다.", "잘못된 JSON 형식입니다.");
        }

        Solution solution = solutionCommandService.saveSolution(member, exerciseId, request, file);

        return ApiResponse.onSuccess(SolutionConverter.toSolutionResultDTO(solution));
    }

    @GetMapping("")
    @Operation(summary = "솔루션 목록 조회 API", description = "사용자의 솔루션 목록을 조회하는 API이며, 페이징을 포함합니다. query String으로 page 번호를 주세요.")
    @Parameters({
            @Parameter(name = "targetBody", description = "카테고리로, 운동 부위, query parameter 입니다! NULL, UPPER_BODY, LOWER_BODY 이렇게 셋 중 하나의 값만 입력해주세요."),
            @Parameter(name = "period", description = "카테고리로, 지정 기간, query parameter 입니다! 0, 1, 2, 3, 4 중 하나의 값만 입력해주세요."),
            @Parameter(name = "page", description = "현재 페이지, query parameter 입니다!")
    })
    public ApiResponse<SolutionResponseDto.SolutionListDto> getSolutionList(@AuthUser Member member,
                                                                            @RequestParam(name = "targetBody", defaultValue = "NULL") String exerciseType,
                                                                            @RequestParam(name = "period", defaultValue = "0") Integer period,
                                                                            @RequestParam(name = "page", defaultValue = "0") Integer page) {
        List<Solution> solutionList = solutionQueryService.getSolutionList(member.getId(), exerciseType, period, page);
        return ApiResponse.onSuccess(SolutionConverter.solutionListDTO(solutionList, page));
    }

    @GetMapping("/{solutionId}")
    @Operation(summary = "솔루션 단건 조회 API", description = "솔루션 단건 조회하는 API입니다.")
    @Parameters({
            @Parameter(name = "solutionId", description = "어떤 솔루션인지 판단하는 솔루션 아이디, path variable 입니다!")
    })
    public ApiResponse<SolutionResponseDto.SolutionDetailDto> getSolutionDetail(@AuthUser Member member,
                                                                                @ExistSolution @PathVariable("solutionId") Long solutionId) {
        SolutionResponseDto.SolutionDetailDto solutionDetailDto = solutionQueryService.getSolutionDetail(solutionId);
        return ApiResponse.onSuccess(solutionDetailDto);
    }

    @GetMapping("/expert/{solutionId}")
    @Operation(summary = "전문가 영상 조회 API", description = "전문가 영상을 조회하는 API입니다.")
    @Parameters({
            @Parameter(name = "solutionId", description = "운동 전문가에 대한 솔루션 id, path variable 입니다!")
    })
    public ApiResponse<SolutionResponseDto.ExpertSolutionDto> getExpertExerciseVideoDetail(@AuthUser Member member,
                                                                                           @ExistSolution @PathVariable("solutionId") Long solutionId) {
        String url = solutionVideoQueryService.getUrl(solutionId);
        return ApiResponse.onSuccess(SolutionConverter.toExpertSolutionDTO(url));
    }
}
