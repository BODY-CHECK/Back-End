package org.example.bodycheck.common.openai;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.apiPayload.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OpenAIController {

    private final OpenAIService openAIService;

    @GetMapping("/chatGPT")
    public ApiResponse<String> chatGPT(@RequestParam(name = "prompt") String prompt) {

        String response = openAIService.chat(prompt);

        return ApiResponse.onSuccess(response);
    }
}
