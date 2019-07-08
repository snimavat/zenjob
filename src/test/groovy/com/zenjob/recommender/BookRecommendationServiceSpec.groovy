package com.zenjob.recommender

import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class BookRecommendationServiceSpec extends Specification implements ServiceUnitTest<BookRecommendationService>, DataTest{
    def setup() {
        mockDomains(Book, UserPreference)
        BookRecommender recommender = Mock()
        service.bookRecommender = recommender
    }

    void "test createLike"() {
        when: "create new"
        service.createLike(1, 1)
        UserPreference pref = UserPreference.findByUserIdAndBookId(1, 1)

        then:
        pref != null
        pref.preference == 1

        when: "updat existing"
        service.createDislike(1, 1)

        then: "existing record should have been updated"
        1 == UserPreference.countByUserIdAndBookId(1, 1,)
        pref.preference == -1

    }

    void "test createDislike"() {
        when: "create new"
        service.createDislike(1, 1)
        UserPreference pref = UserPreference.findByUserIdAndBookId(1, 1)

        then:
        pref != null
        pref.preference == -1

        when: "updat existing"
        service.createLike(1, 1)

        then: "existing record should have been updated"
        1 == UserPreference.countByUserIdAndBookId(1, 1,)
        pref.preference == 1
    }

    void "test recommend"() {
        setup:
        Book b = new Book(id: 1, asin: 1, title: "test", author: "text", genre: "test").save()

        when:
        List<Book> result = service.recommend(1, 20)

        then:
        1 * service.bookRecommender.recommend(1, 20) >> [1]
        result.size() == 1
        result.contains(b)
    }
}
