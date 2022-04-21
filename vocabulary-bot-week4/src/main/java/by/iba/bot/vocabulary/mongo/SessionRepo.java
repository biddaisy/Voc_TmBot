package by.iba.bot.vocabulary.mongo;

import by.iba.bot.vocabulary.mongo.collection.Session;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Mongo repository interface to access chat configs
 *
 *  @author Mikalai Zaikin (nzaikin@iba.by)
 *  @since 4Q2021
 */
public interface SessionRepo extends MongoRepository<Session, Integer> {

    Session findAllByChatId(Long chatId);

    /**
     * A method to retrieve all sessions eligible for notification:
     *   1) show words flag is `true`
     *   2) show words interval is a valid value
     */
    @Query("{ 'showWords' : true, 'showInterval' : {$ne:null} }")
    List<Session> findAllToNotify();

}