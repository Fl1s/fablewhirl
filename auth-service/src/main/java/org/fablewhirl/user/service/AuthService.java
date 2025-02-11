package org.fablewhirl.user.service;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${keycloak.auth-server-url}")
    private String keycloakAuthServerUrl;

    @Value("${keycloak.realm}")
    private String keycloakRealm;

    @Value("${keycloak.credentials.client-id}")
    private String keycloakClientId;

    @Value("${keycloak.credentials.secret}")
    private String keycloakClientSecret;

    private static final Logger logger = Logger.getLogger(AuthService.class.getName());

    private Keycloak getAdminKeycloakInstance() {
        return KeycloakBuilder.builder()
                .serverUrl(keycloakAuthServerUrl)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .realm(keycloakRealm)
                .clientId(keycloakClientId)
                .clientSecret(keycloakClientSecret)
                .build();
    }

    public String registerUser(String username, String email, String password) {
        Keycloak keycloak = getAdminKeycloakInstance();
        RealmResource realmResource = keycloak.realm(keycloakRealm);
        UsersResource usersResource = realmResource.users();

        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail(email);
        user.setEnabled(true);

        Response response = usersResource.create(user);
        if (response.getStatus() != 201) {
            if (response.getStatus() == 409) {
                throw new RuntimeException("User with this username or email already exists in Keycloak.");
            }
            throw new RuntimeException("Failed to create user in Keycloak: " + response.getStatusInfo());
        }

        String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        keycloak.realm(keycloakRealm).users().get(userId).resetPassword(credential);

        RoleRepresentation userRole = realmResource.roles().get("user").toRepresentation();
        keycloak.realm(keycloakRealm).users().get(userId)
                .roles().realmLevel().add(Collections.singletonList(userRole));

        logger.info("User successfully registered in Keycloak with ID: " + userId);
        return userId;
    }


    public AccessTokenResponse authenticateUser(String username, String password) {
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(keycloakAuthServerUrl)
                .realm(keycloakRealm)
                .grantType(OAuth2Constants.PASSWORD)
                .clientId(keycloakClientId)
                .clientSecret(keycloakClientSecret)
                .username(username)
                .password(password)
                .build();

        AccessTokenResponse accessTokenResponse = keycloak.tokenManager().getAccessToken();
        String accessToken = accessTokenResponse.getToken();
        String refreshToken = keycloak.tokenManager().refreshToken().getToken();

        logger.info("[Generated tokens: Access Token (valid for 1 week), Refresh Token (valid for 15 minutes)]");

        AccessTokenResponse response = new AccessTokenResponse();
        response.setToken(accessToken);
        response.setExpiresIn(604800);
        response.setNotBeforePolicy(0);
        response.setTokenType("Bearer");
        response.setScope("email userId username");

        response.setRefreshToken(refreshToken);
        response.setRefreshExpiresIn(900);
        return response;
    }

    public void logoutUser(String refreshToken) {
        String logoutUrl = keycloakAuthServerUrl + "/realms/" + keycloakRealm + "/protocol/openid-connect/logout";

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", keycloakClientId);
        formData.add("client_secret", keycloakClientSecret);
        formData.add("refresh_token", refreshToken);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(logoutUrl, formData, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            logger.info("[User successfully logged out.]");
        } else {
            logger.warning("Logout failed: " + response.getStatusCode() + " " + response.getBody() + "]");
            throw new RuntimeException("Logout failed: " + response.getStatusCode() + "]");
        }
    }

    public void removeUser(String userId) {
        Keycloak keycloak = getAdminKeycloakInstance();
        RealmResource realmResource = keycloak.realm(keycloakRealm);
        UsersResource usersResource = realmResource.users();

        try {
            if (usersResource.get(userId).toRepresentation() != null) {
                usersResource.delete(userId);
                logger.info("[User with userId: " + userId + " successfully removed from Keycloak.]");
            } else {
                logger.warning("[User with userId: " + userId + " not found in Keycloak.]");
                throw new RuntimeException("[User not found in Keycloak.]");
            }
        } catch (Exception e) {
            logger.severe("[Failed to remove user with userId: " + userId + ". Error: " + e.getMessage() + "]");
            throw new RuntimeException("Failed to remove user from Keycloak: " + e.getMessage() + "]", e);
        }
    }
}
