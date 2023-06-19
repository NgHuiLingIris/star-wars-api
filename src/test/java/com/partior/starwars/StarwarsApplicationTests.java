package com.partior.starwars;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class StarwarsApplicationTests {

    private StarwarsAPIController controller;
    private StarwarsAPIService starwarsAPIService;
    
    @Mock
    private RestTemplate restTemplate;

    private Map<String, Object> darthVaderData;
	private Map<String, Object> deathStarData;
	private Map<String, Object> leiaData;
	private Map<String, Object> tieData;

    @BeforeEach
    public void setup() throws JsonParseException, JsonMappingException, IOException {
        MockitoAnnotations.openMocks(this);
        starwarsAPIService = new StarwarsAPIService(restTemplate);
        controller = new StarwarsAPIController(starwarsAPIService);

		ObjectMapper mapper = new ObjectMapper();
		darthVaderData = mapper.readValue(new File("src\\test\\java\\com\\partior\\starwars\\resources\\darthVaderData.json"), new TypeReference<Map<String,Object>>(){});
		deathStarData = mapper.readValue(new File("src\\test\\java\\com\\partior\\starwars\\resources\\deathStarData.json"), new TypeReference<Map<String,Object>>(){});
		leiaData = mapper.readValue(new File("src\\test\\java\\com\\partior\\starwars\\resources\\leiaData.json"), new TypeReference<Map<String,Object>>(){});
		tieData = mapper.readValue(new File("src\\test\\java\\com\\partior\\starwars\\resources\\tieData.json"), new TypeReference<Map<String,Object>>(){});
        String peopleUrl = "https://swapi.dev/api/people/";
        String starshipUrl = "https://swapi.dev/api/starships/";

        when(restTemplate.getForObject(peopleUrl + "4/", Map.class)).thenReturn(darthVaderData);
		when(restTemplate.getForObject(starshipUrl + "9/", Map.class)).thenReturn(deathStarData);
		when(restTemplate.getForObject(peopleUrl + "5/", Map.class)).thenReturn(leiaData);
		when(restTemplate.getForObject(starshipUrl + "13/", Map.class)).thenReturn(tieData);
    }

    @Test
    public void whenGetInformation_thenCorrectResponse() {
        ResponseEntity<Map<String, Object>> result = controller.getInformation();

        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());

        Map<String, Object> body = result.getBody();
        assertNotNull(body);
        Map<String, Object> starship = (Map<String, Object>) body.get("starship");
        assertNotNull(starship);

        assertEquals("TIE Advanced x1", starship.get("name"));
		assertEquals("Starfighter", starship.get("class"));
		assertEquals("Twin Ion Engine Advanced x1", starship.get("model"));
		assertEquals(342953, body.get("crew"));
		assertEquals(true, body.get("isLeiaOnPlanet"));
    }

    @Test
    public void whenNoStarShipFound_thenValueEmptyObject() {
        darthVaderData.put("starships", Collections.emptyList());
        ResponseEntity<Map<String, Object>> result = controller.getInformation();

        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());

        Map<String, Object> body = result.getBody();
        assertNotNull(body);
        Map<String, Object> starship = (Map<String, Object>) body.get("starship");
        assertEquals(Collections.emptyMap(), starship);

		assertEquals(342953, body.get("crew"));
		assertEquals(true, body.get("isLeiaOnPlanet"));
    }

    @Test
    public void whenNoCrew_thenCrewValueIsZero() {
        deathStarData.remove("crew");
        ResponseEntity<Map<String, Object>> result = controller.getInformation();

        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());

        Map<String, Object> body = result.getBody();
        assertNotNull(body);
        Map<String, Object> starship = (Map<String, Object>) body.get("starship");
        assertNotNull(starship);

        assertEquals("TIE Advanced x1", starship.get("name"));
		assertEquals("Starfighter", starship.get("class"));
		assertEquals("Twin Ion Engine Advanced x1", starship.get("model"));
		assertEquals(0, body.get("crew"));
		assertEquals(true, body.get("isLeiaOnPlanet"));
    }

    @Test
    public void whenPrincessLeiaNotOnPlanet_thenIsLeiaOnPlanetEqualsFalse() {
        leiaData.put("homeworld", "https://swapi.dev/api/planets/3/");

        ResponseEntity<Map<String, Object>> result = controller.getInformation();

        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());

        Map<String, Object> body = result.getBody();
        assertNotNull(body);
        Map<String, Object> starship = (Map<String, Object>) body.get("starship");
        assertNotNull(starship);

        assertEquals("TIE Advanced x1", starship.get("name"));
		assertEquals("Starfighter", starship.get("class"));
		assertEquals("Twin Ion Engine Advanced x1", starship.get("model"));
		assertEquals(342953, body.get("crew"));
		assertEquals(false, body.get("isLeiaOnPlanet"));
    }
}
