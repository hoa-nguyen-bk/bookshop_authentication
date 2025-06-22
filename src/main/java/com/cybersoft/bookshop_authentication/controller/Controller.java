package com.cybersoft.bookshop_authentication.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authen")
public class Controller {
    @GetMapping("/displayMessage")
    public ResponseEntity<String> showMessage(){
        return ResponseEntity.ok("Microservice 1 authen controller executed");
    }
}