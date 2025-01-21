package org.fablewhirl.authservice.service;

import org.fablewhirl.authservice.dto.RegisterDto;
import org.fablewhirl.authservice.producer.KafkaProducer;
import org.fablewhirl.authservice.response.KeycloakTokenResponse;
import org.fablewhirl.authservice.request.LoginRequest;
import org.fablewhirl.authservice.event.RegistrationEvent;
import org.fablewhirl.authservice.dto.TokenDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final KeycloakService keycloakService;
    private final RedisCommands<String, String> redisCommands;
    private final KafkaProducer eventProducer;

    public TokenDto login(LoginRequest request) {
        KeycloakTokenResponse keycloakTokens = keycloakService.getTokens(request.getUsername(), request.getPassword());

        String redisKey = "refresh_token:" + request.getUsername();
        redisCommands.setex(redisKey, keycloakTokens.getRefreshTokenTtl(), keycloakTokens.getRefreshToken());

        return TokenDto.builder()
                .accessToken(keycloakTokens.getAccessToken())
                .refreshToken(keycloakTokens.getRefreshToken())
                .build();
    }

    public ResponseEntity<RegisterDto> register(RegistrationEvent registerRequest) {
        String userId = keycloakService.registerUser(registerRequest);

        RegistrationEvent event = RegistrationEvent.builder()
                .userId(userId)
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .bio(registerRequest.getBio())
                .createdAt(registerRequest.getCreatedAt())
                .build();

        eventProducer.sendRegistration(event);
        return null;
    }

    public TokenDto refreshToken(String username, String refreshToken) {
        String redisKey = "refresh_token:" + username;
        String storedRefreshToken = redisCommands.get(redisKey);

        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            throw new RuntimeException("Invalid or expired refresh token");
        }

        KeycloakTokenResponse newTokens = keycloakService.refreshTokens(refreshToken);

        redisCommands.setex(redisKey, newTokens.getRefreshTokenTtl(), newTokens.getRefreshToken());

        return TokenDto.builder()
                .accessToken(newTokens.getAccessToken())
                .refreshToken(newTokens.getRefreshToken())
                .build();
    }

    public void logout(String username) {
        String redisKey = "refresh_token:" + username;
        redisCommands.del(redisKey);
    }
}