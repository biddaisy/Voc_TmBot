package by.iba.bot.vocabulary.telegram;

import by.iba.bot.vocabulary.config.BotCommand;
import by.iba.bot.vocabulary.config.BotConfig;
import by.iba.bot.vocabulary.model.SessionStatus;
import by.iba.bot.vocabulary.mongo.collection.Word;
import by.iba.bot.vocabulary.service.SessionService;
import by.iba.bot.vocabulary.service.WordService;
import com.vdurmont.emoji.EmojiParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The utility class to compose text messages for user interaction
 *
 * @author Mikalai Zaikin (nzaikin@iba.by)
 * @since 4Q2021
 */
@Service
@RequiredArgsConstructor
public class MessageGenerator {

    public static final String WELCOME = "Приветствую, ";
    public static final String BN = "</b>\n";

    private final SessionService sessionService;
    private final BotConfig botConfig;
    private final WordService wordService;

    public String generateStartMessage(String name) {
        return EmojiParser.parseToUnicode(WELCOME + name + "! :wave: \nЧтобы узнать, как мной пользоваться - введите " + BotCommand.HELP.getName());
    }

    public String generateNotifyOnMessage(String name) {
        return EmojiParser.parseToUnicode(WELCOME + name + "!\n :stopwatch: Вы включили режим показа новых слов, для его корректной работы также должен быть выбран временной интервал между показами (" + BotCommand.NOTIFYSET.getName() + "). Отмена режима показа - командой " + BotCommand.NOTIFYOFF.getName());
    }

    public String generateNotifyOffMessage(String name) {
        return EmojiParser.parseToUnicode(WELCOME + name + "!\n :x: Уведомления с новыми словами отключены. \nБот не будет присылать новые слова для изучения пока вы не включите снова командой " + BotCommand.NOTIFYON.getName());
    }

    public String generateHelpMessage() {
        String message = botConfig.getCommands().stream().reduce(":bulb: <b>Доступные команды</b>\n",
                (m, c) -> m + c.getName() + " - " + c.getDescription() + "\n", (m1, m2) -> m1);
        return EmojiParser.parseToUnicode(message);
    }

    public String generateSuccessCancel() {
        return EmojiParser.parseToUnicode(":white_check_mark: Активная команда успешно отклонена");
    }

    public String generateSuccessNotifySet(Integer notifyPeriod) {
        return EmojiParser.parseToUnicode(":white_check_mark: Новый период показа новых слов - каждых " + notifyPeriod + " минут(ы)");
    }

    public String generateSuccessWordSet(Integer numOfWords) {
        return EmojiParser.parseToUnicode(":white_check_mark: За один раз будет показано " + numOfWords + ((numOfWords == 1) ? " слово" : " слов(а)"));
    }

    public String generateErrorNotifySet() {
        return EmojiParser.parseToUnicode(":x: Введите целочисленное положительное значение (в минутах) или отмените установку значения (/" + BotCommand.CANCEL.toString().toLowerCase() + ")");
    }

    public String generateErrorWordSet() {
        return EmojiParser.parseToUnicode(":x: Введите целочисленное положительное значение от 1 до " + SessionService.MAX_NUM_OF_WORDS_TO_SHOW + " включительно или отмените установку значения (/" + BotCommand.CANCEL.toString().toLowerCase() + ")");
    }

    public String generateStatusMessage(SessionStatus ss) {
        Long currentPeriod = null;
        if (ss.getLastShownTime() != null) {
            LocalDateTime lastShownTime = ss.getLastShownTime();
            LocalDateTime now = LocalDateTime.now();
            Duration duration = Duration.between(lastShownTime, now);
            currentPeriod = duration.toMinutes();
        }

        String message = ":information_source: <b>Статус вашей бот-сессии</b>\n";
        message += "Автоматический показ новых слов: <b>" + (ss.isShowWords() ? "ВКЛ" : "ВЫКЛ") + BN;
        message += "Периодичность показа слов: <b>" + (ss.getShowInterval() != null ? ss.getShowInterval() + " минут(ы)" : "не задан") + BN;
        message += "Количество слов показываемых за один раз: <b>" + ss.getNumOfWordsToShow() + BN;
        message += "Предыдущее уведомление отправлено: <b>" + (currentPeriod != null ? currentPeriod + " минут(ы) назад" : "никогда") + BN;
        return EmojiParser.parseToUnicode(message);
    }

    public String generateWordsMessage(List<Word> wordList) {
        return EmojiParser.parseToUnicode(":open_book:\n" + wordList.stream().map(this::generateWordMessage).collect(Collectors.joining()));
    }

    public String generateWordMessage(Word word) {
        StringBuilder sb = new StringBuilder();
        sb.append("<b>").append(word.getValue()).append("</b>");
        sb.append(word.getStress() != null ? "  " + word.getStress() + "  " : "");
        sb.append(word.getTranscription() != null ? "  [" + word.getTranscription() + "]" : "");
        sb.append("\n  ");
        addTranslations(word, sb);
        addUsages(word, sb);
        sb.append("\n");
        return sb.toString();
    }

    private void addUsages(Word word, StringBuilder sb) {
        if (word.getUsages() != null) {
            for (Word.Usage wu : word.getUsages()) {
                sb.append(wu.en != null ? "<i>" + wu.en + "</i>\n  " : "");
                sb.append(wu.ru != null ? "<i>" + wu.ru + "</i>\n  " : "");
            }
        }
    }

    private void addTranslations(Word word, StringBuilder sb) {
        for (Word.Translation t : word.getTranslations()) {
            sb.append(t.en != null ? "- " + t.en + "\n  " : "");
            sb.append(t.ru != null ? "- " + t.ru + "\n  " : "");
        }
    }

    public String generateWordMessage() {
        Word word = wordService.getRandomWord();
        return generateWordMessage(word);
    }

    public String generateExitMessage(String name) {
        return EmojiParser.parseToUnicode("До свидания, " + name + "! :wave: \nПриятно было пообщаться!");
    }

    public String generateAboutMessage() throws UnknownHostException {
        String sb = ":information_source: <b>Информация</b>\n" + ":black_small_square: Слов в словаре: <b>" + wordService.getCount() + "</b>" +
                "\n" +
                ":black_small_square: Язык словаря: <b>" + botConfig.getWordsCollectionLanguage() + "</b>" +
                "\n" +
                ":black_small_square: Сессий пользователей: <b>" + sessionService.getCount() + "</b>" +
                "\n" +
                ":black_small_square: Сервер: <b>" + InetAddress.getLocalHost().getHostName() + "</b>" +
                "\n" +
                "\n" +
                ":man_technologist: Автор: <b>@IBA10_VocabularyBot</b> © " + Year.now().getValue();
        return EmojiParser.parseToUnicode(sb);
    }
}