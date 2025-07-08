package com.example.demo.Models;
// import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Date;
import java.util.List;



@Document(collection = "users")
public class Users {
    
    private String id;
    private String username;
    private String email;
    private String passwordHash;
    private String Avatar_url;
    private List<Favorites> favorites;
    private List<RecentPlay> recentPlays;
    private List<RecentSearch> recentSearches;

    public List<RecentSearch> getRecentSearches() {
        return recentSearches;
    }

    public void setRecentSearches(List<RecentSearch> recentSearches) {
        this.recentSearches = recentSearches;
    }
    public static class RecentSearch {
        private String query;

        public RecentSearch() {}

        public RecentSearch(String query) {
            this.query = query;
        }

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }
    }
    private Date createdAt;
    private Date updatedAt;
    private String spotifyToken;
    
     

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
        this.recentPlays = new java.util.ArrayList<>();
        this.favorites = new java.util.ArrayList<>();
        this.recentSearches = new java.util.ArrayList<>();
        this.updatedAt = new Date(System.currentTimeMillis());
        this.spotifyToken = "";

    }
    

    
    public String getId() {
        return id;
    }
    
    public class ReturnUsers {
        private String id;
        private String username;
        private String email;
        private String avatarUrl;
        private List<Favorites> favorites;
        private List<RecentPlay> recentPlays;
        private List<RecentSearch> recentSearches;

        public ReturnUsers(String id, String username, String email, String avatarUrl, List<Favorites> favorites, List<RecentPlay> recentPlays, List<RecentSearch> recentSearches) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.avatarUrl = avatarUrl;
            this.favorites = favorites;
            this.recentPlays = recentPlays;
            this.recentSearches = recentSearches;
        }

        public String getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public List<Favorites> getFavorites() {
            return favorites;
        }

        public List<RecentPlay> getRecentPlays() {
            return recentPlays;
        }

        public List<RecentSearch> getRecentSearches() {
            return recentSearches;
        }
    }

    public ReturnUsers toReturnUsers() {
        return new ReturnUsers(
            id,
            username,
            email,
            Avatar_url,
            favorites != null ? favorites : new java.util.ArrayList<>(),
            recentPlays != null ? recentPlays : new java.util.ArrayList<>(),
            recentSearches != null ? recentSearches : new java.util.ArrayList<>()
        );
    }
     public List<Favorites> getFavorites() {
        return favorites;
    }
    public void setFavorites(List<Favorites> favorites) {
    this.favorites = favorites;
    }
    
    
    public void setSpotifyToken(String spotifyToken) {
        this.spotifyToken = spotifyToken;
    }
    public String getSpotifyToken() {
        return spotifyToken;
    }

    public void setId(String id) {
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
        private String id;

        public Favorites() {}

        public Favorites(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public static class RecentPlay {
        private String songId;

        public RecentPlay() {}

        public RecentPlay(String songId) {
            this.songId = songId;
        }

        public String getSongId() {
            return songId;
        }

        public void setSongId(String songId) {
            this.songId = songId;
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