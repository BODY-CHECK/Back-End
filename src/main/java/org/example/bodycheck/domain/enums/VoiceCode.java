package org.example.bodycheck.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VoiceCode {
    KO_KR_NEURAL2_A("ko-KR-Neural2-A"),
    KO_KR_NEURAL2_B("ko-KR-Neural2-B"),
    KO_KR_NEURAL2_C("ko-KR-Neural2-C"),
    KO_KR_STANDARD_A("ko-KR-Standard-A"),
    KO_KR_STANDARD_B("ko-KR-Standard-B"),
    KO_KR_STANDARD_C("ko-KR-Standard-C"),
    KO_KR_STANDARD_D("ko-KR-Standard-D"),
    KO_KR_WAVENET_A("ko-KR-Wavenet-A"),
    KO_KR_WAVENET_B("ko-KR-Wavenet-B"),
    KO_KR_WAVENET_C("ko-KR-Wavenet-C"),
    KO_KR_WAVENET_D("ko-KR-Wavenet-D");

    private final String value;

    @JsonCreator
    public static VoiceCode fromValue(String value) {
        String normalizedValue = value.toUpperCase().replace("-", "_");
        for (VoiceCode voiceCode : VoiceCode.values()) {
            if (voiceCode.name().equals(normalizedValue)) {
                return voiceCode;
            }
        }
        throw new IllegalArgumentException("Unknown voice type : " + value);
    }
}
