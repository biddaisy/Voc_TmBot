package by.iba.bot.vocabulary.command

import by.iba.bot.vocabulary.service.SessionService
import com.vdurmont.emoji.EmojiParser
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Update
import javax.annotation.PostConstruct

@Component
class CancelBotCommand(
    private val sessionService: SessionService
) : BotCommand(Command.CANCEL) {

    private lateinit var successCancelMessage: String

    @PostConstruct
    fun init() {
        successCancelMessage = generateSuccessCancelMessage();
    }

    override fun handleUpdate(update: Update, chatId: Long, inputMessageText: String): String {
        sessionService.resetBotCommand(chatId)
        return successCancelMessage
    }

    override fun handleUpdate(update: Update, chatId: Long): String {
        return "Нет активной команды для отмены"
    }

    private fun generateSuccessCancelMessage(): String {
        return EmojiParser.parseToUnicode(":white_check_mark: Активная команда успешно отклонена")
    }
}