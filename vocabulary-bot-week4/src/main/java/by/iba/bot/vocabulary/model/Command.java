package by.iba.bot.vocabulary.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * The POJO which contains a bot command
 *
 *  @author Mikalai Zaikin (nzaikin@iba.by)
 *  @since 4Q2021
 */
@Getter
@RequiredArgsConstructor
public class Command {
    private final String name;         // action (e.g. '/start', '/help', etc...)
    private final String description;  // command description
}

