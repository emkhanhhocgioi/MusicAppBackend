package com.example.demo.Controllers;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.example.demo.Models.Users;

import com.example.demo.Repostories.UserRepostory;
import com.fasterxml.jackson.databind.ObjectMapper;







@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepostory userRepostory;

    public UserController(UserRepostory userRepostory) {
        this.userRepostory = userRepostory;
    }

    @GetMapping("/detail")
    public ResponseEntity<?> getMethodName(@RequestParam String uid) {
        try {
            Users user = userRepostory.findById(uid).orElse(null);
            if (user == null) {
                return ResponseEntity.status(404).body("User not found");
            }
            
            
            return ResponseEntity.ok(user.toReturnUsers());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }

    @PutMapping("/recent-plays/add/{id}")
    public ResponseEntity addRecentPlay(@PathVariable String id, @RequestBody String songid) {
       try {
            Users user = userRepostory.findById(id).orElse(null);
        if (user != null && !user.getRecentPlays().contains(songid)  ) {
               List<Users.RecentPlay> recentPlays = user.getRecentPlays();
            recentPlays.add(new Users.RecentPlay(songid));
            if (recentPlays.size() > 3) {
                recentPlays = recentPlays.subList(recentPlays.size() - 3, recentPlays.size());
            }
            user.setRecentPlays(recentPlays);
            userRepostory.save(user);
            return ResponseEntity.ok("Recent play added successfully");
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
   
    } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }
    
    

    @PutMapping("/favorite/remove/{id}")
public ResponseEntity removeFavourite(@PathVariable String id, @RequestBody String songid) {
    try {
        Users user = userRepostory.findById(id).orElse(null);
        if (user != null) {
            List<Users.Favorites> favorites = user.getFavorites();
            boolean removed = favorites.removeIf(fav -> fav.getId().equals(songid));
            user.setFavorites(favorites);
            userRepostory.save(user);
            if (removed) {
                return ResponseEntity.ok("Song removed from favorites successfully");
            } else {
                return ResponseEntity.status(404).body("Song not found in favorites");
            }
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Internal error");
    }
}
    
    




}
