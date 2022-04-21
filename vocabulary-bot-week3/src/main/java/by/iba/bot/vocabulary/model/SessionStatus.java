package by.iba.bot.vocabulary.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * The POJO which contains chat session status for the particular user.
 *
 *  @author Mikalai Zaikin (nzaikin@iba.by)
 *  @since 4Q2021
 */
@Getter
@Setter
@NoArgsConstructor
public class SessionStatus {
    boolean showWords;
    Integer showInterval;
    LocalDateTime lastShownTime;
    Integer numOfWordsToShow;
}
