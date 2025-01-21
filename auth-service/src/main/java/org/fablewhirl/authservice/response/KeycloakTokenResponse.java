package org.fablewhirl.authservice.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KeycloakTokenResponse {
    private String accessToken;
    private String refreshToken;
    private long refreshTokenTtl;
}

