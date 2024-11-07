package com.bosch.hackathon.repository;

import com.bosch.hackathon.models.PeopleCount;
import org.springframework.stereotype.Service;

@Service
public class CameraInfoRepository {

    private final PeopleCountRepository peopleCountRepository;

    public CameraInfoRepository(PeopleCountRepository peopleCountRepository) {
        this.peopleCountRepository = peopleCountRepository;
    }

    public int getQueueCounter() {
        PeopleCount latestCount = peopleCountRepository.findTopByOrderByTimeDesc();
        return latestCount != null ? latestCount.getPeople() : 0;
    }

}
