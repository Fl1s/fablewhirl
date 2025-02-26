package org.fablewhirl.thread.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "threads")
public class ThreadEntity {
    @Id
    private String threadId;

    private String userId;
    private String title = "";
    private String content = "";
    private LocalDateTime createdAt;
}
