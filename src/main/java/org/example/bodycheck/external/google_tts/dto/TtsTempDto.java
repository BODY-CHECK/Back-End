package org.example.bodycheck.external.google_tts.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TtsTempDto {

    private byte[] audioBytes;
}
