package org.fablewhirl.user.controller;

import lombok.RequiredArgsConstructor;
import org.fablewhirl.user.dto.UserLoginDto;
import org.fablewhirl.user.dto.UserRegistrationDto;
import org.fablewhirl.user.dto.UserRemoveDto;
import org.fablewhirl.user.producer.AuthProducer;
import org.fablewhirl.user.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthProducer userEventProducer;
    private final AuthService authService;

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> signUp(@RequestBody UserRegistrationDto dto) {
        userEventProducer.handleUserRegistration(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody UserLoginDto dto) {
        return ResponseEntity.ok(authService.handleUserLogin(dto));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal Jwt jwt) {
        authService.logoutUser(jwt.getSubject());
        return ResponseEntity.ok("[User successfully logged out from all sessions.]");
    }

    @PostMapping("/remove-user")
    public ResponseEntity<?> removeUser(@RequestBody UserRemoveDto dto) {
        userEventProducer.handleUserRemove(dto);
        return ResponseEntity.ok("[User with ID: " + dto.getUserId() + "successfully removed!]");
    }
}


