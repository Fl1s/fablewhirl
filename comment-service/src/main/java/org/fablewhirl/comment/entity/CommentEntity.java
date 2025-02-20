package org.fablewhirl.comment.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "comments")
public class CommentEntity {
    @Id
    private String commentId;

    private String threadId;
    private String userId;
    private String parentId;
    private String content = "";
    private boolean edited = false;
    private LocalDateTime createdAt;
}
