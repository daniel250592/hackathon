package com.bosch.hackathon.controller;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class OllamaAiClient {

    private final ChatClient chatClient;

    @Value("classpath:/data/06_11.json")
    private Resource data;

    @Value("classpath:/prompts/analyze.st")
    private Resource prompt;

    public OllamaAiClient(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/ai/analyze")
    public String analyzeData() {

        PromptTemplate template = new PromptTemplate(prompt);
        Map<String, Object> map = new HashMap<>();
        map.put("data", data);

        var p = template.create(map);
        var r = chatClient.call(p);

        return r.getResult().getOutput().getContent();
    }
}
