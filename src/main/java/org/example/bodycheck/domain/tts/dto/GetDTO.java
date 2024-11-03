package org.example.bodycheck.domain.tts.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetDTO {
    String context;
    byte[] audioBytes;
    Long memberId;
}
