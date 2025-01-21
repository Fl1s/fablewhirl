package org.fablewhirl.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserReadDto {
    private String username;
    private String email;
    private String bio;
    private String roles;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
