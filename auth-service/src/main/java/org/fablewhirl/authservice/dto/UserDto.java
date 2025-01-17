package org.fablewhirl.authservice.dto;

import lombok.Data;
import org.springframework.scheduling.quartz.LocalDataSourceJobStore;

import java.time.LocalDateTime;

@Data
public class UserDto {
    private String id;

    private String username;
    private String password;
    private String role;
}
