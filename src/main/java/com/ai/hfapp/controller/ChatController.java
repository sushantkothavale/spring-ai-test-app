package com.ai.hfapp.controller;

import com.ai.hfapp.config.CustomHuggingfaceChatModel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final CustomHuggingfaceChatModel chatModel;

    public ChatController(CustomHuggingfaceChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @PostMapping
    public String chat(@RequestBody String userMessage) {
        return chatModel.call(userMessage);
    }
}