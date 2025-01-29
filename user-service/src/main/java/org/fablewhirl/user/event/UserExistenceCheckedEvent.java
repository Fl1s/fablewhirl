package org.fablewhirl.user.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserExistenceCheckedEvent extends CheckUserExistenceEvent {
    private String correlationId;
    private boolean userExists;
}

