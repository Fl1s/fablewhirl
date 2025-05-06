package org.fablewhirl.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDto {
    @NotBlank(message = "[ Email must not be blank ]")
    @Email(message = "[ Invalid email format ]")
    private String email;

    @NotBlank(message = "[ Username must not be blank ]")
    @Size(min = 3, max = 20, message = "[ Username must be between 3 and 20 characters ]")
    private String username;

    @NotBlank(message = "[ Password must not be blank ]")
    @Size(min = 6, max = 100, message = "[ Password must be at least 6 characters long ]")
    private String password;

    @Size(max = 250, message = "[ Bio must be at most 250 characters ]")
    private String bio;
}
