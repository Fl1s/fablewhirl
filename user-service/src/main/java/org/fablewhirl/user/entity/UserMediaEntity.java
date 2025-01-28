package org.fablewhirl.user.entity;

import jakarta.validation.constraints.NotNull;
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

    @NotNull
    private String userId;

    private String avatarUrl;
    private String bannerUrl;
}
