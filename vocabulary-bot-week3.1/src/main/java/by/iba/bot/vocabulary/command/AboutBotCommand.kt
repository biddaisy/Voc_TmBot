package by.iba.bot.vocabulary.command

import by.iba.bot.vocabulary.config.BotConfig
import by.iba.bot.vocabulary.service.SessionService
import by.iba.bot.vocabulary.service.WordService
import com.vdurmont.emoji.EmojiParser
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Update
import java.net.InetAddress
import java.net.UnknownHostException
import java.time.Year

@Component
class AboutBotCommand(
    private val botConfig: BotConfig,
    private val wordService: WordService,
    private val sessionService: SessionService
) : BotCommand(Command.ABOUT) {

    override fun handleUpdate(update: Update, chatId: Long, inputMessageText: String): String {
        throw UnsupportedOperationException()
    }

    override fun handleUpdate(update: Update, chatId: Long): String {
        return generateAboutMessage();
    }

    @Throws(UnknownHostException::class)
    private fun generateAboutMessage(): String {
        val sb = StringBuilder(":information_source: <b>Информация</b>\n")
        sb.append(":black_small_square: Язык словаря: ").append(botConfig.getWordsCollectionLanguage())
        sb.append("\n")
        sb.append(":black_small_square: Слов в словаре: <b>").append(wordService.getCount()).append("</b>")
        sb.append("\n")
        sb.append(":black_small_square: Сессий пользователей: <b>").append(sessionService.getCount()).append("</b>")
        sb.append("\n")
        sb.append(":black_small_square: Сервер: <b>").append(InetAddress.getLocalHost().hostName).append("</b>")
        sb.append("\n")
        sb.append("\n")
        sb.append(":man_technologist: Автор: <b>").append("@IBA10_VocabularyBot").append("</b> © ")
            .append(Year.now().value)
        return EmojiParser.parseToUnicode(sb.toString())
    }
}