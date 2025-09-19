package org.example.bodycheck.external.google.tts.service;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.domain.enums.VoiceCode;
import org.example.bodycheck.domain.member.entity.Member;
import org.example.bodycheck.external.google.tts.dto.*;
import org.example.bodycheck.external.google.tts.entity.Tts;
import org.example.bodycheck.external.google.tts.repository.TtsRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TtsService {
    private RestTemplate restTemplate = new RestTemplate();
    //private final FileService fileService;

    private final TtsRepository ttsRepository;

    @Value("${google.api.key}")
    private String apiKey;

    public TtsResultDto createSpeech(String voice, String script) {
        if (script == null || script.trim().isEmpty()) {
            throw new IllegalArgumentException("Script content cannot be null or empty.");
        }

        // TtsDto 설정: context를 스크립트로 사용
        VoiceCode voiceCode = VoiceCode.fromValue(voice);

        TtsDto ttsdto = setSpeech(script, voiceCode.getValue());

        // TTS API 요청 및 결과 생성
        TtsTempDto ttsTempDTO = requestSpeech(ttsdto);

        //byte[] audio = ttsTempDTO.getAudioBytes();

        //String path = makeAudioFile(audio);
        return TtsResultDto.builder()
                .audioBytes(ttsTempDTO.getAudioBytes())
                .build();
    }

    private TtsDto setSpeech(String content, String voice) {
        String formattedVoice = VoiceCode.fromValue(voice).getValue();
        return TtsDto.builder()
                .voice(formattedVoice)   //ex: "ko-KR-Wavenet-A"
                .content(content)
                .build();
    }



    private TtsTempDto requestSpeech(TtsDto ttsdto) {
        String url = "https://texttospeech.googleapis.com/v1beta1/text:synthesize?key=" + apiKey;

        Map<String, Object> requestBody = new HashMap<>();
        Map<String, Object> input = new HashMap<>();
        input.put("text", ttsdto.getContent());
        requestBody.put("input", input);

        Map<String, Object> voice = new HashMap<>();
        voice.put("name", ttsdto.getVoice());
        voice.put("languageCode", "ko-KR"); // languageCode 추가
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
        System.out.printf("TtsService: TTS took %.2f seconds%n", (double)(endTime - startTime)/1000);
        String audioContent = (String) response.getBody().get("audioContent");
        if (audioContent == null) {
            throw new IllegalStateException("Audio content is null. Check TTS API response.");
        }

        byte[] audioBytes = Base64.getDecoder().decode(audioContent);
        System.out.println("Audio Bytes Length: " + audioBytes.length);

        return TtsTempDto.builder()
                .audioBytes(audioBytes)
                .build();
    }

    /*
    private String makeAudioFile (byte[] audioBytes) {
        if (audioBytes == null || audioBytes.length == 0) {
            throw new IllegalArgumentException("Audio bytes cannot be null or empty.");
        }
        MultipartFile multipartFile = new MockMultipartFile(
                UUID.randomUUID() + ".mp3",
                UUID.randomUUID() + ".mp3",
                "audio/mpeg",
                audioBytes);
        return fileService.uploadFile(multipartFile);
    }

     */


    public GetDto getCount(Long ttsId, Member member) {
        Tts tts = ttsRepository.findById(ttsId)
                .orElseThrow(() -> new NoSuchElementException("No TTS ID: " + ttsId));

        String scriptContent = tts.getContent();
        if (scriptContent == null || scriptContent.trim().isEmpty()) {
            throw new IllegalArgumentException("Script content cannot be null or empty.");

        }
        System.out.println("TTS Content: " + tts.getContent());


        // TTS 생성 메서드를 사용하여 음성을 생성
        VoiceCode voiceCode = VoiceCode.fromValue("ko-KR-Standard-A");
        TtsResultDto ttsResult = createSpeech(voiceCode.getValue(), scriptContent); // TTSResultDTO로 받기
        byte[] audioBytes = ttsResult.getAudioBytes();

        return GetDto.builder()
                .context(scriptContent)
                .audioBytes(audioBytes) // 직접 TTS 결과에서 오디오 파일 경로 사용
                .memberId(member.getId())
                .build();
    }

    public GetDto getContent(Long exerciseId, Integer ttsIdx, Member member) {
        Tts tts = ttsRepository.findByExercise_IdAndTtsIdx(exerciseId, ttsIdx)
                .orElseThrow(() -> new NoSuchElementException("No TTS found for exercise ID: " + exerciseId + " and TTS index: " + ttsIdx));

        String scriptContent = tts.getContent();
        if (scriptContent == null || scriptContent.trim().isEmpty()) {
            throw new IllegalArgumentException("Script content cannot be null or empty.");

        }
        System.out.println("TTS Content: " + tts.getContent());


        // TTS 생성 메서드를 사용하여 음성을 생성
        VoiceCode voiceCode = VoiceCode.fromValue("ko-KR-Wavenet-B");
        TtsResultDto ttsResult = createSpeech(voiceCode.getValue(), scriptContent); // TTSResultDTO로 받기
        byte[] audioBytes = ttsResult.getAudioBytes();

        return GetDto.builder()
                .context(scriptContent)
                .audioBytes(audioBytes) // 직접 TTS 결과에서 오디오 파일 경로 사용
                .memberId(member.getId())
                .build();
    }
}
