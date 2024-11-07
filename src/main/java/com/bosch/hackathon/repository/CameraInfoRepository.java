package com.bosch.hackathon.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
public class CameraInfoRepository {

    public int getQueueCounter() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("src/main/resources/result.txt"));
            if (!lines.isEmpty()) {
                String lastLine = lines.get(lines.size() - 1);
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(lastLine);
                return jsonNode.get("Data").get("Count").asInt();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
