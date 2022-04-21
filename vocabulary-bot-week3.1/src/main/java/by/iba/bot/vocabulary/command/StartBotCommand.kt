package by.iba.bot.vocabulary.command

import com.vdurmont.emoji.EmojiParser
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Update


@Component
class StartBotCommand : BotCommand(Command.START) {

    override fun handleUpdate(update: Update, chatId: Long, inputMessageText: String): String {
        throw UnsupportedOperationException()
    }

    override fun handleUpdate(update: Update, chatId: Long): String {
        return generateStartMessage(update.message.chat.firstName)
    }

    private fun generateStartMessage(name: String): String {
        return EmojiParser.parseToUnicode(
            """
            ${MessageConstants.WELCOME}$name! :wave: 
            Чтобы узнать, как мной пользоваться - введите ${Command.HELP.getName()}
            """.trimIndent()
        )
    }
}