package org.fablewhirl.authservice.service;

import lombok.RequiredArgsConstructor;
import org.fablewhirl.authservice.config.KeycloakProperties;
import org.fablewhirl.authservice.request.KeycloakTokenResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
}
