package by.iba.bot.vocabulary.telegram;


import by.iba.bot.vocabulary.config.BotCommand;
import by.iba.bot.vocabulary.config.BotState;
import by.iba.bot.vocabulary.model.SessionStatus;
import by.iba.bot.vocabulary.mongo.collection.Session;
import by.iba.bot.vocabulary.mongo.collection.Word;
import by.iba.bot.vocabulary.service.SessionService;
import by.iba.bot.vocabulary.service.WordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * The class which encapsulates main bot logic
 *
 * @author Mikalai Zaikin (nzaikin@iba.by)
 * @since 3Q2021
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class VocabularyBotFacade {

    private final SessionService sessionService;

    private final MessageGenerator messageGenerator;

    private final WordService wordService;

    @Autowired
    @Lazy
    private VocabularyBot vocabularyBot;

    /**
     * The timer callback method for triggering notification sending
     * Initial delay required to make application fully start
     * Then method called every second to check which user requires notification
     */
    @Scheduled(fixedDelay = 1000 * 60, initialDelay = 1000 * 20)
    public void scheduleShowWord() {
        LocalDateTime now = LocalDateTime.now();
        List<Word> randomWords = wordService.getRandomWords(SessionService.MAX_NUM_OF_WORDS_TO_SHOW);
        List<Session> sessions = sessionService.getAllToNotify(); // get all properly configured sessions
        sessions.stream().filter(s -> isNotified(now, s)).forEach(s -> notify(randomWords, s));
    }

    private void notify(List<Word> wordList, Session s) {
        List<Word> wordsToShow = wordList.subList(0, s.getNumOfWordsToShow());
        sendMessage(s.getChatId(), messageGenerator.generateWordsMessage(wordsToShow));
        sessionService.resetLastNotified(s.getChatId());
    }

    private boolean isNotified(LocalDateTime now, Session s) {
        Integer interval = s.getShowInterval();
        LocalDateTime lastNotified = s.getLastShownTime();
        if (lastNotified == null)
            return true;
        Duration duration = Duration.between(lastNotified, now);
        long durationMinutes = duration.toMinutes();
        return durationMinutes >= interval;
    }

    private void sendMessage(Long chatId, String messageText) {
        SendMessage.SendMessageBuilder messageBuilder = SendMessage.builder();
        messageBuilder.chatId(chatId.toString())
                .parseMode(ParseMode.HTML)
                .text(messageText);
        try {
            vocabularyBot.execute(messageBuilder.build());
        } catch (TelegramApiException telegramApiException) {
            logSendError(messageText, chatId);
        }
    }

    public void handleUpdate(Update update) throws IOException {
        if (!update.hasMessage())
            return;
        Long chatId = update.getMessage().getChatId();
        String messageText = update.getMessage().getText();
        String userFirstName = update.getMessage().getChat().getFirstName();

        if (!sessionService.isChatInit(chatId)) {
            sessionService.initChat(chatId);
            sendMessage(update, messageGenerator.generateStartMessage(userFirstName));
        } else {
            handleBotState(update, chatId, messageText, userFirstName);
        }
    }

    private void sendMessage(Update update, String messageText) {
        SendMessage.SendMessageBuilder messageBuilder = SendMessage.builder();
        Long chatId = update.getMessage().getChatId();
        messageBuilder.chatId(String.valueOf(chatId));
        messageBuilder.parseMode(ParseMode.HTML).text(messageText);
        try {
            vocabularyBot.execute(messageBuilder.build());
        } catch (TelegramApiException telegramApiException) {
            logSendError(messageText, chatId);
        }
    }

    private void logSendError(String messageText, Long chatId) {
        log.error("failed to send message '{}' to chat {}", messageText, chatId);
    }

    private void handleBotState(Update update, Long chatId, String messageText, String userFirstName) throws IOException {
        BotState botState = sessionService.getBotState(chatId);

        if (messageText.equals(BotCommand.CANCEL.getName())) {
            handleCancelCommand(update, chatId, botState);
            return;
        }

        switch (botState) {
            case DEFAULT: {
                handleDefaultState(update, chatId, messageText, userFirstName);
                break;
            }
            case SET_NOTIFY_PERIOD: {
                handleSetNotifyPeriodState(update, chatId, messageText);
                break;
            }
            case SET_NUMBER_OF_WORDS: {
                handleSetNumberOfWordsState(update, chatId, messageText);
                break;
            }
        }
    }

    private void handleSetNumberOfWordsState(Update update, Long chatId, String messageText) {
        Integer numOfWords = null;
        try {
            numOfWords = Integer.parseInt(messageText);
        } catch (NumberFormatException e) {
            // Just ignore. It's a bad practice, but we control by checking the numOfWords for null value
        }
        if ((numOfWords != null) && (numOfWords > 0) && (numOfWords <= SessionService.MAX_NUM_OF_WORDS_TO_SHOW)) {
            sessionService.setNumOfWordsToShow(chatId, numOfWords);
            sessionService.setBotState(chatId, BotState.DEFAULT); // return to the default state
            sendMessage(update, messageGenerator.generateSuccessWordSet(numOfWords));
        } else {
            sendMessage(update, messageGenerator.generateErrorWordSet());
        }
    }

    private void handleSetNotifyPeriodState(Update update, Long chatId, String messageText) {
        Integer showInterval = null;
        try {
            showInterval = Integer.parseInt(messageText);
        } catch (NumberFormatException e) {
            // Just ignore. It's a bad practice, but we control by checking the showInterval for null value
        }
        if ((showInterval != null) && (showInterval > 0)) {
            sessionService.setShowInterval(chatId, showInterval);
            sessionService.setBotState(chatId, BotState.DEFAULT); // return to the default state
            sendMessage(update, messageGenerator.generateSuccessNotifySet(showInterval));
        } else {
            sendMessage(update, messageGenerator.generateErrorNotifySet());
        }
    }

    private void handleDefaultState(Update update, Long chatId, String messageText, String userFirstName) throws UnknownHostException {
        if (messageText.equals(BotCommand.HELP.getName())) {
            sendMessage(update, messageGenerator.generateHelpMessage());
        } else if (messageText.equals(BotCommand.NOTIFYSET.getName())) {
            sessionService.setBotState(chatId, BotState.SET_NOTIFY_PERIOD);
            sendMessage(update, "Введите периодичность показа новых слов (в минутах) :");
        } else if (messageText.equals(BotCommand.WORD.getName())) {
            sendMessage(update, messageGenerator.generateWordMessage());
        } else if (messageText.equals(BotCommand.START.getName())) {
            sessionService.setBotState(chatId, BotState.DEFAULT);
            sendMessage(update, messageGenerator.generateStartMessage(userFirstName));
        } else if (messageText.equals(BotCommand.NOTIFYON.getName())) {
            sessionService.setShowWords(chatId, true);
            sendMessage(update, messageGenerator.generateNotifyOnMessage(userFirstName));
        } else if (messageText.equals(BotCommand.NOTIFYOFF.getName())) {
            sessionService.setShowWords(chatId, false);
            sendMessage(update, messageGenerator.generateNotifyOffMessage(userFirstName));
        } else if (messageText.equals(BotCommand.STATUS.getName())) {
            SessionStatus sessionStatus = sessionService.getSessionStatus(chatId);
            sendMessage(update, messageGenerator.generateStatusMessage(sessionStatus));
        } else if (messageText.equals(BotCommand.WORDSET.getName())) {
            sessionService.setBotState(chatId, BotState.SET_NUMBER_OF_WORDS);
            sendMessage(update, "Введите количество показываемых за один раз слов (1.." + SessionService.MAX_NUM_OF_WORDS_TO_SHOW + ") :");
        } else if (messageText.equals(BotCommand.EXIT.getName())) {
            sessionService.exit(chatId);
            sendMessage(update, messageGenerator.generateExitMessage(userFirstName));
        } else if (messageText.equals(BotCommand.ABOUT.getName())) {
            sendMessage(update, messageGenerator.generateAboutMessage());
        }
    }

    private void handleCancelCommand(Update update, Long chatId, BotState botState) {
        if (botState == BotState.DEFAULT) {
            sendMessage(update, "Нет активной команды для отмены");
        } else {
            sessionService.setBotState(chatId, BotState.DEFAULT);
            sendMessage(update, messageGenerator.generateSuccessCancel());
        }
    }
}