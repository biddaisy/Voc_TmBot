package by.iba.bot.vocabulary.telegram;

import by.iba.bot.vocabulary.config.BotCommand;
import by.iba.bot.vocabulary.config.BotConfig;
import by.iba.bot.vocabulary.model.Command;
import by.iba.bot.vocabulary.mongo.collection.Word;
import by.iba.bot.vocabulary.service.WordService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * The utility class to compose text messages for user interaction
 *
 * @author Mikalai Zaikin (nzaikin@iba.by)
 * @since 4Q2021
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MessageGenerator {

    private final BotConfig botConfig;

    private final WordService wordService;

    @Getter
    private String helpMessage;

    @Getter
    private String aboutMessage;

    @PostConstruct
    public void init() {
        helpMessage = generateHelpMessage();
        aboutMessage = generateAboutMessage();
    }

    public String generateStartMessage(String name) {
        return "Приветствую, " + name + "! \nЧтобы узнать, как мной пользоваться - введите " + BotCommand.HELP.getName();
    }

    private String generateHelpMessage() {
        return botConfig.getCommands().stream().reduce("Доступные команды\n", this::getCommandDescription, (m1, m2) -> m1);
    }

    private String getCommandDescription(String message, Command command) {
        return message + command.getName() + " - " + command.getDescription() + "\n";
    }

    public String generateWordMessage(Word word) {
        return word.getValue() + generateTranscription(word) + "\n " + word.getTranslation();
    }

    private String generateTranscription(Word word) {
        return word.getTranscription() != null ? "\n  [" + word.getTranscription() + "]" : "";
    }

    public String generateWordMessage() {
        Word word = wordService.getRandomWord();
        return generateWordMessage(word);
    }

    /**
     * Returns the following information:
     * <p>
     * 1) number of words in the dictionary
     * 2) the hostname of the server where the bot is currently running
     *
     * @return Information about the bot
     */
    private String generateAboutMessage() {
        String dictionaryDescription = String.format("Англо-русский словарь. Содержит %s %s.", wordService.getCount(), wordService.ofWord(wordService.getCount()));
        String hostDescription = null;
        try {
            String hostName = InetAddress.getLocalHost().getHostName();
            hostDescription = String.format("%nБот запущен на хосте %s.", hostName);
        } catch (UnknownHostException e) {
            log.error("Getting host failure", e);
        }
        return dictionaryDescription + (hostDescription != null ? hostDescription : "");
    }

}