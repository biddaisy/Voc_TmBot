package by.iba.bot.vocabulary.telegram;

import by.iba.bot.vocabulary.config.BotConfig;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
}