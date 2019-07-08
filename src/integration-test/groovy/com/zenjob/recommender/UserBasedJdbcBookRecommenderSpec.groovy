package com.zenjob.recommender

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import org.grails.datastore.gorm.GormEnhancer
import org.grails.datastore.mapping.core.Datastore
import spock.lang.Specification

import static com.zenjob.recommender.UserPreference.DISLIKE
import static com.zenjob.recommender.UserPreference.LIKE

@Integration
@Rollback
class UserBasedJdbcBookRecommenderSpec extends Specification {

    UserBasedJdbcBookRecommender bookRecommender

    void "test recommendations"() {
        setup:
        loadTestData()
        bookRecommender.proximityThreshold = 3

        expect: "just verify test data is loaded"
        UserPreference.count() > 0

        when: "limit is set"
        List<Long> result = bookRecommender.recommend(2, 1)

        then: "returns just specified number of records"
        result.size() == 1
        result[0] == 13

        when: "test for user 2"
        result = bookRecommender.recommend(2, 20)

        then: "7 items from user:1 and 1 item from user:3 should be selected"
        result.size() == 7
        result[0] == 13 //user:3 has 4 similar preferences - so books from him should rank higher
        !result.contains(14L) //14 comes as preference from user:3 but user has disliked it.
        result.containsAll([13L, 3L, 6L, 7L, 8L, 9L, 10L])
        Collections.disjoint(result, [1L, 2L, 4L, 5L, 14L]) == true //should not have returned any items already rated

        when: "verify for user:1"
        result = bookRecommender.recommend(1, 20)

        then:
        result.size() == 2
        result.containsAll([13L, 14L])

        when: "verify for user:13"
        result = bookRecommender.recommend(3, 20)

        then: "verify for user:3"
        result.size() == 6
        result.containsAll([3L, 6L, 7L, 8L, 9L, 10L])

        when: "verify threshold"
        bookRecommender.proximityThreshold = 2
        result = bookRecommender.recommend(3, 20)

        then: "two items from user:4 should be returned along with 6 items from user:1"
        result.size() == 8
        result.containsAll([15L, 16L])
    }

    void loadTestData() {

        /*
          user:2 has 4 similar preferences as user:3 and 3 similar preferences as user:1
          user:1 has 3 similar preferences as user:1 and user:3
          user:3 & user:4 has two similar preferences

         */

        UserPreference.createPreference(1, 1, LIKE)
        UserPreference.createPreference(1, 2, LIKE)
        UserPreference.createPreference(1, 3, LIKE)
        UserPreference.createPreference(1, 4, LIKE)
        UserPreference.createPreference(1, 5, DISLIKE)
        UserPreference.createPreference(1, 6, LIKE)
        UserPreference.createPreference(1, 7, LIKE)
        UserPreference.createPreference(1, 8, LIKE)
        UserPreference.createPreference(1, 9, LIKE)
        UserPreference.createPreference(1, 10, LIKE)
        UserPreference.createPreference(1, 11, DISLIKE)
        UserPreference.createPreference(1, 12, DISLIKE)

        UserPreference.createPreference(2, 1, LIKE)
        UserPreference.createPreference(2, 2, LIKE)
        UserPreference.createPreference(2, 4, DISLIKE)
        UserPreference.createPreference(2, 5, DISLIKE)
        UserPreference.createPreference(2, 14, DISLIKE)

        UserPreference.createPreference(3, 1, LIKE)
        UserPreference.createPreference(3, 2, LIKE)
        UserPreference.createPreference(3, 4, DISLIKE)
        UserPreference.createPreference(3, 5, DISLIKE)
        UserPreference.createPreference(3, 13, LIKE)
        UserPreference.createPreference(3, 14, LIKE)

        UserPreference.createPreference(4, 13, LIKE)
        UserPreference.createPreference(4, 14, LIKE)
        UserPreference.createPreference(4, 15, LIKE)
        UserPreference.createPreference(4, 16, LIKE)


        //flush so tht data is visible to jdbc
        Datastore ds = GormEnhancer.findSingleDatastore()
        if(ds.hasCurrentSession()) ds.getCurrentSession().flush()
    }


}
