package org.example.bodycheck.external.google_tts.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetDto {
    String context;
    byte[] audioBytes;
    Long memberId;
}
