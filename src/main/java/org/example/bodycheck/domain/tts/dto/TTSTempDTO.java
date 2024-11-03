package org.example.bodycheck.domain.tts.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TTSTempDTO {

    private byte[] audioBytes;
}
