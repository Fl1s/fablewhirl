package org.fablewhirl.authservice.request;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private String bio;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
