package by.iba.bot.vocabulary.mongo.collection;

import by.iba.bot.vocabulary.command.Command;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.lang.NonNull;

import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * POJO to work with bot chat session state
 *
 * Always must be initialized with: session ID, state,  number of words to show
 *
 *  @author Mikalai Zaikin (nzaikin@iba.by)
 *  @since 4Q2021
 */
@Getter
@Setter
@RequiredArgsConstructor
@Document(collection = "sessions1")
public class Session {

    @Id
    private BigInteger id;

    @NonNull
    private Long chatId;

    @Field(targetType = FieldType.STRING)
    private Command command;

    /**
     * To track when user was last time shown a word by the bot,
     * to maintain correct notification period
     */
    @Field(targetType = FieldType.DATE_TIME)
    private LocalDateTime lastShownTime;

    /**
     * Show interval (in minutes) between words
     */
    @Field(targetType = FieldType.INT32)
    private Integer showInterval;

    /**
     * A flag to turn on and turn off showing words to the user
     */
    @Field(targetType = FieldType.BOOLEAN)
    private Boolean showWords;

    /**
     * Number of words to show each tome
     */
    @Field(targetType = FieldType.INT32)
    @NonNull
    private Integer numOfWordsToShow;
}