package org.example.bodycheck.external.openai.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class OpenAIRequestDto { // openAI로 보낼 DTO

	@Builder
	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class ChatGptRequestDto {
		private String model;
		private List<Message> messages;
	}

	@Getter
	@AllArgsConstructor
	public static class ImageUrl {
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
		@JsonProperty("image_url")
		private ImageUrl imageUrl;

		public ImageContent(String type, ImageUrl imageUrl) {
			super(type);
			this.imageUrl = imageUrl;
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
