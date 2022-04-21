package by.iba.bot.vocabulary.model;

import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * The POJO which contains chat session status for the particular user.
 *
 * @author Mikalai Zaikin (nzaikin@iba.by)
 * @since 4Q2021
 */
@Setter
@NoArgsConstructor
public class SessionStatus {

    private boolean showWords;
    private Integer showInterval;
    private LocalDateTime lastShownTime;
    private Integer numOfWordsToShow;

    public boolean isShowWords() {
        return showWords;
    }

    public Integer getShowInterval() {
        return showInterval;
    }

    public LocalDateTime getLastShownTime() {
        return lastShownTime;
    }

    public Integer getNumOfWordsToShow() {
        return numOfWordsToShow;
    }

}
