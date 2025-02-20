package org.fablewhirl.thread.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
