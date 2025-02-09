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

    public void registerUser(String userId, String username, String email, String password) {
        Keycloak keycloak = getAdminKeycloakInstance();
        RealmResource realmResource = keycloak.realm(keycloakRealm);
        UsersResource usersResource = realmResource.users();

        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail(email);
        user.setEnabled(true);

        if (user.getAttributes() == null) {
            user.setAttributes(new HashMap<>());
        }

        user.getAttributes().put("userId", Collections.singletonList(userId));

        Response response = usersResource.create(user);
        if (response.getStatus() != 201) {
            if (response.getStatus() == 409) {
                throw new RuntimeException("User with this username or email already exists in Keycloak.");
            }
            throw new RuntimeException("Failed to create user in Keycloak: " + response.getStatusInfo());
        }

        if (response.getStatus() == 201) {
            userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
        }

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        keycloak.realm(keycloakRealm).users().get(userId).resetPassword(credential);

        RoleRepresentation userRole = realmResource.roles().get("user").toRepresentation();
        keycloak.realm(keycloakRealm).users().get(userId)
                .roles().realmLevel().add(Collections.singletonList(userRole));

        logger.info("User successfully registered in Keycloak: " + username + " with userId: " + userId);
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

        logger.info("Generated tokens: Access Token (valid for 1 week), Refresh Token (valid for 15 minutes)");

        AccessTokenResponse response = new AccessTokenResponse();
        response.setToken(accessToken);
        response.setExpiresIn(604800);

        response.setRefreshToken(refreshToken);
        response.setRefreshExpiresIn(900);
        return response;
    }

}
