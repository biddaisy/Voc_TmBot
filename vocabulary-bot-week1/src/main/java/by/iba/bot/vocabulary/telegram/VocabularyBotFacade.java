package by.iba.bot.vocabulary.telegram;

import by.iba.bot.vocabulary.config.BotCommand;
import by.iba.bot.vocabulary.model.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The class which encapsulates main bot logic
 *
 * @author Mikalai Zaikin (nzaikin@iba.by)
 * @since 4Q2021
 */
@Component
public class VocabularyBotFacade {

    private static final List<Command> COMMANDS = Stream.of(BotCommand.values()).map(e -> new Command(e.getName(), e.getDescription())).collect(Collectors.toList());

    private static final String START_MSG = "Hello, ";
    private static final String WORD_MSG = "Java \n [ˈdʒɑːvə]\n 1) о-в Ява\n 2) яванский кофе\n 3) язык программирования №1";
    private static final String HELP_MSG;
    private static final String DATE_MSG = "Сегодня ";
    private static final String TIME_MSG = "Сейчас ";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm";
    private static final String EUROPE_MINSK = "Europe/Minsk";

    static {
        HELP_MSG = "Доступные команды\n" + COMMANDS.stream().map(c -> c.getName() + " - " + c.getDescription() + "\n").collect(Collectors.joining());
    }

    @Autowired
    private VocabularyBot vocabularyBot;

    public void handleUpdate(Update update) {
        if (!update.hasMessage()) return;

        final var messageText = update.getMessage().getText();
        final var chatId = update.getMessage().getChatId();
        final var userFirstName = update.getMessage().getChat().getFirstName();

        handleBotUpdate(update, chatId, messageText, userFirstName);
    }

    private void sendMessage(Update update, String messageText) {
        SendMessage.SendMessageBuilder messageBuilder = SendMessage.builder();
        messageBuilder.chatId(String.valueOf(update.getMessage().getChatId())).text(messageText);
        try {
            vocabularyBot.execute(messageBuilder.build());
        } catch (TelegramApiException telegramApiException) {
            telegramApiException.printStackTrace();
        }
    }

    private void handleBotUpdate(Update update, Long chatId, String messageText, String userFirstName) {
        if (messageText.equals(BotCommand.HELP.getName())) {
            sendMessage(update, HELP_MSG);
        } else if (messageText.equals(BotCommand.WORD.getName())) {
            sendMessage(update, WORD_MSG);
        } else if (messageText.equals(BotCommand.START.getName())) {
            sendMessage(update, START_MSG + userFirstName);
        } else if (messageText.equals(BotCommand.DATE.getName())) {
            sendMessage(update, getCurrentDate());
        } else if (messageText.equals(BotCommand.TIME.getName())) {
            sendMessage(update, getCurrentTime());
        }
    }

    private String getCurrentTime() {
        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of(EUROPE_MINSK));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_FORMAT, new Locale("ru"));
        return TIME_MSG + localDateTime.format(formatter);
    }

    private String getCurrentDate() {
        LocalDate localDate = LocalDate.now(ZoneId.of(EUROPE_MINSK));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT, new Locale("ru"));
        return DATE_MSG + localDate.format(formatter);
    }
}
