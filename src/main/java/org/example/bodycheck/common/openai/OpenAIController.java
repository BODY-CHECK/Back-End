package org.example.bodycheck.common.openai;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.apiPayload.ApiResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class OpenAIController {

    private final OpenAIService openAIService;

    @GetMapping("/chatGPT")
    public ApiResponse<String> chatGPT(@RequestParam(name = "prompt") String prompt) {

        String response = openAIService.chat(prompt);

        return ApiResponse.onSuccess(response);
    }

    @PostMapping(value ="/visionGPT", consumes = "multipart/form-data")
    public ApiResponse<String> visionGPT(@RequestPart(value = "image", required = false) MultipartFile image,
                                         @RequestPart(value = "prompt") @Valid String prompt) throws IOException {

        String response;
        if (image == null) {
            response = openAIService.chat(prompt);
        }
        else {
            response = openAIService.vision(image, prompt);
        }

        return ApiResponse.onSuccess(response);
    }
}
