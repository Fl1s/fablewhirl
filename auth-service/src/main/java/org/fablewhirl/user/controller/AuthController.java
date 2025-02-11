package org.fablewhirl.user.controller;

import lombok.RequiredArgsConstructor;
import org.fablewhirl.user.event.UserLoginEvent;
import org.fablewhirl.user.event.UserLogoutEvent;
import org.fablewhirl.user.event.UserRegistrationEvent;
import org.fablewhirl.user.event.UserRemoveEvent;
import org.fablewhirl.user.listener.AuthEventListener;
import org.fablewhirl.user.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JwtDecoder jwtDecoder;

    private final AuthEventListener userEventListener;
    private final AuthService authService;

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> signUp(@RequestBody UserRegistrationEvent event) {
        return ResponseEntity.ok(userEventListener.handleUserRegistration(event));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody UserLoginEvent event) {
        return userEventListener.handleUserLogin(event);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody UserLogoutEvent event) {
        authService.logoutUser(event.getRefreshToken());
        return ResponseEntity.ok("[User is successfully logged out!]");
    }


    @PostMapping("/remove-user")
    public ResponseEntity<?> removeUser(@RequestBody UserRemoveEvent event) {
        userEventListener.handleUserRemove(event);
        return ResponseEntity.ok("[User with ID: " + event.getUserId() + "successfully removed!]");
    }
}
