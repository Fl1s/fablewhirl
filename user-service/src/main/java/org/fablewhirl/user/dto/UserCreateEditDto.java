package org.fablewhirl.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateEditDto {
    private String username;
    private String email;
    private String password;
    private String bio;
}