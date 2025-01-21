package org.fablewhirl.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.fablewhirl.authservice.dto.TokenDto;
import org.fablewhirl.authservice.dto.RegisterDto;
import org.fablewhirl.authservice.request.LoginRequest;
import org.fablewhirl.authservice.event.RegistrationEvent;
import org.fablewhirl.authservice.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public TokenDto login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/refresh-token")
    @ResponseStatus(HttpStatus.OK)
    public TokenDto refreshToken(@RequestParam String username, @RequestParam String refreshToken) {
        return authService.refreshToken(username, refreshToken);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RegisterDto> register(@RequestBody RegistrationEvent registerRequest) {
        return authService.register(registerRequest);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout(@RequestParam String username, @RequestParam String refreshToken) {
        authService.logout(username);
    }
}
