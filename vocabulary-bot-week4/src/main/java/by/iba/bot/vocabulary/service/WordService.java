package by.iba.bot.vocabulary.service;

import by.iba.bot.vocabulary.mongo.WordRepo;
import by.iba.bot.vocabulary.mongo.collection.Word;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static by.iba.bot.vocabulary.service.SessionService.MAX_NUM_OF_WORDS_TO_SHOW;

/**
 * A service class to work with words
 *
 *  @author Mikalai Zaikin (nzaikin@iba.by)
 *  @since 4Q2021
 */
@Service
@RequiredArgsConstructor
public class WordService {

    private final WordRepo wordRepo;

    private int numOfWords;

    @PostConstruct
    public void init() {
        numOfWords = (int)wordRepo.count();
    }

    public Word getRandomWord() {
        return wordRepo.findRandom(1).get(0);
    }

    public List<Word> getRandomWords(int numOfWords) {
        if (numOfWords < 1) {
            throw new IllegalArgumentException("At least 1 word must be requested");
        }
        if (numOfWords > MAX_NUM_OF_WORDS_TO_SHOW) {
            throw new IllegalArgumentException(String.format("No more than %d words may be requested", MAX_NUM_OF_WORDS_TO_SHOW));
        }
        return wordRepo.findRandom(numOfWords);
    }

    public Word getWord(int id) {
        if ((id < 0) || (id >= numOfWords)) {
            throw new IllegalArgumentException(String.format("Word ID must be between 0 and %d", numOfWords));
        }
        return wordRepo.findAllById(id);
    }

    /**
     * Total number of words in the dictionary
     */
    public int getCount() {
        return numOfWords;
    }
}