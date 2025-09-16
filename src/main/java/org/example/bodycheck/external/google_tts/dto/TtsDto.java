package org.example.bodycheck.external.google_tts.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TtsDto {

    @NotEmpty(message = "voice type is null")
    private String voice;

    private String content;
}
