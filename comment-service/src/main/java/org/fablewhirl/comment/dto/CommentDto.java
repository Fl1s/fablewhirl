package org.fablewhirl.comment.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private String commentId;

    private String threadId;
    private String parentId;
    private String userId;
    private String content;
    private boolean edited;
    private LocalDateTime createdAt;
}
