package by.iba.bot.vocabulary.config;

/**
 * This enum list states of the bot.
 *   1) Default state -- bot can accept any command
 *   2) Set notify period state -- the bot may accept only positive integer number
 *   3) Set number of words -- define how many words to show each time (1 or more)
 *
 *  @author Mikalai Zaikin (nzaikin@iba.by)
 *  @since 4Q2021
 */
public enum BotState {
    DEFAULT, SET_NOTIFY_PERIOD, SET_NUMBER_OF_WORDS
}