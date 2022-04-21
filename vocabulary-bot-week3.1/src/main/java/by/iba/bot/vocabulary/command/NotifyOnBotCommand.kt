package by.iba.bot.vocabulary.command

import by.iba.bot.vocabulary.service.SessionService
import com.vdurmont.emoji.EmojiParser
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Update

@Component
class NotifyOnBotCommand(
    private val sessionService: SessionService
) : BotCommand(Command.NOTIFYON) {

    override fun handleUpdate(update: Update, chatId: Long, inputMessageText: String): String {
        TODO("Not yet implemented")
    }

    override fun handleUpdate(update: Update, chatId: Long): String {
        sessionService.setShowWords(chatId, true)
        return generateNotifyOnMessage(update.message.chat.firstName)
    }

    fun generateNotifyOnMessage(name: String): String {
        return EmojiParser.parseToUnicode(
            """${MessageConstants.WELCOME}$name!
 :stopwatch: Вы включили режим показа новых слов, для его корректной работы также должен быть выбран временной интервал между показами (${Command.NOTIFYSET.getName()}). Отмена режима показа - командой ${Command.NOTIFYOFF.getName()}"""
        )
    }

}