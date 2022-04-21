package by.iba.bot.vocabulary.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * A utility class to register the bot with the Telegram server
 *
 *  @author Mikalai Zaikin (nzaikin@iba.by)
 *  @since 4Q2021
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class VocabularyBotRegistrar {

    private final VocabularyBot vocabularyBot;

    /**
     * Once Spring Boot application fully started, perform bot registration
     */
    @EventListener(ApplicationReadyEvent.class)
    public void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(vocabularyBot);
            log.info("Telegram Bot is registered with the Telegram server");
        } catch (TelegramApiRequestException e) {
            log.error("failed to register bot", e);
            System.exit(1);
        }
    }
}