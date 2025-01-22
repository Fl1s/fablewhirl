package org.fablewhirl.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "characters")
public class CharacterEntity {
    @Id
    private String id;

    private List<String> tags;
    private DisabledBlocks disabledBlocks;
    private String edition;
    private Spells spells;
    private String data;
    private String jsonType;
    private String version;

    @Data
    public static class DisabledBlocks {
        private List<String> infoLeft;
        private List<String> infoRight;
        private List<String> notesLeft;
        private List<String> notesRight;
    }

    @Data
    public static class Spells {
        private String mode;
        private List<String> prepared;
        private List<String> book;
    }
}

