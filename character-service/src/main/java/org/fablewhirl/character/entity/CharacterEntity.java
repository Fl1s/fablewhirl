package org.fablewhirl.character.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.PrePersist;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Document(collection = "characters")
public class CharacterEntity {
    @Id
    private String id;

    @JsonProperty("userId")
    private String userId;

    private List<String> tags = new ArrayList<>();

    private DisabledBlocks disabledBlocks = new DisabledBlocks();

    private String edition = "2024";

    private Spells spells = new Spells();

    private String data = "{\"isDefault\":true,\"jsonType\":\"character\",\"template\":\"default\",\"name\":{\"value\":\"\"},\"info\":{\"charClass\":{\"name\":\"charClass\",\"label\":\"класс и уровень\",\"value\":\"\"},\"charSubclass\":{\"name\":\"charSubclass\",\"label\":\"подкласс\",\"value\":\"\"},\"level\":{\"name\":\"level\",\"label\":\"уровень\",\"value\":1},\"background\":{\"name\":\"background\",\"label\":\"предыстория\",\"value\":\"\"},\"playerName\":{\"name\":\"playerName\",\"label\":\"имя игрока\",\"value\":\"\"},\"race\":{\"name\":\"race\",\"label\":\"раса\",\"value\":\"\"},\"alignment\":{\"name\":\"alignment\",\"label\":\"мировоззрение\",\"value\":\"\"},\"experience\":{\"name\":\"experience\",\"label\":\"опыт\",\"value\":\"\"}},\"subInfo\":{\"age\":{\"name\":\"age\",\"label\":\"возраст\",\"value\":\"\"},\"height\":{\"name\":\"height\",\"label\":\"рост\",\"value\":\"\"},\"weight\":{\"name\":\"weight\",\"label\":\"вес\",\"value\":\"\"},\"eyes\":{\"name\":\"eyes\",\"label\":\"глаза\",\"value\":\"\"},\"skin\":{\"name\":\"skin\",\"label\":\"кожа\",\"value\":\"\"},\"hair\":{\"name\":\"hair\",\"label\":\"волосы\",\"value\":\"\"}},\"spellsInfo\":{\"base\":{\"name\":\"base\",\"label\":\"Базовая характеристика заклинаний\",\"value\":\"\"},\"save\":{\"name\":\"save\",\"label\":\"Сложность спасброска\",\"value\":\"\"},\"mod\":{\"name\":\"mod\",\"label\":\"Бонус атаки заклинанием\",\"value\":\"\"}},\"spells\":{},\"spellsPact\":{},\"proficiency\":2,\"stats\":{\"str\":{\"name\":\"str\",\"label\":\"Сила\",\"score\":10,\"modifier\":0},\"dex\":{\"name\":\"dex\",\"label\":\"Ловкость\",\"score\":10,\"modifier\":0},\"con\":{\"name\":\"con\",\"label\":\"Телосложение\",\"score\":10,\"modifier\":0},\"int\":{\"name\":\"int\",\"label\":\"Интеллект\",\"score\":10,\"modifier\":0},\"wis\":{\"name\":\"wis\",\"label\":\"Мудрость\",\"score\":10,\"modifier\":0},\"cha\":{\"name\":\"cha\",\"label\":\"Харизма\",\"score\":10,\"modifier\":0}},\"saves\":{\"str\":{\"name\":\"str\",\"isProf\":false},\"dex\":{\"name\":\"dex\",\"isProf\":false},\"con\":{\"name\":\"con\",\"isProf\":false},\"int\":{\"name\":\"int\",\"isProf\":false},\"wis\":{\"name\":\"wis\",\"isProf\":false},\"cha\":{\"name\":\"cha\",\"isProf\":false}},\"skills\":{\"acrobatics\":{\"baseStat\":\"dex\",\"name\":\"acrobatics\",\"label\":\"Акробатика\"},\"investigation\":{\"baseStat\":\"int\",\"name\":\"investigation\",\"label\":\"Анализ\"},\"athletics\":{\"baseStat\":\"str\",\"name\":\"athletics\",\"label\":\"Атлетика\"},\"perception\":{\"baseStat\":\"wis\",\"name\":\"perception\",\"label\":\"Восприятие\"},\"survival\":{\"baseStat\":\"wis\",\"name\":\"survival\",\"label\":\"Выживание\"},\"performance\":{\"baseStat\":\"cha\",\"name\":\"performance\",\"label\":\"Выступление\"},\"intimidation\":{\"baseStat\":\"cha\",\"name\":\"intimidation\",\"label\":\"Запугивание\"},\"history\":{\"baseStat\":\"int\",\"name\":\"history\",\"label\":\"История\"},\"sleight of hand\":{\"baseStat\":\"dex\",\"name\":\"sleight of hand\",\"label\":\"Ловкость рук\"},\"arcana\":{\"baseStat\":\"int\",\"name\":\"arcana\",\"label\":\"Магия\"},\"medicine\":{\"baseStat\":\"wis\",\"name\":\"medicine\",\"label\":\"Медицина\"},\"deception\":{\"baseStat\":\"cha\",\"name\":\"deception\",\"label\":\"Обман\"},\"nature\":{\"baseStat\":\"int\",\"name\":\"nature\",\"label\":\"Природа\"},\"insight\":{\"baseStat\":\"wis\",\"name\":\"insight\",\"label\":\"Проницательность\"},\"religion\":{\"baseStat\":\"int\",\"name\":\"religion\",\"label\":\"Религия\"},\"stealth\":{\"baseStat\":\"dex\",\"name\":\"stealth\",\"label\":\"Скрытность\"},\"persuasion\":{\"baseStat\":\"cha\",\"name\":\"persuasion\",\"label\":\"Убеждение\"},\"animal handling\":{\"baseStat\":\"wis\",\"name\":\"animal handling\",\"label\":\"Уход за животными\"}},\"vitality\":{\"hp-dice-current\":{\"value\":1},\"hp-dice-multi\":{}},\"attunementsList\":[{\"id\":\"attunement-1737687333846\",\"checked\":false,\"value\":\"\"}],\"weaponsList\":[{\"id\":\"weapon-1737687333846\",\"name\":{\"value\":\"\"},\"mod\":{\"value\":\"+0\"},\"dmg\":{\"value\":\"\"}}],\"weapons\":{},\"text\":{},\"coins\":{},\"resources\":{},\"bonusesSkills\":{},\"bonusesStats\":{},\"conditions\":[]}";

    private String jsonType = "character";

    private String version = "2";

    @PrePersist
    public void prePersist() {
        if (id == null || id.isEmpty()) {
            id = UUID.randomUUID().toString();
        }
        if (disabledBlocks != null && disabledBlocks.getId().isEmpty()) {
            disabledBlocks.setId(id);
        }
    }

    @Data
    public static class DisabledBlocks {
        @JsonProperty("info-left")
        private List<String> infoLeft = new ArrayList<>();

        @JsonProperty("info-right")
        private List<String> infoRight = new ArrayList<>();

        @JsonProperty("notes-left")
        private List<String> notesLeft = new ArrayList<>();

        @JsonProperty("notes-right")
        private List<String> notesRight = new ArrayList<>();

        @JsonProperty("_id")
        private String id = "";
    }

    @Data
    public static class Spells {
        private String mode = "cards";

        private List<String> prepared = new ArrayList<>();

        private List<String> book = new ArrayList<>();
    }
}


