package org.fablewhirl.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDto {
    @NotBlank(message = "[ Username must not be blank ]")
    private String username;

    @NotBlank(message = "[ Password must not be blank ]")
    @Size(min = 6, max = 100, message = "[ Password must be at least 6 characters long ]")
    private String password;
}
