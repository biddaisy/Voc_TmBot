package by.iba.bot.vocabulary.command

import by.iba.bot.vocabulary.model.SessionStatus
import by.iba.bot.vocabulary.service.SessionService
import com.vdurmont.emoji.EmojiParser
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Update
import java.time.Duration
import java.time.LocalDateTime

@Component
class StatusBotCommand(
    private val sessionService: SessionService
) : BotCommand(Command.STATUS) {

    override fun handleUpdate(update: Update, chatId: Long, inputMessageText: String): String {
        throw UnsupportedOperationException()
    }

    override fun handleUpdate(update: Update, chatId: Long): String {
        val sessionStatus: SessionStatus = sessionService.getSessionStatus(chatId)
        return generateStatusMessage(sessionStatus)
    }

    fun generateStatusMessage(ss: SessionStatus): String {
        val currentPeriod: Long? = updateCurrentPeriod(ss)
        var message = ":information_source: "+ MessageConstants.B + "Статус вашей бот-сессии" + MessageConstants.BN
        message += "Автоматический показ новых слов: " + MessageConstants.B + (if (ss.isShowWords) "ВКЛ" else "ВЫКЛ") + MessageConstants.BN
        message += "Периодичность показа слов: " + MessageConstants.B + (if (ss.showInterval != null) ss.showInterval.toString() + " минут(ы)" else "не задан") + MessageConstants.BN
        message += "Количество слов показываемых за один раз: " + MessageConstants.B + ss.numOfWordsToShow + MessageConstants.BN
        message += "Предыдущее уведомление отправлено: " + MessageConstants.B + (if (currentPeriod != null) "$currentPeriod минут(ы) назад" else "никогда") + MessageConstants.BN
        return EmojiParser.parseToUnicode(message)
    }

    private fun updateCurrentPeriod(ss: SessionStatus): Long? {
        var currentPeriod: Long? = null
        if (ss.lastShownTime != null) {
            val lastShownTime = ss.lastShownTime
            val now = LocalDateTime.now()
            val duration = Duration.between(lastShownTime, now)
            currentPeriod = duration.toMinutes()
        }
        return currentPeriod
    }
}