package org.fablewhirl.character.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.PrePersist;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.UUID;

@Data
@Document(collection = "characters")
public class CharacterEntity {
    @Id
    private String id;

    @JsonProperty("userId")
    private String userId;

    private List<String> tags;
    private DisabledBlocks disabledBlocks;
    private String edition;
    private Spells spells;
    private String data;
    private String jsonType;
    private String version;

    @Data
    public static class DisabledBlocks {
        @JsonProperty("info-left")
        private List<String> infoLeft;

        @JsonProperty("info-right")
        private List<String> infoRight;

        @JsonProperty("notes-left")
        private List<String> notesLeft;

        @JsonProperty("notes-right")
        private List<String> notesRight;

        @JsonProperty("_id")
        private String id;
    }

    @Data
    public static class Spells {
        private String mode;
        private List<String> prepared;
        private List<String> book;
    }
    @PrePersist
    public void prePersist() {
        if (id == null || id.isEmpty()) {
            id = UUID.randomUUID().toString();
        }
        if (disabledBlocks != null) {
            disabledBlocks.setId(id);
        }
    }
}

