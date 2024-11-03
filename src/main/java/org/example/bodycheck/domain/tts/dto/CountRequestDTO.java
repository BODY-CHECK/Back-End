package org.example.bodycheck.domain.tts.dto;

import lombok.Builder;
import lombok.Getter;
import org.example.bodycheck.domain.enums.VoiceCode;

@Getter
@Builder
public class CountRequestDTO {

    Integer count;
    Long exerciseId;
    Boolean isUpdated;
    String voice;
}
