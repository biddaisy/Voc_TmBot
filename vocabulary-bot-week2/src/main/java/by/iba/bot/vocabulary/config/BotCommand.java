package by.iba.bot.vocabulary.config;

/**
 * This enum contains all commands supported by the Telegram bot
 *
 * @author Mikalai Zaikin (nzaikin@iba.by)
 * @since 4Q2021
 */
public enum BotCommand {
    START("Старт"),
    HELP("Помощь"),
    WORD("Показать новое слово сейчас"),
    ABOUT("Что за бот ?");

    private final String description;

    BotCommand(String description) {
        this.description = description;
    }

    /**
     * Text description of what the command does
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns a command as it must be typed by the user in the chat, i.e. '/start'
     */
    public String getName() {
        return "/" + name().toLowerCase();
    }
}