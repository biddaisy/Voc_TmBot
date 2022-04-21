package by.iba.bot.vocabulary.service;

import by.iba.bot.vocabulary.mongo.WordRepository;
import by.iba.bot.vocabulary.mongo.collection.Word;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Random;

/**
 * A service class to work with words
 *
 * @author Mikalai Zaikin (nzaikin@iba.by)
 * @since 4Q2021
 */
@Service
@RequiredArgsConstructor
public class WordService {

    private static final String[] ofWords = {"слово", "слова", "слов"};

    private final WordRepository wordRepository;

    private int wordCount;

    private Random random;

    /**
     * Called once on the bean construction
     */
    @PostConstruct
    public void init() {
        wordCount = (int) wordRepository.count();
        random = new Random();
    }

    public Word getRandomWord() {
        int randomId = random.nextInt(wordCount);
        return getWord(randomId);
    }

    public Word getWord(int id) {
        if (id < 0 || id >= wordCount) {
            throw new IllegalArgumentException(String.format("Word ID must be between 0 (inclusive) and %d (exclusive)", wordCount));
        }
        return wordRepository.findAllById(id);
    }

    /**
     * Total number of words in the dictionary
     */
    public int getCount() {
        return wordCount;
    }

    public String ofWord(int num) {
        if (num <= 0){
            throw new IllegalArgumentException("num must be positive");
        }
        int mod10 = num % 10;
        int mod100 = num % 100;
        if (mod100 >= 11 && mod100 <=19){
            return ofWords[2];
        } else if (mod10 ==1){
            return ofWords[0];
        } else if (mod10 >=2 && mod10 <=4){
            return ofWords[1];
        } else {
            return ofWords[2];
        }
    }

}