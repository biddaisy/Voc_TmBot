package by.iba.bot.vocabulary.command

import org.telegram.telegrambots.meta.api.objects.Update

abstract class BotCommand(val command: Command) {

    abstract fun handleUpdate(update: Update, chatId: Long, inputMessageText: String): String

    abstract fun handleUpdate(update: Update, chatId: Long): String

}