package by.iba.bot.vocabulary.command

import by.iba.bot.vocabulary.mongo.collection.Word
import by.iba.bot.vocabulary.service.WordService
import com.vdurmont.emoji.EmojiParser
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Update

@Component
class WordBotCommand(
    private val wordService: WordService
) : BotCommand(Command.WORD) {

    override fun handleUpdate(update: Update, chatId: Long, inputMessageText: String): String {
        throw UnsupportedOperationException()
    }

    override fun handleUpdate(update: Update, chatId: Long): String {
        return generateWordMessage()
    }

    fun generateWordsMessage(words: List<Word>): String {
        return EmojiParser.parseToUnicode(
            ":open_book:\n" + words.map {this.generateWordMessage(it)}.joinToString ()
        )
    }

    private fun generateWordMessage(): String {
        val word: Word = wordService.getRandomWord()
        return generateWordMessage(word)
    }

    private fun generateWordMessage(word: Word): String {
        val sb: StringBuilder = getWordDescription(word)
        addWordTranslation(word, sb)
        if (word.usages != null) {
            for (wu in word.usages) {
                sb.append(if (wu.en != null) "<i>${wu.en}</i>" else "")
                sb.append(if (wu.ru != null) "<i>${wu.ru}</i>" else "")
            }
        }
        sb.append("\n")
        return sb.toString()
    }

    private fun addWordTranslation(word: Word, sb: StringBuilder) {
        for (t in word.translations) {
            sb.append(t.en ?: "")
            sb.append(t.ru ?: "")
        }
    }

    private fun getWordDescription(word: Word): StringBuilder {
        val sb = StringBuilder()
        sb.append("<b>").append(word.value).append("</b>")
        sb.append(if (word.stress != null) "  " + word.stress + "  " else "")
        sb.append(if (word.transcription != null) "  [" + word.transcription + "]" else "")
        sb.append("\n  ")
        return sb
    }

}