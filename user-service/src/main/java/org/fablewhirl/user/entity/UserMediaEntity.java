package org.fablewhirl.user.entity;

import jakarta.persistence.Entity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "user_media")
public class UserMediaEntity {
    @Id
    private String id;

    private Long userId;
    private String avatarUrl;
    private String bannerUrl;
}


