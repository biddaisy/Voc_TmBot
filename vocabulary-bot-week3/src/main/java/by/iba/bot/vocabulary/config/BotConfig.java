package by.iba.bot.vocabulary.config;

import by.iba.bot.vocabulary.model.Command;
import lombok.Getter;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *  @author Mikalai Zaikin (nzaikin@iba.by)
 *  @since 4Q2021
 */
@Component
@ConfigurationProperties("bot")
@Getter
@Setter
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
     * Vocabulary language
     */
    private String vocLang;

    /**
     * List of supported commands for this bot
     */
    private List<Command> commands;

    @PostConstruct
    private void init(){
        commands = Stream.of(BotCommand.values()).map(e -> new Command(e.getName(), e.getDescription())).collect(Collectors.toList());
     }
}