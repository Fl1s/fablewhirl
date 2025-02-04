package org.fablewhirl.thread.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ThreadDto {
    @JsonProperty("userId")
    private String userId;

    private String title;
    private String content;
    private List<String> media;
    private List<String> comments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private int commentCount;
}
