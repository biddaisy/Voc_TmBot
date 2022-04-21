package by.iba.bot.vocabulary.config;


import by.iba.bot.vocabulary.model.Command;
import lombok.Getter;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

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
     * List of supported commands for this bot
     */
    private List<Command> commands;

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

}