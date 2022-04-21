package by.iba.bot.vocabulary.service;

import by.iba.bot.vocabulary.command.Command;
import by.iba.bot.vocabulary.model.SessionStatus;
import by.iba.bot.vocabulary.mongo.SessionRepo;
import by.iba.bot.vocabulary.mongo.collection.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * A service class to work with user sessions
 *
 * @author Mikalai Zaikin (nzaikin@iba.by)
 * @since 4Q2021
 */
@Service
@RequiredArgsConstructor
public class SessionService {

    public static final int MAX_NUM_OF_WORDS_TO_SHOW = 5;

    private final SessionRepo sessionRepo;

    /**
     * Check if the bot chat session is super new
     */
    public boolean isChatInit(Long chatId) {
        return sessionRepo.findAllByChatId(chatId) != null;
    }

    /**
     * Create a new chat session
     */
    public void initChat(Long chatId) {
        sessionRepo.save(new Session(chatId, 1));
    }

    public void setBotCommand(Long chatId, Command command) {
        Session session = sessionRepo.findAllByChatId(chatId);
        session.setCommand(command);
        sessionRepo.save(session);
    }

    public void resetBotCommand(Long chatId) {
        Session session = sessionRepo.findAllByChatId(chatId);
        session.setCommand(null);
        sessionRepo.save(session);
    }

    public Command getBotCommand(Long chatId) {
        return sessionRepo.findAllByChatId(chatId).getCommand();
    }

    public void setShowWords(Long chatId, boolean showWords) {
        Session session = sessionRepo.findAllByChatId(chatId);
        session.setShowWords(showWords);
        sessionRepo.save(session);
    }

    public void resetLastNotified(Long chatId) {
        Session session = sessionRepo.findAllByChatId(chatId);
        session.setLastShownTime(LocalDateTime.now());
        sessionRepo.save(session);
    }

    public void resetLastNotified(Session session) {
        session.setLastShownTime(LocalDateTime.now());
        sessionRepo.save(session);
    }

    /**
     * @param chatId   TG chat ID
     * @param interval Interval between showing of two words, in minutes
     */
    public void setShowInterval(Long chatId, int interval) {
        Session session = sessionRepo.findAllByChatId(chatId);
        session.setShowInterval(interval);
        sessionRepo.save(session);
    }

    public void exit(Long chatId) {
        Session session = sessionRepo.findAllByChatId(chatId);
        sessionRepo.delete(session);
    }

    public void setNumOfWordsToShow(Long chatId, int numOfWords) {
        Session session = sessionRepo.findAllByChatId(chatId);
        session.setNumOfWordsToShow(numOfWords);
        sessionRepo.save(session);
    }


    /**
     * Obtain chat session status
     */
    public SessionStatus getSessionStatus(Long chatId) {
        SessionStatus status = new SessionStatus();
        Session session = sessionRepo.findAllByChatId(chatId);
        if (session != null) {
            status.setShowWords(Optional.ofNullable(session.getShowWords()).orElse(false));
            status.setShowInterval(session.getShowInterval());
            status.setLastShownTime(session.getLastShownTime());
            status.setNumOfWordsToShow(session.getNumOfWordsToShow());
        }
        return status;
    }

    /**
     * Get all sessions eligible for notification:
     * 1) notify flag `true` and
     * 2) notify period is set to a valid value
     */
    public List<Session> getAllToNotify() {
        return sessionRepo.findAllToNotify();
    }

    public long getCount() {
        return sessionRepo.count();
    }
}