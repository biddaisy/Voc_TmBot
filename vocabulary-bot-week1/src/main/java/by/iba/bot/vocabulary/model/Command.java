package by.iba.bot.vocabulary.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * The POJO which contains a bot command
 *
 *  @author Mikalai Zaikin (nzaikin@iba.by)
 *  @since 4Q2021
 */
@Getter
@Setter
@AllArgsConstructor
public class Command {
    private String name;         // action (e.g. '/start', '/help', etc...)
    private String description;  // command description
}

