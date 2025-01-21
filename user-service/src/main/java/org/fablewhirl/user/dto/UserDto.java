package org.fablewhirl.user.dto;

import lombok.Data;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String id;

    private String username;
    private String email;
    private String bio;
    private String roles;

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}



