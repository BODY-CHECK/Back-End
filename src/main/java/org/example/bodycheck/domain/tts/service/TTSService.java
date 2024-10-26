package org.example.bodycheck.domain.tts.service;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.domain.enums.VoiceCode;
import org.example.bodycheck.domain.tts.dto.TTSDTO;
import org.example.bodycheck.domain.tts.dto.TTSResultDTO;
import org.example.bodycheck.domain.tts.dto.TTSTempDTO;
import org.example.bodycheck.domain.tts.entity.Tts;
import org.example.bodycheck.domain.tts.repository.ScriptRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TTSService {

    private final RestTemplate restTemplate;

    private final ScriptRepository scriptRepository;

    @Value("${google.api.key}")
    private String apiKey;

    public TTSResultDTO createSpeech(Long exerciseId, String voice, String script) {

        List<String> scriptList = script != null ? List.of(script) : getScriptFromDB(exerciseId);

        // TTSDTO 설정
        TTSDTO ttsdto = setSpeech(scriptList, voice);

        // TTS API 요청 및 결과 생성
        TTSTempDTO ttsTempDTO = requestSpeech(ttsdto);

        return TTSResultDTO.builder()
                .audioBytes(ttsTempDTO.getAudioBytes())
                .build();
    }

    private TTSDTO setSpeech(List<String> script, String voice) {
        String formattedVoice = VoiceCode.fromValue(voice).getValue();
        return TTSDTO.builder()
                .voice(formattedVoice)   //ex: "ko-KR-Wavenet-A"
                .script(String.join(" ", script))
                .build();
    }



    private TTSTempDTO requestSpeech(TTSDTO ttsdto) {
        String url = "https://texttospeech.googleapis.com/v1beta1/text:synthesize?key=" + apiKey;

        Map<String, Object> requestBody = new HashMap<>();
        Map<String, Object> input = new HashMap<>();
        input.put("ssml", ttsdto.getScript());
        requestBody.put("input", input);

        Map<String, Object> voice = new HashMap<>();
        voice.put("name", ttsdto.getVoice());
        requestBody.put("voice", voice);

        Map<String, Object> audioConfig = new HashMap<>();
        audioConfig.put("audioEncoding", "MP3");
        requestBody.put("audioConfig", audioConfig);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        Long startTime = System.currentTimeMillis();
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
        Long endTime = System.currentTimeMillis();
        System.out.printf("TTSService: TTS took %.2f seconds%n", (double)(endTime - startTime)/1000);
        String audioContent = (String) response.getBody().get("audioContent");

        byte[] audioBytes = Base64.getDecoder().decode(audioContent);

        return TTSTempDTO.builder()
                .audioBytes(audioBytes)
                .build();
    }

    private List<String> getScriptFromDB(Long exerciseId) {
        // scriptId를 사용해 DB에서 script 데이터를 조회
        Optional<Tts> script = scriptRepository.findById(exerciseId);
        if (script.isPresent()) {
            return script.get().getScript(); // ScriptEntity의 script 필드에 저장된 List<String> 반환
        } else {
            throw new RuntimeException("Script not found for ID: " + exerciseId);
        }
    }
}

