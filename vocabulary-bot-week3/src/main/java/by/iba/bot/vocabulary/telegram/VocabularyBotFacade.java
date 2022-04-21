package by.iba.bot.vocabulary.telegram;

import by.iba.bot.vocabulary.config.BotCommand;
import by.iba.bot.vocabulary.config.BotState;
import by.iba.bot.vocabulary.model.SessionStatus;
import by.iba.bot.vocabulary.mongo.collection.Session;
import by.iba.bot.vocabulary.mongo.collection.Word;
import by.iba.bot.vocabulary.service.SessionService;
import by.iba.bot.vocabulary.service.WordService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.net.UnknownHostException;
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
public class VocabularyBotFacade {

    private final SessionService sessionService;
    private final MessageGenerator messageGenerator;
    private final WordService wordService;
    private final ApplicationContext applicationContext;

    private VocabularyBot getVocabularyBot() {
        return applicationContext.getBean(VocabularyBot.class);
    }

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
        getVocabularyBot().sendMessage(session.getChatId(), messageGenerator.generateWordsMessage(wordsToShow));
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


    public void handleUpdate(Update update) throws UnknownHostException {
        if (!update.hasMessage()) return;

        var chatId = update.getMessage().getChatId();
        var messageText = update.getMessage().getText();
        var userFirstName = update.getMessage().getChat().getFirstName();

        if (!sessionService.isChatInit(chatId)) {
            sessionService.initChat(chatId);
            getVocabularyBot().sendMessage(update, messageGenerator.generateStartMessage(userFirstName));
        } else {
            handleBotState(update, chatId, messageText, userFirstName);
        }
    }

    private void handleBotState(Update update, Long chatId, String messageText, String userFirstName) throws UnknownHostException {
        BotState botState = sessionService.getBotState(chatId);

        if (messageText.equals(BotCommand.CANCEL.getName())) {
            handleCommandCancel(update, chatId, botState);
            return;
        }

        switch (botState) {
            case DEFAULT: {
                handleStateDefault(update, chatId, messageText, userFirstName);
                break;
            }
            case SET_NOTIFY_PERIOD: {
                handleStateSetNotifyPeriod(update, chatId, messageText);
                break;
            }
            case SET_NUMBER_OF_WORDS: {
                handleStateSetNumberOfWords(update, chatId, messageText);
                break;
            }
        }
    }

    private void handleStateSetNumberOfWords(Update update, Long chatId, String messageText) {
        int numOfWords = getInteger(messageText);
        if (numOfWords > 0 && numOfWords <= SessionService.MAX_NUM_OF_WORDS_TO_SHOW) {
            sessionService.setNumOfWordsToShow(chatId, numOfWords);
            sessionService.setBotState(chatId, BotState.DEFAULT); // return to the default state
            getVocabularyBot().sendMessage(update, messageGenerator.generateSuccessWordSet(numOfWords));
        } else {
            getVocabularyBot().sendMessage(update, messageGenerator.getErrorWordSetMessage());
        }
    }

    private int getInteger(String messageText) {
        return NumberUtils.toInt(messageText);
    }

    private void handleStateSetNotifyPeriod(Update update, Long chatId, String messageText) {
        int showInterval = getInteger(messageText);
        if (showInterval > 0) {
            sessionService.setShowInterval(chatId, showInterval);
            sessionService.setBotState(chatId, BotState.DEFAULT); // return to the default state
            getVocabularyBot().sendMessage(update, messageGenerator.generateSuccessNotifySet(showInterval));
        } else {
            getVocabularyBot().sendMessage(update, messageGenerator.getErrorNotifySetMessage());
        }
    }

    private void handleStateDefault(Update update, Long chatId, String messageText, String userFirstName) throws UnknownHostException {
        if (messageText.equals(BotCommand.HELP.getName())) {
            getVocabularyBot().sendMessage(update, messageGenerator.getHelpMessage());
        } else if (messageText.equals(BotCommand.NOTIFYSET.getName())) {
            sessionService.setBotState(chatId, BotState.SET_NOTIFY_PERIOD);
            getVocabularyBot().sendMessage(update, "Введите периодичность показа новых слов (в минутах) :");
        } else if (messageText.equals(BotCommand.WORD.getName())) {
            getVocabularyBot().sendMessage(update, messageGenerator.generateWordMessage());
        } else if (messageText.equals(BotCommand.START.getName())) {
            sessionService.setBotState(chatId, BotState.DEFAULT);
            getVocabularyBot().sendMessage(update, messageGenerator.generateStartMessage(userFirstName));
        } else if (messageText.equals(BotCommand.NOTIFYON.getName())) {
            sessionService.setShowWords(chatId, true);
            getVocabularyBot().sendMessage(update, messageGenerator.generateNotifyOnMessage(userFirstName));
        } else if (messageText.equals(BotCommand.NOTIFYOFF.getName())) {
            sessionService.setShowWords(chatId, false);
            getVocabularyBot().sendMessage(update, messageGenerator.generateNotifyOffMessage(userFirstName));
        } else if (messageText.equals(BotCommand.STATUS.getName())) {
            SessionStatus sessionStatus = sessionService.getSessionStatus(chatId);
            getVocabularyBot().sendMessage(update, messageGenerator.generateStatusMessage(sessionStatus));
        } else if (messageText.equals(BotCommand.WORDSET.getName())) {
            sessionService.setBotState(chatId, BotState.SET_NUMBER_OF_WORDS);
            getVocabularyBot().sendMessage(update, "Введите количество показываемых за один раз слов (1.." + SessionService.MAX_NUM_OF_WORDS_TO_SHOW + ") :");
        } else if (messageText.equals(BotCommand.EXIT.getName())) {
            sessionService.exit(chatId);
            getVocabularyBot().sendMessage(update, messageGenerator.generateExitMessage(userFirstName));
        } else if (messageText.equals(BotCommand.ABOUT.getName())) {
            getVocabularyBot().sendMessage(update, messageGenerator.generateAboutMessage());
        } else {
            getVocabularyBot().sendMessage(update, messageGenerator.getHelpMessage());
        }

    }

    private void handleCommandCancel(Update update, Long chatId, BotState botState) {
        if (botState == BotState.DEFAULT) {
            getVocabularyBot().sendMessage(update, "Нет активной команды для отмены");
        } else {
            sessionService.setBotState(chatId, BotState.DEFAULT);
            getVocabularyBot().sendMessage(update, messageGenerator.getSuccessCancelMessage());
        }
    }
}