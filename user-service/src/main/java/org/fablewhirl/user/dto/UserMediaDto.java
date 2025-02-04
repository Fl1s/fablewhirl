package org.fablewhirl.user.dto;

import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMediaDto {
    @Lob
    private byte[] avatar;
    @Lob
    private byte[] banner;
}

