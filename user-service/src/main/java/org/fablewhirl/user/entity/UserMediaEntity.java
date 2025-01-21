package org.fablewhirl.user.entity;

import jakarta.persistence.Lob;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "user_media")
public class UserMediaEntity {
    private String id;
    private String userId;

    @Lob
    private byte[] avatar;
    @Lob
    private byte[] banner;
}


