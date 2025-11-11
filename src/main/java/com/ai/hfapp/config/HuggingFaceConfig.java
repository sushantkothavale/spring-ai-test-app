package com.ai.hfapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HuggingFaceConfig {

    @Value("${huggingface.chat.api-key}")
    private String apiKey;

    @Value("${huggingface.chat.url}")
    private String url;

    @Value("${huggingface.chat.model}")
    private String model;

    @Bean
    public CustomHuggingfaceChatModel huggingfaceChatModel() {
        return new CustomHuggingfaceChatModel(apiKey, url, model);
    }
}