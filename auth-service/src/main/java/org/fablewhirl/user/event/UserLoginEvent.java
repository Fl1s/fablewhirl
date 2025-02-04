package org.fablewhirl.user.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginEvent {
    private String correlationId;
    private String username;
    private String password;
}
