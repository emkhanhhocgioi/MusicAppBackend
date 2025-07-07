package com.example.demo.Controllers;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.*;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.web.client.RestTemplate;

import com.example.demo.Models.Users;
import com.example.demo.Repostories.UserRepostory;
import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/spotify")
public class SportifyController {


   @Autowired
    private  UserRepostory userRepostory;

     @Value("${spotify_id}")
    private String spotifyId;

    @Value("${spotify_secret}")
    private String spotifySecret;
     
    private String cachedToken = null;
    private long tokenExpiryTime = 0;

  

    @GetMapping("/search")
    public ResponseEntity<String> searchArtis(@RequestParam String userid, @RequestParam String query) {
        String token = getCachedAccessToken();
        String response = searchMusicByTrack(userid, token, query);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/recent")
    public ResponseEntity<String> getRecents(@RequestParam String userid ) {
          String token = getCachedAccessToken();
          String response = getRecentSongs(token, userid);
          return ResponseEntity.ok(response);
    }
    


    public String searchMusicByTrack (String userid,String token ,String query ){

         Users user = userRepostory.findById(userid).orElse(null);

        if (user != null) {
                List<Users.RecentSearch> recentSearches = user.getRecentSearches();
                recentSearches.add(new Users.RecentSearch(query));
                if (recentSearches.size() > 3) {
                    recentSearches = recentSearches.subList(recentSearches.size() - 3, recentSearches.size());
                }
                String response = userRepostory.save(user).toString();
                System.out.println("Recent searches updated: " + response);
                
        }


        String url = "https://api.spotify.com/v1/search?q=" + query + "&type=track&limit=6";
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
        java.util.List<java.util.Map<String, Object>> tracks = new java.util.ArrayList<>();
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.JsonNode root = mapper.readTree(responseBody);
            com.fasterxml.jackson.databind.JsonNode items = root.path("tracks").path("items");
            if (items.isArray()) {
                for (com.fasterxml.jackson.databind.JsonNode item : items) {
                    java.util.Map<String, Object> track = new java.util.HashMap<>();
                    track.put("id", item.path("id").asText());
                    track.put("title", item.path("name").asText());
                    track.put("artist", item.path("artists").get(0).path("name").asText());
                    track.put("album", item.path("album").path("name").asText());
                    track.put("duration", item.path("duration_ms").asInt());
                    track.put("externalUrl", item.path("external_urls").path("spotify").asText());
                    com.fasterxml.jackson.databind.JsonNode images = item.path("album").path("images");
                    String coverUrl = images.isArray() && images.size() > 0 ? images.get(0).path("url").asText() : "";
                    track.put("coverUrl", coverUrl);
                    tracks.add(track);
                }
            }
        } catch (Exception e) {
            System.out.println("Error parsing track info: " + e.getMessage());
        }
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            return mapper.writeValueAsString(tracks);
        } catch (Exception e) {
            return "[]";
        }
    }

    
 
    public String getRecentSongs(String token, String userId) {
    Users user = userRepostory.findById(userId).orElse(null);
    java.util.List<java.util.Map<String, Object>> tracks = new java.util.ArrayList<>();
    if (user != null) {
        List<Users.RecentPlay> recentPlays = user.getRecentPlays();
        for (Users.RecentPlay song : recentPlays) {
            try {
                
                String url = "https://api.spotify.com/v1/tracks/" + song.getSongId();
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
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                com.fasterxml.jackson.databind.JsonNode item = mapper.readTree(responseBody);

                java.util.Map<String, Object> track = new java.util.HashMap<>();
                track.put("id", item.path("id").asText());
                track.put("title", item.path("name").asText());
                track.put("artist", item.path("artists").get(0).path("name").asText());
                track.put("album", item.path("album").path("name").asText());
                track.put("duration", item.path("duration_ms").asInt());
                track.put("externalUrl", item.path("external_urls").path("spotify").asText());
                com.fasterxml.jackson.databind.JsonNode images = item.path("album").path("images");
                String coverUrl = images.isArray() && images.size() > 0 ? images.get(0).path("url").asText() : "";
                track.put("coverUrl", coverUrl);
                tracks.add(track);
            } catch (Exception e) {
                System.out.println("Error fetching track info: " + e.getMessage());
            }
        }
    }
    try {
        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        return mapper.writeValueAsString(tracks);
    } catch (Exception e) {
        return "[]";
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
   

    
    

    

