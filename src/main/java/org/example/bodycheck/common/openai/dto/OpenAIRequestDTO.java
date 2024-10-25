package org.example.bodycheck.common.openai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class OpenAIRequestDTO { // openAI로 보낼 DTO

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChatGPTRequestDTO {
        private String model;
        private List<MessageDTO.Message> messages;
    }
}
