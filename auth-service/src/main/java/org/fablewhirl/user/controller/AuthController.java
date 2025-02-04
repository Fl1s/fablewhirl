package org.fablewhirl.user.controller;

import lombok.RequiredArgsConstructor;
import org.fablewhirl.user.event.UserLoginEvent;
import org.fablewhirl.user.event.UserRegisteredEvent;
import org.fablewhirl.user.listener.UserEventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserEventListener userEventListener;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisteredEvent event) {
        return userEventListener.handleUserRegistration(event);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginEvent event) {
        return userEventListener.handleUserLogin(event);
    }
}
