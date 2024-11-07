package com.bosch.hackathon.controllers;

import com.bosch.hackathon.models.QueueInfoDto;
import com.bosch.hackathon.services.QueueService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QueueController {

    public final QueueService queueService;

    public QueueController(QueueService queueService) {
        this.queueService = queueService;
    }


    @GetMapping("/queue")
    public QueueInfoDto getCurrentWaitingTime() {
        return queueService.getCurrentWaitingTime();
    }
}
