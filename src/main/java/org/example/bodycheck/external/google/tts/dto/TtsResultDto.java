package org.example.bodycheck.external.google.tts.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TtsResultDto {
    byte[] audioBytes;
}
