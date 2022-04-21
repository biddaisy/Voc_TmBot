package by.iba.bot.vocabulary;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * The main application class.
 * Starts the application and keeps in running state.
 * NOTE: unlike most Spring Boot applications we do not accept external HTTP requests.
 *
 *  @author Mikalai Zaikin (nzaikin@iba.by)
 *  @since 4Q2021
 */
@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class VocabularyBotApplication implements CommandLineRunner {

    public static void main(String[] args) {
        new SpringApplicationBuilder(VocabularyBotApplication.class)
                .web(WebApplicationType.NONE) // disable default Tomcat server, we don't need to listen HTTP(S)  port
                .run(args);
    }

    /**
     * Perform bot configuration on startup.
     */
    @Override
    public void run(String... args) {
        log.info("Telegram Bot successfully configured");
    }
}
