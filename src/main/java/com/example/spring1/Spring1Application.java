package com.example.spring1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class Spring1Application {

    static void main(String[] args) {
        SpringApplication.run(Spring1Application.class, args);
    }

}
