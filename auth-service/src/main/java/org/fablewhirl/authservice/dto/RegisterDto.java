package org.fablewhirl.authservice.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
@Builder
@Getter
public class RegisterDto {
    private String id;
    private String username;
    private String email;
    private String bio;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
