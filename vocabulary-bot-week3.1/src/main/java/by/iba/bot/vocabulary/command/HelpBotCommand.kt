package by.iba.bot.vocabulary.command

import by.iba.bot.vocabulary.config.BotConfig
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Update

@Component
class HelpBotCommand(val botConfig: BotConfig) : BotCommand(Command.HELP) {

    override fun handleUpdate(update: Update, chatId: Long, inputMessageText: String): String {
        throw UnsupportedOperationException()
    }

    override fun handleUpdate(update: Update, chatId: Long): String {
        return botConfig.helpMessage
    }

}