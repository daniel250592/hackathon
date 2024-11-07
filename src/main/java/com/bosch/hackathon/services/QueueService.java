package com.bosch.hackathon.services;

import com.bosch.hackathon.models.QueueInfoDto;
import com.bosch.hackathon.repository.CameraInfoRepository;
import org.springframework.stereotype.Service;

@Service
public class QueueService {

    private final int SERVICE_TIME_IN_SECONDS = 30;
    private final int SERVICE_PEOPLE = 2;

    public final CameraInfoRepository cameraInfoRepository;

    public QueueService(CameraInfoRepository cameraInfoRepository) {
        this.cameraInfoRepository = cameraInfoRepository;
    }


    public QueueInfoDto getCurrentWaitingTime() {
        return new QueueInfoDto(calculateWaitingTimeInMinutes(
                cameraInfoRepository.getQueueCounter(), SERVICE_PEOPLE),
                cameraInfoRepository.getQueueCounter());

    }

    public int calculateWaitingTimeInMinutes(int peopleInQueue, int peopleGivingOutMeals) {
        int extraTime = (peopleGivingOutMeals > 1) ? peopleGivingOutMeals * 15 : 0;
        return peopleInQueue * (SERVICE_TIME_IN_SECONDS + extraTime) / 60;
    }
}
