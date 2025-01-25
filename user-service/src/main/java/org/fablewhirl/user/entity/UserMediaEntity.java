package org.fablewhirl.user.entity;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "user_media")
public class UserMediaEntity {
    private String id;

    @NotNull
    private String userId;
    @Lob
    private byte[] avatar;
    @Lob
    private byte[] banner;
}



