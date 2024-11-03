package org.example.bodycheck.domain.voiceList.service;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.domain.enums.VoiceCode;
import org.example.bodycheck.domain.member.entity.Member;
import org.example.bodycheck.domain.voiceList.dto.VoiceListDTO;
import org.example.bodycheck.domain.voiceList.entity.VoiceList;
import org.example.bodycheck.domain.voiceList.repository.VoiceListRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class VoiceListService {

    private final VoiceListRepository voiceListRepository;

    public VoiceListDTO fetchVoiceList(String voiceCode) {
        VoiceCode voice = VoiceCode.fromValue(voiceCode);
        VoiceList voiceList = voiceListRepository.findByVoiceCode(voice)
                .orElseThrow(()->new NoSuchElementException(voiceCode + " not found"));
        return new VoiceListDTO(voiceList);
    }
}
