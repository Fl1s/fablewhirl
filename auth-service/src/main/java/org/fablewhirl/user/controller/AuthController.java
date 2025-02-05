package org.fablewhirl.user.controller;

import lombok.RequiredArgsConstructor;
import org.fablewhirl.user.event.UserLoginEvent;
import org.fablewhirl.user.event.UserRegisteredEvent;
import org.fablewhirl.user.listener.AuthEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthEventListener userEventListener;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisteredEvent event) {
        logger.info("Received registration request for username: {}", event.getUsername());
        return userEventListener.handleUserRegistration(event);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginEvent event) {
        return userEventListener.handleUserLogin(event);
    }
}
