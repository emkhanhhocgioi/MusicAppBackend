package com.example.demo.Controllers;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.*;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/spotify")
public class SportifyController {
    // Remove static token and constructor


     @Value("${spotify_id}")
    private String spotifyId;

    @Value("${spotify_secret}")
    private String spotifySecret;
     
    private String cachedToken = null;
    private long tokenExpiryTime = 0;

    @GetMapping("/search")
    public ResponseEntity<String> searchArtis(@RequestParam String query) {
        String token = getCachedAccessToken();
        String response = searchMusicByTrack(token, query);
        
        return ResponseEntity.ok(response);
    }


    public String searchMusicByTrack (String token ,String query ){
        String url = "https://api.spotify.com/v1/search?q=" + query + "&type=track&limit=5";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            String.class
        );
        String responseBody = response.getBody();
        List<String> names = new ArrayList<String>();
        List<String> artists = new ArrayList<String>();

        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.JsonNode root = mapper.readTree(responseBody);
            com.fasterxml.jackson.databind.JsonNode items = root.path("tracks").path("items");
            if (items.isArray()) {
                for (com.fasterxml.jackson.databind.JsonNode item : items) {
                    String name = item.path("name").asText();
                    String artis = item.path("artists").get(0).path("name").asText();
                    artists.add(artis);
                    names.add(name);
                }
            }
        } catch (Exception e) {
            System.out.println("Error parsing track names: " + e.getMessage());
        }
        return mapperToJson(names, artists);
    }

    // Helper to convert list to JSON string
    private String mapperToJson(java.util.List<String> names, java.util.List<String> artists) {
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            java.util.Map<String, java.util.List<String>> map = new java.util.HashMap<>();
            map.put("names", names);
            map.put("artists", artists);
            return mapper.writeValueAsString(map);
        } catch (Exception e) {
            return names.toString();
        }
    }
    

    
    public String getCachedAccessToken() {
        long now = System.currentTimeMillis();
        if (cachedToken == null || now >= tokenExpiryTime) {
            String auth = spotifyId + ":" + spotifySecret;
            String encodeAuth = Base64.getEncoder().encodeToString(auth.getBytes());

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Basic " + encodeAuth);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            String body = "grant_type=client_credentials";
            HttpEntity<String> request = new HttpEntity<>(body, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(
                "https://accounts.spotify.com/api/token", request, String.class);

            String responseBody = response.getBody();
            String accessToken = null;
            int expiresIn = 3600; // default 1 hour
            try {
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                java.util.Map<String, Object> map = mapper.readValue(responseBody, java.util.Map.class);
                accessToken = (String) map.get("access_token");
                Object expiresObj = map.get("expires_in");
                if (expiresObj != null) {
                    expiresIn = ((Number) expiresObj).intValue();
                }
            } catch (Exception e) {
                System.out.println("Error parsing access_token: " + e.getMessage());
            }
            cachedToken = accessToken;
            tokenExpiryTime = now + (expiresIn - 60) * 1000L; // renew 1 min before expiry
            System.out.println("Spotify access_token: " + accessToken);
        }
        return cachedToken;
    }
}
   

    
    

    

