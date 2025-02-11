package org.fablewhirl.user.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationEvent {
    private String correlationId;
    private String userId;
    private String username;
    private String email;
    private String password;
    private String bio;
}
