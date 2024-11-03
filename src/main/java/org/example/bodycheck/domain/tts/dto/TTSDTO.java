package org.example.bodycheck.domain.tts.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TTSDTO {

    @NotEmpty(message = "voice type is null")
    private String voice;

    private String content;
}
