package com.example.ccb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@SpringBootApplication
public class CcbApplication {

    public static void main(String[] args) {
        SpringApplication.run(CcbApplication.class, args);
    }

}
