package org.fablewhirl.thread.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private String id;

    @JsonProperty
    private String userId;

    private String title = "";
    private String content = "";
    private List<String> media = new ArrayList<>();
    private List<String> comments = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private int commentCount = 0;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        if (id == null || id.isEmpty()) {
            id = UUID.randomUUID().toString();
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
