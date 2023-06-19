package com.partior.starwars;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
public class StarwarsAPIController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StarwarsAPIController.class);
    private final StarwarsAPIService starwarsAPIService;

    public StarwarsAPIController(StarwarsAPIService starwarsAPIService) {
        this.starwarsAPIService = starwarsAPIService;
    }

    @GetMapping("/information")
    public ResponseEntity<Map<String, Object>> getInformation() {
        try {
            LOGGER.info("Fetching Star Wars information");
            
            Map<String, Object> response = starwarsAPIService.getEssentialInformation();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            LOGGER.error("Error while fetching Star Wars information: ", e);
            return ResponseEntity.status(500).body(Collections.singletonMap("error", e.toString()));
        }
    }
}
