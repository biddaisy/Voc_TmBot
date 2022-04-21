package by.iba.bot.vocabulary.command

import by.iba.bot.vocabulary.service.SessionService
import com.vdurmont.emoji.EmojiParser
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Update

@Component
class NotifyOffBotCommand(
    private val sessionService: SessionService
) : BotCommand(Command.NOTIFYOFF) {

    override fun handleUpdate(update: Update, chatId: Long, inputMessageText: String): String {
        throw UnsupportedOperationException()
    }

    override fun handleUpdate(update: Update, chatId: Long): String {
        sessionService.setShowWords(chatId, false)
        return generateNotifyOffMessage(update.message.chat.firstName)
    }

    fun generateNotifyOffMessage(name: String): String {
        return EmojiParser.parseToUnicode(
            """${MessageConstants.WELCOME}$name!
 :x: Уведомления с новыми словами отключены. 
Бот не будет присылать новые слова для изучения пока вы не включите снова командой ${Command.NOTIFYON.getName()}"""
        )
    }

}