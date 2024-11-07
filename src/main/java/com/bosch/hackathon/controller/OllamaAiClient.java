package com.bosch.hackathon.controller;

import com.bosch.hackathon.models.PeopleCount;
import com.bosch.hackathon.repository.PeopleCountRepository;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class OllamaAiClient {

    private final ChatClient chatClient;


    private final PeopleCountRepository repository;

    @Value("classpath:/prompts/analyze.st")
    private Resource prompt;

    public OllamaAiClient(ChatClient chatClient, PeopleCountRepository repository) {
        this.chatClient = chatClient;
        this.repository = repository;
    }

    @CrossOrigin("http://localhost:3000")
    @GetMapping("/ai")
    public String ai(
            @RequestParam(value = "question", required = false, defaultValue = "When is the best time to visit location?")
            String question
    ) {
        return this.analyzeData(question);
    }

    public String analyzeData(String question) {

        PromptTemplate template = new PromptTemplate(prompt);
        Map<String, Object> map = new HashMap<>();



        var fiveMinuteIntervals = repository.findAll().stream()
                .collect(Collectors.groupingBy(pc -> pc.getTime().truncatedTo(ChronoUnit.MINUTES).withMinute(pc.getTime().getMinute() / 5 * 5)))
                .entrySet().stream()
                .map(entry -> {
                    double averageCount = entry.getValue().stream().mapToInt(PeopleCount::getPeople).average().orElse(0);
                    return Map.entry(entry.getKey(), averageCount);
                }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                .entrySet()
                .stream()
                .map(entry -> "{ \"time\": \"" + entry.getKey().toString() + "\", \"people\": " + entry.getValue() + " }")
                .toList();


        map.put("data", fiveMinuteIntervals);
        map.put("question", question);

        var p = template.create(map);
        var r = chatClient.call(p);

        return r.getResult().getOutput().getContent();
    }
}
