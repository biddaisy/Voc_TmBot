package by.iba.bot.vocabulary.config;


import by.iba.bot.vocabulary.model.Command;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Mikalai Zaikin (nzaikin@iba.by)
 * @since 4Q2021
 */
@Component
@Getter
@Setter
public class BotConfig {

    /**
     * Contains TG bot name picked during bot registration
     */
    @Value("${bot_username}")
    private String name;

    /**
     * Contains TG bot token assigned during bot registration
     */
    @Value("${bot_token}")
    private String accessToken;

    /**
     * List of supported commands for this bot
     */
    private List<Command> commands;

    @PostConstruct
    public void init() {
        commands = Stream.of(BotCommand.values()).map(e -> new Command(e.getName(), e.getDescription())).collect(Collectors.toList());
    }

}