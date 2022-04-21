package by.iba.bot.vocabulary.mongo;

import by.iba.bot.vocabulary.mongo.collection.Word;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Mongo repository interface to access words
 *
 *  @author Mikalai Zaikin (nzaikin@iba.by)
 *  @since 4Q2021
 */
@Repository
public interface WordRepository extends MongoRepository<Word, Integer> {

    Word findAllById(Integer id);

}