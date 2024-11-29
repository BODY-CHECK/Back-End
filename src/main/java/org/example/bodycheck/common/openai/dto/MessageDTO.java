package org.example.bodycheck.common.openai.dto;

import lombok.*;

public class MessageDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TextMessage extends Message {
        private String content;

        public TextMessage(String role, String content) {
            super(role);
            this.content = content;
        }
    }
}
