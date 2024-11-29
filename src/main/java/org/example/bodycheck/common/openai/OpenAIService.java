package org.example.bodycheck.common.openai;

import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.example.bodycheck.common.openai.dto.Message;
import org.example.bodycheck.common.openai.dto.MessageDTO;
import org.example.bodycheck.common.openai.dto.OpenAIRequestDTO;
import org.example.bodycheck.common.openai.dto.OpenAIResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OpenAIService {

    private final RestTemplate restTemplate;

    private final String model = "gpt-4o-mini"; // "gpt-3.5-turbo", "gpt-4o-mini", "gpt-4o"
    private final String url = "https://api.openai.com/v1/chat/completions";

    @Transactional
    public String chat(String prompt) {
        Message message = new MessageDTO.TextMessage("user", prompt);

        List<Message> messages = new ArrayList<>();
        messages.add(message);
        OpenAIRequestDTO.ChatGPTRequestDTO request = new OpenAIRequestDTO.ChatGPTRequestDTO(model, messages);

        Long startTime = System.currentTimeMillis();
        OpenAIResponseDTO.ChatGPTResponseDTO response = restTemplate.postForObject(url, request, OpenAIResponseDTO.ChatGPTResponseDTO.class);
        Long endTime = System.currentTimeMillis();
        System.out.printf("OpenAIService: Response took %.2f seconds%n", (double)(endTime - startTime)/1000);

        String content = response.getChoices().get(0).getMessage().getContent();

        return content;
    }

    @Transactional
    public String vision(MultipartFile image, String prompt) throws IOException {
        String base64Image = Base64.encodeBase64String(image.getBytes());
        String imageUrl = "data:image/jpeg;base64," + base64Image;

        OpenAIRequestDTO.TextContent textContent = new OpenAIRequestDTO.TextContent("text", prompt);
        OpenAIRequestDTO.ImageContent imageContent = new OpenAIRequestDTO.ImageContent("image_url", new OpenAIRequestDTO.ImageUrl(imageUrl));

        Message message = new OpenAIRequestDTO.ImageMessage("user", List.of(textContent, imageContent));

        List<Message> messages = new ArrayList<>();
        messages.add(message);
        OpenAIRequestDTO.ChatGPTRequestDTO request = new OpenAIRequestDTO.ChatGPTRequestDTO(model, messages);

        OpenAIResponseDTO.ChatGPTResponseDTO response = restTemplate.postForObject(url, request, OpenAIResponseDTO.ChatGPTResponseDTO.class);

        String content = response.getChoices().get(0).getMessage().getContent();

        return content;
    }
}
