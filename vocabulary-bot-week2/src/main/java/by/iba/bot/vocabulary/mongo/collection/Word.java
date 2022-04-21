package by.iba.bot.vocabulary.mongo.collection;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.lang.NonNull;

import java.util.Objects;

/**
 * POJO to represent a word stored in Mongo DB collection.
 *
 *  @author Mikalai Zaikin (nzaikin@iba.by)
 *  @since 4Q2021
 */
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@Document(collection = "words")
public class Word {

    /**
     * By design, we have Word IDs started from 0 and then incremented by 1 (i.e. 0, 1,2,3,4, and so on)
     */
    @Id
    private Integer id;

    @NonNull
    @Field(name = "word", targetType = FieldType.STRING)
    private String value;

    @Field(targetType = FieldType.STRING)
    private String transcription;

    @NonNull
    @Field(targetType = FieldType.STRING)
    private String translation;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Word)) return false;
        Word word = (Word) o;
        return getId().equals(word.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}