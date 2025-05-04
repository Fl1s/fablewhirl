package org.fablewhirl.thread.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ThreadCreateDto {
    private String userId;
    private String title;
    private String content;
}
