package org.example.bodycheck.common.openai;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.openai.dto.MessageDTO;
import org.example.bodycheck.common.openai.dto.OpenAIRequestDTO;
import org.example.bodycheck.common.openai.dto.OpenAIResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OpenAIService {

    private final RestTemplate restTemplate;

    @Transactional
    public String chat(String prompt) {

        String model = "gpt-4o-mini"; // "gpt-3.5-turbo", "gpt-4o-mini", "gpt-4o"
        String url = "https://api.openai.com/v1/chat/completions";

        MessageDTO.Message message = new MessageDTO.Message("user", prompt);

        List<MessageDTO.Message> messages = new ArrayList<>();
        messages.add(message);
        OpenAIRequestDTO.ChatGPTRequestDTO request = new OpenAIRequestDTO.ChatGPTRequestDTO(model, messages);

        Long startTime = System.currentTimeMillis();
        OpenAIResponseDTO.ChatGPTResponseDTO response = restTemplate.postForObject(url, request, OpenAIResponseDTO.ChatGPTResponseDTO.class);
        Long endTime = System.currentTimeMillis();
        System.out.printf("OpenAIService: Response took %.2f seconds%n", (double)(endTime - startTime)/1000);

        String content = response.getChoices().get(0).getMessage().getContent();

        return content;

    }
}
