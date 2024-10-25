package org.example.bodycheck.domain.voiceList.dto;

import lombok.*;
import org.example.bodycheck.domain.voiceList.entity.VoiceList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class VoiceListDTO {
    private String voice;
    private String filePath;

    public VoiceListDTO(VoiceList voiceList) {
        voice = voiceList.getVoiceCode().name();
        filePath = voiceList.getFilePath();
    }
}
