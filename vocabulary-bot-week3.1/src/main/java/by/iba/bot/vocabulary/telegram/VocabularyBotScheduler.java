package by.iba.bot.vocabulary.telegram;

import by.iba.bot.vocabulary.command.Command;
import by.iba.bot.vocabulary.command.WordBotCommand;
import by.iba.bot.vocabulary.config.BotConfig;
import by.iba.bot.vocabulary.mongo.collection.Session;
import by.iba.bot.vocabulary.mongo.collection.Word;
import by.iba.bot.vocabulary.service.SessionService;
import by.iba.bot.vocabulary.service.WordService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * The class which encapsulates main bot logic
 *
 * @author Mikalai Zaikin (nzaikin@iba.by)
 * @since 4Q2021
 */
@Component
@RequiredArgsConstructor
public class VocabularyBotScheduler {

    private final SessionService sessionService;
    private final WordService wordService;
    private final BotConfig botConfig;
    private final VocabularyBot vocabularyBot;

    /**
     * The timer callback method for triggering notification sending
     * Initial delay required to make application fully start
     * Then method called every second to check which user requires notification
     */
    @Scheduled(fixedDelay = 1000 * 60, initialDelay = 1000 * 20)
    public void scheduleShowWord() {
        var now = LocalDateTime.now();
        var sessions = sessionService.getAllToNotify(); // get all properly configured sessions
        int maxWordsToShow = sessions.stream()
                .max(Comparator.comparing(Session::getNumOfWordsToShow))
                .map(Session::getNumOfWordsToShow).orElse(0);
        if (maxWordsToShow < 1) return;
        var randomWords = wordService.getRandomWords(maxWordsToShow);
        sessions.stream()
                .filter(s -> isShown(now, s))
                .forEach(s -> showWords(randomWords, s));
    }

    private void showWords(List<Word> randomWords, Session session) {
        var wordsToShow = randomWords.subList(0, session.getNumOfWordsToShow());
        var wordBotCommand = (WordBotCommand) botConfig.getBotCommand(Command.WORD);
        vocabularyBot.sendMessage(session.getChatId(), wordBotCommand.generateWordsMessage(wordsToShow));
        sessionService.resetLastNotified(session);
    }

    private boolean isShown(LocalDateTime now, Session s) {
        var interval = s.getShowInterval();
        var lastNotified = s.getLastShownTime();
        if (lastNotified != null) {
            var duration = Duration.between(lastNotified, now);
            var durationMinutes = duration.toMinutes();
            return durationMinutes >= interval;
        }
        return true;
    }

}