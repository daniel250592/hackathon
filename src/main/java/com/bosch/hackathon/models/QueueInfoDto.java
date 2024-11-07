package com.bosch.hackathon.models;

public record QueueInfoDto(
        int waitingTimeEstimation,
        int queueLength) {
}
