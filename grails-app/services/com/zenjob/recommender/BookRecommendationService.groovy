package com.zenjob.recommender

import grails.compiler.GrailsCompileStatic
import grails.gorm.transactions.Transactional

@GrailsCompileStatic
@Transactional
class BookRecommendationService {

    BookRecommender bookRecommender

    UserPreference createLike(Long user, Long book) {
        UserPreference.createLike(user, book)
    }

    UserPreference createDislike(Long user, Long book) {
        UserPreference.createDislike(user, book)
    }

    UserPreference createPreference(Long user, Long book, Integer preference) {
        UserPreference.createPreference(user, book, preference)
    }

    /**
     * Returns a collection of recommended books for given user
     *
     */
    @Transactional(readOnly = true)
    Collection<Book> recommend(Long user, Integer limit = 20) {
        Collection<Long> books = bookRecommender.recommend(user, limit)
        return books.collect({ Long id ->  Book.get(id) })
    }

}
