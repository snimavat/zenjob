package com.zenjob.recommender

import grails.gorm.transactions.Transactional
import groovy.sql.GroovyRowResult
import groovy.sql.Sql
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

import javax.sql.DataSource

/**
 * Jdbc based implementation for BookRecommender.
 * It works by identifying users who have similar preference (likes/dislikes) as given user and returns the book Ids
 * liked by similar user's but not yet rated by given user.
 *
 * A user with higher number of similar preferences as given user will have higher weightage and books from him will
 * rank higher.
 */

@CompileStatic
@Component("bookRecommender")
class UserBasedJdbcBookRecommender implements BookRecommender {

    @Autowired
    DataSource dataSource

    /**
     * specify how many preferences should match before user is considered a similar user.
     */
    @Value('${zenjob.recommender.proximityThreshold:3}')
    int proximityThreshold

    @Override
    @Transactional(readOnly = true)
    Collection<Long> recommend(Long userId, Integer numRecommendations) {

        /*
         * The query selects the user's with similar taste as given user based on likes/dislikes
         * User's are given weightage based on number of similar preference
         */
        String similarUsersQuery = """
            select p2.user_id, count(p2.book_id) as weight 
            from user_preferences p1 join user_preferences p2
                 on p1.book_id = p2.book_id and p1.preference = p2.preference
                 where 
                    p1.user_id = :userId
                    and p2.user_id != p1.user_id
                 group by p2.user_id having count(p2.book_id) >= :threshold

        """

       /*
        * Finds the books which are liked by similar users, ordered by weightage.
        * Books which are disliked by the given user are ignored
        */
       String recommendationQuery = """
        select preferences.book_id, sum(similar_users.weight) as weight
         from (${similarUsersQuery}) as similar_users
         join 
            user_preferences preferences
            on preferences.user_id = similar_users.user_id and preferences.preference > 0 /* ignores dislikes */
         where 
            not exists(select 1 from user_preferences where preferences.book_id = user_preferences.book_id and user_preferences.user_id = :userId)
         group by preferences.book_id 
         order by weight desc
         limit :limit
        """

        Sql sql = new Sql(dataSource)

        Map parameters = [userId: userId, threshold: proximityThreshold, limit: numRecommendations]
        List<GroovyRowResult> rows = sql.rows(recommendationQuery, parameters)

        return rows.collect({ it.book_id as Long})
    }
}
