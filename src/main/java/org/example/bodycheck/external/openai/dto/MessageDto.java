package org.example.bodycheck.external.openai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MessageDto {

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
