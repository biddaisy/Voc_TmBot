package by.iba.bot.vocabulary.telegram;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * The Telegram bot class
 *
 *  @author Mikalai Zaikin (nzaikin@iba.by)
 *  @since 4Q2021
 */
@Component
public class VocabularyBot extends TelegramLongPollingBot {

    @Autowired
    private VocabularyBotFacade vocabularyBotFacade;

    @Value("${bot_username}")
    private String botUsername;

    @Value("${bot_token}")
    private String botToken;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        vocabularyBotFacade.handleUpdate(update);
    }
}