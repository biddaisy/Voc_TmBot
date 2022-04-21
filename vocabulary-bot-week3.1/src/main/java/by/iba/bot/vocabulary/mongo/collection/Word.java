package by.iba.bot.vocabulary.mongo.collection;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * POJO to represent a word stored in Mongo DB collection.
 *
 * @author Mikalai Zaikin (nzaikin@iba.by)
 * @since 4Q2021
 */
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@Document(collection = "#{@wordsCollectionName}")
public class Word {

    @Id
    private Integer id;

    @NonNull
    @Field(name = "word", targetType = FieldType.STRING)
    private String value;

    @Field(targetType = FieldType.STRING)
    private String stress;

    @Field(targetType = FieldType.STRING)
    private String transcription;

    @NonNull
    @Field(targetType = FieldType.ARRAY)
    private List<Translation> translations;

    @Field(targetType = FieldType.ARRAY)
    private List<Usage> usages;

    @RequiredArgsConstructor
    public static class Translation {
        public final String en;
        public final String ru;
    }

    @RequiredArgsConstructor
    public static class Usage {
        public final String en;
        public final String ru;
    }

    public Integer getId() {
        return id;
    }

    @NonNull
    public String getValue() {
        return value;
    }

    public String getStress() {
        return stress;
    }

    public String getTranscription() {
        return transcription;
    }

    @NonNull
    public List<Translation> getTranslations() {
        return translations;
    }

    public List<Usage> getUsages() {
        return usages;
    }

}