package org.fablewhirl.comment.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "comment_data")
public class CommentEntity {
    @Id
    private String id;
    private String threadId;
    private String userId;
    private String content;
    private String media;
    private boolean edited = false;

    @PrePersist
    public void prePersist() {
        if (id == null || id.isEmpty()) {
            id = UUID.randomUUID().toString();
        }
    }
    @PreUpdate
    public void preUpdate() {
        edited = true;
    }
}
