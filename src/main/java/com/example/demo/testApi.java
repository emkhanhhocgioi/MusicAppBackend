package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class testApi {
     
    
    @GetMapping("/api/hello")
    public String sayHello() {
        return "Hello from Spring Boot!";
    }

}
