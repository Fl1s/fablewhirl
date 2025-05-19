package org.fablewhirl.user.service;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.fablewhirl.user.dto.UserLoginDto;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class AuthService {
    private static final Logger logger = Logger.getLogger(AuthService.class.getName());

    @Value("${keycloak.realm}")
    private String keycloakRealm;

    private final Keycloak keycloakAdminClient;
    private final TokenCacheService tokenCacheService;


    public String registerUser(String username, String email, String password) {
        logger.info("[Attempting to register user: " + username + "]");
        RealmResource realmResource = keycloakAdminClient.realm(keycloakRealm);
        UsersResource usersResource = realmResource.users();

        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail(email);
        user.setEnabled(true);

        Response response = usersResource.create(user);
        if (response.getStatus() != 201) {
            if (response.getStatus() == 409) {
                logger.warning("[User already exists: " + username + "]");
                throw new RuntimeException("[User already exists in Keycloak.]");
            }
            logger.severe("[Failed to create user in Keycloak: " + response.getStatusInfo() + "]");
            throw new RuntimeException("[Failed to create user in Keycloak: " + response.getStatusInfo() + "]");
        }

        String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        keycloakAdminClient.realm(keycloakRealm).users().get(userId).resetPassword(credential);

        RoleRepresentation userRole = realmResource.roles().get("user").toRepresentation();
        keycloakAdminClient.realm(keycloakRealm).users().get(userId)
                .roles().realmLevel().add(Collections.singletonList(userRole));

        logger.info("[User successfully registered with ID: " + userId + "]");
        return userId;
    }

    public ResponseEntity<?> handleUserLogin(UserLoginDto dto) {
        try {
            String userId = keycloakAdminClient
                    .realm(keycloakRealm)
                    .users()
                    .search(dto.getUsername())
                    .stream()
                    .filter(user -> user.getUsername().equalsIgnoreCase(dto.getUsername()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("User not found"))
                    .getId();
            logger.info("[User login successful! Access token generated.]");
            return ResponseEntity.ok(tokenCacheService.getCachedToken(userId, dto.getUsername(), dto.getPassword()));
        } catch (Exception e) {
            logger.severe("[User login failed. Error: " + e.getMessage() + "]");
            ResponseEntity.status(401).build();
        }
        return ResponseEntity.status(401).build();
    }

    @CacheEvict(value = "AUTH_TOKEN", key = "#userId")
    public void logoutUser(String userId) {
        logger.info("[Logging out user with ID: " + userId + "]");
        try {
            UserResource userResource = keycloakAdminClient.realm(keycloakRealm).users().get(userId);
            userResource.logout();
            logger.info("[User with ID " + userId + " has been logged out.]");
        } catch (Exception e) {
            logger.warning("[Error while logging out user: " + e.getMessage() + "]");
        }
        ResponseEntity.ok().build();
    }

    @CacheEvict(value = "AUTH_TOKEN", key = "#userId")
    public void removeUser(String userId) {
        logger.info("[Removing user with ID: " + userId + "]");
        RealmResource realmResource = keycloakAdminClient.realm(keycloakRealm);
        UsersResource usersResource = realmResource.users();

        try {
            if (usersResource.get(userId).toRepresentation() != null) {
                usersResource.delete(userId);
                logger.info("[User removed: " + userId + "]");
            } else {
                throw new RuntimeException("[User not found in Keycloak.]");
            }
        } catch (Exception e) {
            logger.severe("[Failed to remove user: " + e.getMessage() + "]");
            throw new RuntimeException("[Failed to remove user from Keycloak: " + e.getMessage() + "]", e);
        }
    }
}
