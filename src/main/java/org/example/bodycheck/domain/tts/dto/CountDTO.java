package org.example.bodycheck.domain.tts.dto;

import lombok.Builder;
import lombok.Getter;
import org.example.bodycheck.domain.enums.VoiceCode;
import org.example.bodycheck.domain.member.entity.Member;

@Getter
@Builder
public class CountDTO {

    String context;
    Boolean isUpdated;
    Long exerciseId;
    byte[] audioBytes;

}
