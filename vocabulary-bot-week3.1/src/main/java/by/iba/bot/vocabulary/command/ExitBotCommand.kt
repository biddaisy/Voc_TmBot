package by.iba.bot.vocabulary.command

import by.iba.bot.vocabulary.service.SessionService
import com.vdurmont.emoji.EmojiParser
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Update

@Component
class ExitBotCommand(
    private val sessionService: SessionService
) : BotCommand(Command.EXIT) {

    override fun handleUpdate(update: Update, chatId: Long, inputMessageText: String): String {
        throw UnsupportedOperationException()
    }

    override fun handleUpdate(update: Update, chatId: Long): String {
        sessionService.exit(chatId)
        return generateExitMessage(update.message.chat.firstName)
    }

    private fun generateExitMessage(name: String): String {
        return EmojiParser.parseToUnicode("До свидания, $name! :wave: \nПриятно было пообщаться!")
    }
}