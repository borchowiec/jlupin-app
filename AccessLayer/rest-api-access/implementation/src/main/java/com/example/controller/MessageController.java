package com.example.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    @PostMapping("/add-message")
    public ResponseEntity<?> addMessage() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
