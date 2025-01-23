package org.fablewhirl.character.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CharacterDto {
    @JsonProperty("_id")
    @JsonIgnore
    private String id;

    @JsonProperty("userId")
    private String userId;

    private List<String> tags;
    private DisabledBlocksDto disabledBlocks;
    private String edition;
    private SpellsDto spells;
    private String data;
    private String jsonType;
    private String version;

    @Data
    public static class DisabledBlocksDto {
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
    public static class SpellsDto {
        private String mode;
        private List<String> prepared;
        private List<String> book;
    }
}

