package org.fablewhirl.user.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_info")
public class UserEntity {
    @Id
    private String id;

    private String username;
    private String email;
    private String password;
    private String bio;
    private String roles;

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @PrePersist
    public void prePersist() {
        createdDate = LocalDateTime.now();
            if (id == null || id.isEmpty()) {
                id = UUID.randomUUID().toString();
            }
    }

    @PreUpdate
    public void preUpdate() {
        updatedDate = LocalDateTime.now();
    }
}


