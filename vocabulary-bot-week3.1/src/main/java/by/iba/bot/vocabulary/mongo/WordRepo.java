package by.iba.bot.vocabulary.mongo;

import by.iba.bot.vocabulary.mongo.collection.Word;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Mongo repository interface to access words
 *
 *  @author Mikalai Zaikin (nzaikin@iba.by)
 *  @since 4Q2021
 */
@Repository
public interface WordRepo extends MongoRepository<Word, Integer> {

    Word findAllById(Integer id);

    /**
     * Tells us the total number of words in the collection
     */
    @Override
    long count();

    /**
     * Get a random list of words from the collection
     */
    @Aggregation("{$sample: {size: ?0} }")
    List<Word> findRandom(int quantity);
}