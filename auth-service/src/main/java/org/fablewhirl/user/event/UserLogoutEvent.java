package org.fablewhirl.user.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLogoutEvent {
    private String correlationId;
    private String userId;
}
