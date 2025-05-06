package org.fablewhirl.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRemoveDto {
    @NotBlank(message = "[ UserId must not be blank ]")
    private String userId;
}
