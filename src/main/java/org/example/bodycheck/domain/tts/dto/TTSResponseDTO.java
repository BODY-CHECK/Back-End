package org.example.bodycheck.domain.tts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TTSResponseDTO {

    private Long exerciseId;
    private String script;
    private boolean success;
}
