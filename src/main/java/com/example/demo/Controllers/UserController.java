package com.example.demo.Controllers;

import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Models.Users;

import com.example.Repostories.UserRepostory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/users")
public class UserController {

    

    @PostMapping("/auth/login")
    public String postMethodName(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }
    
    

    // Ví dụ: Đăng ký user mới
    @PostMapping("/auth/register")
    public Users registerUser(@RequestBody Users user) {
        // Demo trả về user vừa nhận
        return user;
    }


}
