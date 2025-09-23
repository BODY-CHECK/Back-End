package org.example.bodycheck.external.openai.dto;

import java.util.List;

import lombok.Getter;

public class OpenAIResponseDto { // openAI에서 받은 Dto

	@Getter
	public static class ChoiceDto {
		private int index;
		private MessageDto.TextMessage message;
	}

	@Getter
	public static class ChatGptResponseDto {
		private List<ChoiceDto> choices;
	}
}
