package by.iba.bot.vocabulary;

import by.iba.bot.vocabulary.config.BotCommand;
import by.iba.bot.vocabulary.model.Command;
import by.iba.bot.vocabulary.config.BotConfig;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;


import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The main application class.
 * Starts the application and keeps in running state.
 * NOTE: unlike most Spring Boot applications we do not accept external HTTP requests.
 *
 *  @author Mikalai Zaikin (nzaikin@iba.by)
 *  @since 4Q2021
 */
@SpringBootApplication
@EnableScheduling
@RequiredArgsConstructor
public class Main implements CommandLineRunner {

    private final BotConfig botConfig;

    public static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        new SpringApplicationBuilder(Main.class)
                .web(WebApplicationType.NONE) // disable default Tomcat server, we don't need to listen HTTP(S)  port
                .run(args);
    }

    /**
     * Perform bot configuration on startup.
     */
    @Override
    public void run(String... args) {
        List<Command> commands = Stream.of(BotCommand.values()).map(e -> new Command(e.getName(), e.getDescription())).collect(Collectors.toList());
        botConfig.setCommands(commands);
        LOG.info("Telegram Bot was successfully configured");
    }
}
