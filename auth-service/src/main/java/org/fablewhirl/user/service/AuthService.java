package org.fablewhirl.user.service;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
        logger.info("[Creating Keycloak admin instance.]");
        return KeycloakBuilder.builder()
                .serverUrl(keycloakAuthServerUrl)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .realm(keycloakRealm)
                .clientId(keycloakClientId)
                .clientSecret(keycloakClientSecret)
                .build();
    }

    public String registerUser(String username, String email, String password) {
        logger.info("[Attempting to register user: " + username + "]");
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
                logger.warning("[User with this username or email already exists: " + username + "]");
                throw new RuntimeException("[User with this username or email already exists in Keycloak.]");
            }
            logger.severe("[Failed to create user in Keycloak: " + response.getStatusInfo() + "]");
            throw new RuntimeException("[Failed to create user in Keycloak: " + response.getStatusInfo() + "]");
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

        logger.info("[User successfully registered in Keycloak with ID: " + userId + "]");
        return userId;
    }

    public AccessTokenResponse authenticateUser(String username, String password) {
        logger.info("[Authenticating user: " + username + "]");
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

    public ResponseEntity<?> logoutUser(String userId) {
        logger.info("[Logging out user with ID: " + userId + "]");
        Keycloak keycloak = getAdminKeycloakInstance();
        try {
            UserResource userResource = keycloak.realm(keycloakRealm).users().get(userId);
            userResource.logout();
            logger.info("[User with ID " + userId + " has been logged out from all sessions.]");
        } catch (Exception e) {
            logger.warning("[Error while logging out user with ID " + userId + ": " + e.getMessage() + "]");
        }
        return ResponseEntity.ok().build();
    }

    public void removeUser(String userId) {
        logger.info("[Attempting to remove user with ID: " + userId + "]");
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
            throw new RuntimeException("[Failed to remove user from Keycloak: " + e.getMessage() + "]", e);
        }
    }
}
