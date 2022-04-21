package by.iba.bot.vocabulary.command

import by.iba.bot.vocabulary.service.SessionService
import com.vdurmont.emoji.EmojiParser
import org.apache.commons.lang3.math.NumberUtils
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Update
import java.util.*
import javax.annotation.PostConstruct

@Component
class NotifySetBotCommand(
    private val sessionService: SessionService
) : BotCommand(Command.NOTIFYSET) {

    private lateinit var errorNotifySetMessage: String

    @PostConstruct
    fun init() {
        errorNotifySetMessage = generateErrorNotifySetMessage()
    }

    override fun handleUpdate(update: Update, chatId: Long, inputMessageText: String): String {
        return handleStateSetNotifyPeriod(chatId, inputMessageText)
    }

    override fun handleUpdate(update: Update, chatId: Long): String {
        sessionService.setBotCommand(chatId, Command.NOTIFYSET)
        return "Введите периодичность показа новых слов (в минутах) :"
    }

    private fun handleStateSetNotifyPeriod(chatId: Long, inputMessageText: String): String {
        val showInterval: Int = NumberUtils.toInt(inputMessageText)
        if (showInterval > 0) {
            sessionService.setShowInterval(chatId, showInterval)
            sessionService.resetBotCommand(chatId) // return to the default state
            return generateSuccessNotifySet(showInterval)
        } else {
            return errorNotifySetMessage
        }
    }

    private fun generateSuccessNotifySet(notifyPeriod: Int): String {
        return EmojiParser.parseToUnicode(":white_check_mark: Новый период показа новых слов - каждых $notifyPeriod минут(ы)")
    }


    private fun generateErrorNotifySetMessage(): String {
        return EmojiParser.parseToUnicode(
            ":x: Введите целочисленное положительное значение (в минутах) или отмените установку значения (/" + Command.CANCEL.toString()
                .lowercase(Locale.getDefault()) + ")"
        )
    }
}