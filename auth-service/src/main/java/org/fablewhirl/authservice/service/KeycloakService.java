package org.fablewhirl.authservice.service;

import lombok.RequiredArgsConstructor;
import org.fablewhirl.authservice.config.KeycloakProperties;
import org.fablewhirl.authservice.dto.UserDto;
import org.fablewhirl.authservice.event.RegistrationEvent;
import org.fablewhirl.authservice.response.KeycloakTokenResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KeycloakService {
    private final KeycloakProperties keycloakProperties;
    private final RestTemplate restTemplate;

    public KeycloakTokenResponse getTokens(String username, String password) {
        String url = keycloakProperties.getServerUrl() + "/realms/" + keycloakProperties.getRealm() + "/protocol/openid-connect/token";

        String requestBody = "client_id=" + keycloakProperties.getClientId() +
                "&client_secret=" + keycloakProperties.getClientSecret() +
                "&grant_type=password" +
                "&username=" + username +
                "&password=" + password;

        return restTemplate.postForObject(url, requestBody, KeycloakTokenResponse.class);
    }

    public KeycloakTokenResponse refreshTokens(String refreshToken) {
        String url = keycloakProperties.getServerUrl() + "/realms/" + keycloakProperties.getRealm() + "/protocol/openid-connect/token";

        String requestBody = "client_id=" + keycloakProperties.getClientId() +
                "&client_secret=" + keycloakProperties.getClientSecret() +
                "&grant_type=refresh_token" +
                "&refresh_token=" + refreshToken;

        return restTemplate.postForObject(url, requestBody, KeycloakTokenResponse.class);
    }

    public String registerUser(RegistrationEvent request) {
        String url = keycloakProperties.getServerUrl() + "/admin/realms/" + keycloakProperties.getRealm() + "/users";

        Map<String, Object> keycloakUser = new HashMap<>();
        keycloakUser.put("username", request.getUsername());
        keycloakUser.put("email", request.getEmail());
        keycloakUser.put("enabled", true);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("bio", request.getBio());
        keycloakUser.put("createdAt", attributes);
        keycloakUser.put("updatedAt", attributes);

        restTemplate.postForLocation(url, keycloakUser);

        return getUserIdByUsername(request.getUsername());
    }

    public String getUserIdByUsername(String username) {
        String url = keycloakProperties.getServerUrl() + "/admin/realms/" + keycloakProperties.getRealm() + "/users?username=" + username;

        UserDto[] users = restTemplate.getForObject(url, UserDto[].class);

        if (users.length > 0) {
            return users[0].getId();
        }

        throw new RuntimeException("User not found in Keycloak: " + username);
    }
}
