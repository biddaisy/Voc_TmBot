package by.iba.bot.vocabulary.command

import by.iba.bot.vocabulary.config.BotConfig
import by.iba.bot.vocabulary.service.SessionService
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Update

@Component
class BotCommander(
    private val sessionService: SessionService,
    private val botConfig: BotConfig
) {

    fun handleUpdate(update: Update): String? {
        if (!update.hasMessage()) return null
        val chatId = update.message.chatId
        val inputMessageText = update.message.text
        checkChatInit(chatId)
        val command = Command.valueFrom(inputMessageText) ?: Command.HELP
        val activeBotCommand = sessionService.getBotCommand(chatId)
        if (activeBotCommand != null) {
            val botCommand = botConfig.getBotCommand(checkCancel(command, activeBotCommand))
            return botCommand.handleUpdate(update, chatId, inputMessageText)
        } else {
            val botCommand = botConfig.getBotCommand(command)
            return botCommand.handleUpdate(update, chatId)
        }
    }

    private fun checkCancel(
        command: Command,
        activeCommand: Command
    ): Command = if (command == Command.CANCEL) command else activeCommand

    private fun checkChatInit(chatId: Long?) {
        if (!sessionService.isChatInit(chatId)) {
            sessionService.initChat(chatId)
        }
    }
}