package org.fablewhirl.user.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationEvent {
    private String userId;
    private String username;
    private String email;
    private String bio;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
