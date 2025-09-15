package org.example.bodycheck.external.openai.dto;

import lombok.Getter;

import java.util.List;

public class OpenAIResponseDto { // openAI에서 받은 Dto

    @Getter
    public static class ChoiceDto {
        private int index;
        private MessageDto.TextMessage message;
    }

    @Getter
    public static class ChatGPTResponseDto {
        private List<ChoiceDto> choices;
    }
}
