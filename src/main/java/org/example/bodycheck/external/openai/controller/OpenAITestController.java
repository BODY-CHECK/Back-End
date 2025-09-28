package org.example.bodycheck.external.openai.controller;

import java.io.IOException;

import org.example.bodycheck.common.apipayload.ApiResponse;
import org.example.bodycheck.external.openai.service.OpenAIService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Deprecated
@RestController
@RequiredArgsConstructor
public class OpenAITestController { // 테스트 용

	private final OpenAIService openAIService;

	@GetMapping("/chatGPT")
	public ApiResponse<String> chatGpt(@RequestParam(name = "prompt") String prompt) {

		String response = openAIService.chat(prompt);

		return ApiResponse.onSuccess(response);
	}

	@PostMapping(value = "/visionGPT", consumes = "multipart/form-data")
	public ApiResponse<String> visionGpt(@RequestPart(value = "image", required = false) MultipartFile image,
		@RequestPart(value = "prompt") @Valid String prompt) throws IOException {

		String response;
		if (image == null) {
			response = openAIService.chat(prompt);
		} else {
			response = openAIService.vision(image, prompt);
		}

		return ApiResponse.onSuccess(response);
	}
}
