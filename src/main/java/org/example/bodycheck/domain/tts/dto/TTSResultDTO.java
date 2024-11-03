package org.example.bodycheck.domain.tts.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TTSResultDTO {
    byte[] audioBytes;
}
