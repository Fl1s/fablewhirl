package org.fablewhirl.thread.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ThreadDto {
    private String threadId;
    private String userId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
}
