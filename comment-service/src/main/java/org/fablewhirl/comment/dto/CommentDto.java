package org.fablewhirl.comment.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private String id;
    private String threadId;
    private String parentId;
    private String userId;
    private String content;

    private boolean edited = false;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
