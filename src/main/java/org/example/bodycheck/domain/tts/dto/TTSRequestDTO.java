package org.example.bodycheck.domain.tts.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NotEmpty
public class TTSRequestDTO {
    private Long exerciseId;

    private String voice;

    private String script;
}
