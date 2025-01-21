package org.fablewhirl.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMediaDto {
    private byte[] avatar;
    private byte[] banner;
}

