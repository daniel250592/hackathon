package com.bosch.hackathon.controllers;

import com.bosch.hackathon.controller.OllamaAiClient;
import com.bosch.hackathon.models.QueueInfoDto;
import com.bosch.hackathon.services.QueueService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QueueController {

    public final QueueService queueService;
    public final OllamaAiClient ollamaAiClient;

    public QueueController(QueueService queueService, OllamaAiClient ollamaAiClient) {
        this.queueService = queueService;
        this.ollamaAiClient = ollamaAiClient;
    }


    @CrossOrigin("http://localhost:3000")
    @GetMapping("/queue")
    public QueueInfoDto getCurrentWaitingTime() {
        return queueService.getCurrentWaitingTime();
    }
}
