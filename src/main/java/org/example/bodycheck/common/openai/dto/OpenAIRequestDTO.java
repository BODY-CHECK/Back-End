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
        private List<Message> messages;
    }


    @Getter
    @AllArgsConstructor
    public static class ImageUrl{
        private String url;
    }

    @Getter
    @AllArgsConstructor
    public static class TextContent extends Content {
        private String text;

        public TextContent(String type, String text) {
            super(type);
            this.text = text;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class ImageContent extends Content {
        private ImageUrl image_url;

        public ImageContent(String type, ImageUrl image_url) {
            super(type);
            this.image_url = image_url;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class ImageMessage extends Message {
        private List<Content> content;

        public ImageMessage(String role, List<Content> content) {
            super(role);
            this.content = content;
        }
    }
}
