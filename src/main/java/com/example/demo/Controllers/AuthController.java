package com.example.demo.Controllers;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Repostories.*;
import com.example.demo.Models.Users;

import com.example.demo.dto.RegisterRequest;



@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepostory userRepostory;


    
    @PostMapping("/register")
    public ResponseEntity<?> CreateNewAccount(@RequestBody RegisterRequest request) {
        try {
            System.out.println(request.getEmail() + ", " + request.getPassword() + ", " + request.getUsername());
            Users user = new Users(request.getUsername(), request.getPassword(), request.getEmail());
            userRepostory.insert(user);
            return new ResponseEntity<Users>(user, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    
}
