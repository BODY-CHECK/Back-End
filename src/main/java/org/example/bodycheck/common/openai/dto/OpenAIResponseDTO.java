package org.example.bodycheck.common.openai.dto;

import lombok.Getter;

import java.util.List;

public class OpenAIResponseDTO { // openAI에서 받은 DTO

    @Getter
    public static class ChoiceDTO {
        private int index;
        private MessageDTO.TextMessage message;
    }

    @Getter
    public static class ChatGPTResponseDTO {
        private List<ChoiceDTO> choices;
    }
}
