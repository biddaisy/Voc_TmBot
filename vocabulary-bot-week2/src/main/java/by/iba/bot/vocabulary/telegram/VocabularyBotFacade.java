package by.iba.bot.vocabulary.telegram;

import by.iba.bot.vocabulary.config.BotCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

/**
 * The class which encapsulates main bot logic
 *
 * @author Mikalai Zaikin (nzaikin@iba.by)
 * @since 4Q2021
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class VocabularyBotFacade {

    private final MessageGenerator messageGenerator;

    public Optional<String> handleUpdate(Update update) {
        if (!update.hasMessage()) return Optional.empty();

        var commandName = update.getMessage().getText();
        var userFirstName = update.getMessage().getChat().getFirstName();

        return runCommand(commandName, userFirstName);

    }

    private Optional<String> runCommand(String commandName, String userFirstName) {
        if (commandName.equals(BotCommand.HELP.getName())) {
            return Optional.of(messageGenerator.getHelpMessage());
        } else if (commandName.equals(BotCommand.WORD.getName())) {
            return Optional.of(messageGenerator.generateWordMessage());
        } else if (commandName.equals(BotCommand.START.getName())) {
            return Optional.of(messageGenerator.generateStartMessage(userFirstName));
        } else if (commandName.equals(BotCommand.ABOUT.getName())) {
            return Optional.of(messageGenerator.getAboutMessage());
        }
        return Optional.of("Неверная команда.\n" + messageGenerator.getHelpMessage());
    }
}
