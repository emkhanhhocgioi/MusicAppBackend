package com.example.demo.Controllers;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.crypto.bcrypt.BCrypt;
import com.example.demo.Repostories.*;
import com.example.demo.Models.Users;
import com.example.demo.Models.Users.ReturnUsers;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.LoginRequest;
import com.example.demo.Controllers.SportifyController;;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepostory userRepostory;
    
    @Autowired
    private SportifyController sportifyController;



    @PostMapping("/login")
    public ResponseEntity<?> postMethodName(@RequestBody LoginRequest request) {
        try {
            System.out.println(request.getEmail() + ", " + request.getPassword());
            if (request.getEmail() == null || request.getPassword() == null) {
                return new ResponseEntity<>("Email and password are required", HttpStatus.BAD_REQUEST);
            }
            Users user = userRepostory.findByEmail(request.getEmail());
            if (user == null) {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
            
            if (!BCrypt.checkpw(request.getPassword(), user.getPasswordHash())) {
                return new ResponseEntity<>("Invalid password", HttpStatus.UNAUTHORIZED);
            }



            return new ResponseEntity<>("Sign in successful", HttpStatus.OK); 
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    
    }
    

    
    @PostMapping("/register")
    public ResponseEntity<?> CreateNewAccount(@RequestBody RegisterRequest request) {
        try {
            System.out.println(request.getEmail() + ", " + request.getPassword() + ", " + request.getUsername());
            Users user = new Users(request.getUsername(), request.getEmail(),request.getPassword() );
            userRepostory.insert(user);
            return new ResponseEntity<Users>(user, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    
}
