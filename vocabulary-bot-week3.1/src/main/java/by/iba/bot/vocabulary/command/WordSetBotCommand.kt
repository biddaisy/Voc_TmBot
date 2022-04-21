package by.iba.bot.vocabulary.command

import by.iba.bot.vocabulary.service.SessionService
import com.vdurmont.emoji.EmojiParser
import org.apache.commons.lang3.math.NumberUtils
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Update
import java.util.*
import javax.annotation.PostConstruct

@Component
class WordSetBotCommand(
    private val sessionService: SessionService
) : BotCommand(Command.WORDSET) {

    private lateinit var errorWordSetMessage: String

    @PostConstruct
    fun init() {
        errorWordSetMessage = generateErrorWordSetMessage()
    }

    override fun handleUpdate(update: Update, chatId: Long, inputMessageText: String): String {
        return handleSetNumberOfWords(chatId, inputMessageText);
    }

    override fun handleUpdate(update: Update, chatId: Long): String {
        sessionService.setBotCommand(chatId, Command.WORDSET)
        return "Введите количество показываемых за один раз слов (1.." + SessionService.MAX_NUM_OF_WORDS_TO_SHOW + ") :"
    }

    private fun handleSetNumberOfWords(chatId: Long, messageText: String): String {
        val numOfWords: Int = NumberUtils.toInt(messageText)
        if (numOfWords > 0 && numOfWords <= SessionService.MAX_NUM_OF_WORDS_TO_SHOW) {
            sessionService.setNumOfWordsToShow(chatId, numOfWords)
            sessionService.resetBotCommand(chatId) // return to the default state
            return generateSuccessWordSet(numOfWords)
        } else {
            return errorWordSetMessage
        }
    }

    fun generateSuccessWordSet(numOfWords: Int): String {
        return EmojiParser.parseToUnicode(":white_check_mark: За один раз будет показано " + numOfWords + if (numOfWords == 1) " слово" else " слов(а)")
    }

    private fun generateErrorWordSetMessage(): String {
        return EmojiParser.parseToUnicode(
            ":x: Введите целочисленное положительное значение от 1 до " + SessionService.MAX_NUM_OF_WORDS_TO_SHOW + " включительно или отмените установку значения (/" + Command.CANCEL.toString()
                .lowercase(Locale.getDefault()) + ")"
        )
    }
}