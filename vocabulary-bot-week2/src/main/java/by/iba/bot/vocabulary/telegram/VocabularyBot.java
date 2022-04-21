package by.iba.bot.vocabulary.telegram;

import by.iba.bot.vocabulary.config.BotConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * The Telegram bot class
 *
 * @author Mikalai Zaikin (nzaikin@iba.by)
 * @since 4Q2021
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class VocabularyBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;

    private final VocabularyBotFacade vocabularyBotFacade;

    @Override
    public String getBotUsername() {
        return botConfig.getName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getAccessToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        vocabularyBotFacade.handleUpdate(update).ifPresent(m -> sendMessage(update, m));
    }

    private void sendMessage(Update update, String responseMessage) {
        var messageBuilder = SendMessage.builder();
        messageBuilder.chatId(String.valueOf(update.getMessage().getChatId())).text(responseMessage);
        try {
            execute(messageBuilder.build());
        } catch (TelegramApiException telegramApiException) {
            log.error("failure to send message '{}'", responseMessage, telegramApiException);
        }
    }
}