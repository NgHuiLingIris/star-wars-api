package com.partior.starwars;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StarwarsAPIService {

    private static final String BASE_URL = "https://swapi.dev/api/";
    private static final String LEIA_HOMEWORLD_URL = "https://swapi.dev/api/planets/2/";
    private static final String UNKNOWN_CREW_COUNT = "unknown";
    
    private final RestTemplate restTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(StarwarsAPIService.class);

    public StarwarsAPIService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    protected Map<String, Object> getEssentialInformation() {
        Map<String, Object> darthVaderData = fetchData("people/4/");
        Map<String, Object> response = new LinkedHashMap<>();

        String starshipUrl = getDarthVaderStarshipUrl(darthVaderData);
        if (starshipUrl == ""){
            response.put("starship", Collections.emptyMap());
        } else {
            Map<String, Object> starshipData = fetchStarshipData(starshipUrl);
            response.put("starship", getStarshipData(starshipData));
        }
        
        Map<String, Object> deathStarData = fetchData("starships/9/");
        Map<String, Object> leiaData = fetchData("people/5/");
        
        response.put("crew", getCrewCount(deathStarData));
        response.put("isLeiaOnPlanet", isLeiaOnPlanet(leiaData));
        
        return response;
    }

    private Map fetchData(String path) {
        LOGGER.info("Fetching data for path: {}", path);
        return restTemplate.getForObject(BASE_URL + path, Map.class);
    }

    private Map<String, String> getStarshipData(Map<String, Object> starshipData) {
        Map<String, String> starship = new LinkedHashMap<>();
        starship.put("name", (String) starshipData.get("name"));
        starship.put("class", (String) starshipData.get("starship_class"));
        starship.put("model", (String) starshipData.get("model"));
        return starship;
    }

    private int getCrewCount(Map<String, Object> deathStarData) {
        String crewCount = (String) deathStarData.get("crew");
        if (UNKNOWN_CREW_COUNT.equals(crewCount)) {
            return 0;
        }
        if(crewCount != null) {
            crewCount = crewCount.replace(",", "");
            return Integer.parseInt(crewCount);
        }
        return 0;
    }

    private boolean isLeiaOnPlanet(Map<String, Object> leiaData) {
        String leiaHomeWorld = (String) leiaData.get("homeworld");
        return LEIA_HOMEWORLD_URL.equals(leiaHomeWorld);
    }

    private String getDarthVaderStarshipUrl(Map<String, Object> darthVaderData) {
        List<String> starships = (List<String>) darthVaderData.get("starships");
        return starships.isEmpty() ? "" : starships.get(0);
    }

    private Map<String, Object> fetchStarshipData(String starshipUrl) {
        return starshipUrl.isEmpty() ? new LinkedHashMap<>() : restTemplate.getForObject(starshipUrl, Map.class);
    }
}
