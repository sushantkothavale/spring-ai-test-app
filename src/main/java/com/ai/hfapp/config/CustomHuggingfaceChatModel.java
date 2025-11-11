package com.ai.hfapp.config;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.huggingface.HuggingfaceChatModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

/**
 * Custom HuggingfaceChatModel to send model name in request body for Interface Provider usage.
 */
public class CustomHuggingfaceChatModel extends HuggingfaceChatModel {

    private final String modelName;
    private final WebClient webClient;

    public CustomHuggingfaceChatModel(String apiKey, String url, String modelName) {
        super(apiKey, url);
        this.modelName = modelName;
        this.webClient = WebClient.builder()
                .baseUrl(url)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public ChatResponse call(Prompt prompt) {
        // Prepare request body with explicit model and message as required by Interface Provider
        Map<String, Object> requestBody = Map.of(
                "model", modelName,
                "messages", List.of(
                        Map.of("role", "user", "content", prompt.toString())
                )
        );

        // Synchronously send POST request and get response mapped to HuggingfaceResponse DTO
        HuggingfaceResponse response = webClient.post()
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(HuggingfaceResponse.class)
                .block();

        // Extract generated chat text from response
        String generatedText = (response != null && !response.choices.isEmpty())
                ? response.choices.get(0).message.content
                : "No response from model";

        // Wrap generated text in Spring AI ChatResponse using list of Generation
        Generation generation = new Generation(new AssistantMessage(generatedText));
        return new ChatResponse(List.of(generation));
    }

    // DTO classes to map Huggingface Interface Provider response JSON
    private static class HuggingfaceResponse {
        public List<Choice> choices;
    }

    private static class Choice {
        public Message message;
    }

    private static class Message {
        public String content;
    }
}