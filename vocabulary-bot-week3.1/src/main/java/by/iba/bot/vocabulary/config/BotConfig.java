package by.iba.bot.vocabulary.config;

import by.iba.bot.vocabulary.command.BotCommand;
import by.iba.bot.vocabulary.command.Command;
import com.vdurmont.emoji.EmojiParser;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Mikalai Zaikin (nzaikin@iba.by)
 * @since 4Q2021
 */
@Component
@ConfigurationProperties("bot")
@Getter
@Setter
@Slf4j
public class BotConfig {

    /**
     * Contains TG bot name picked during bot registration
     */
    private String name;

    /**
     * Contains TG bot token assigned during bot registration
     */
    private String accessToken;

    /**
     * MongoDB collection name for words
     */
    private String wordsCollectionName;

    /**
     * Make available as an injectable bean
     */
    @Bean("wordsCollectionName")
    public String getWordsCollectionName() {
        return wordsCollectionName;
    }

    /**
     * Language of the dictionary
     */
    private String wordsCollectionLanguage;

    /**
     * Map of supported commands for this bot
     * Key Command
     * Value AbstractBotCommand
     */
    private Map<Command, BotCommand> botCommands = new EnumMap<>(Command.class);

    private String helpMessage;

    @Autowired
    ListableBeanFactory beanFactory;

    @PostConstruct
    public void init() {
        botCommands = beanFactory.getBeansOfType(BotCommand.class).values().stream()
                .collect(Collectors.toMap(BotCommand::getCommand, Function.identity()));
        helpMessage = generateHelpMessage();
    }

    public Map<Command, BotCommand> getBotCommands() {
        return botCommands;
    }

    public String getWordsCollectionLanguage() {
        return wordsCollectionLanguage;
    }

    public BotCommand getBotCommand(Command command) {
        return botCommands.get(command);
    }

    public String getHelpMessage() {
        return helpMessage;
    }

    private String generateHelpMessage() {
        var message = botCommands.keySet().stream().sorted().reduce(":bulb: <b>Доступные команды</b>\n", (m, c) -> m + c.getName() + " - " + c.getDescription() + "\n", (m1, m2) -> m1);
        return EmojiParser.parseToUnicode(message);
    }

}