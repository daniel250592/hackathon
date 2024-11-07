package com.bosch.hackathon.repository;

import com.bosch.hackathon.models.PeopleCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeopleCountRepository extends JpaRepository<PeopleCount, Long> {


    PeopleCount findTopByOrderByTimeDesc();
}