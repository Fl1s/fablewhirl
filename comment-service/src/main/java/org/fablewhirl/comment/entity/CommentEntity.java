package org.fablewhirl.comment.entity;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "comments")
public class CommentEntity {
    @Id
    private String id;

    private String threadId;
    private String userId;
    private String content = "";
    private String parentId;
    private boolean edited = false;
    private LocalDateTime createdAt;
}
