package by.iba.bot.vocabulary.telegram;

import by.iba.bot.vocabulary.config.BotConfig;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
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
@Slf4j
@RequiredArgsConstructor
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

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        vocabularyBotFacade.handleUpdate(update);
    }

    public void sendMessage(Long chatId, String messageText) {
        SendMessage.SendMessageBuilder messageBuilder = SendMessage.builder();
        messageBuilder.chatId(chatId.toString())
                .parseMode(ParseMode.HTML)
                .text(messageText);
        try {
            execute(messageBuilder.build());
        } catch (TelegramApiException telegramApiException) {
            log.error("failed to send message '{}' to chatId={}", messageText, chatId, telegramApiException);
        }
    }

    public void sendMessage(Update update, String messageText) {
        if (!update.hasMessage()) return;
        Long chatId = update.getMessage().getChatId();
        sendMessage(chatId, messageText);
    }

}