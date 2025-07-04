package com.example.demo.Models;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Date;
import java.util.List;



@Document(collection = "users")
public class Users {
    private ObjectId id;
    private String username;
    private String email;
    private String passwordHash;
    private String Avatar_url;
    private List<Favorites> favorites;
    private List<RecentPlay> recentPlays;
    private Date createdAt;
    private Date updatedAt;
     

    public Users () {

    };

    public Users (String username, String password, String email) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashpasss = encoder.encode(password);
        this.email = email;
        this.username = username;
        this.passwordHash = hashpasss;
        this.createdAt = new Date(System.currentTimeMillis());
        this.Avatar_url = "";
        this.recentPlays = null;
        this.favorites = null;
        this.updatedAt = new Date(System.currentTimeMillis());


    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    // Xóa getter/setter không sử dụng cho profile và favorites

    public List<RecentPlay> getRecentPlays() {
        return recentPlays;
    }

    public void setRecentPlays(List<RecentPlay> recentPlays) {
        this.recentPlays = recentPlays;
    }


    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static class Profile {
        private String name;
        private String avatarUrl;
        private String birthdate; // ISO format (yyyy-MM-dd)
        private String gender;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public String getBirthdate() {
            return birthdate;
        }

        public void setBirthdate(String birthdate) {
            this.birthdate = birthdate;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }
    }

    public static class Favorites {
        private List<ObjectId> songs;
        private List<ObjectId> artists;

        public List<ObjectId> getSongs() {
            return songs;
        }

        public void setSongs(List<ObjectId> songs) {
            this.songs = songs;
        }

        public List<ObjectId> getArtists() {
            return artists;
        }

        public void setArtists(List<ObjectId> artists) {
            this.artists = artists;
        }
    }

    public static class RecentPlay {
        private ObjectId songId;
        private Date playedAt;

        public ObjectId getSongId() {
            return songId;
        }

        public void setSongId(ObjectId songId) {
            this.songId = songId;
        }

        public Date getPlayedAt() {
            return playedAt;
        }

        public void setPlayedAt(Date playedAt) {
            this.playedAt = playedAt;
        }
    }

    public static class AIPreferences {
        private List<String> genres;
        private List<String> moods;

        public List<String> getGenres() {
            return genres;
        }

        public void setGenres(List<String> genres) {
            this.genres = genres;
        }

        public List<String> getMoods() {
            return moods;
        }

        public void setMoods(List<String> moods) {
            this.moods = moods;
        }
    }

   
}