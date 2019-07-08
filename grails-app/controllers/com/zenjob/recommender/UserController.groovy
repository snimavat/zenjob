package com.zenjob.recommender

import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
class UserController {
	static responseFormats = ['json']
    static allowedMethods = [preferences: "GET", recommendations: "GET"]

    BookRecommendationService bookRecommendationService

    /**
     * List user's preferences
     */
    def preferences(Long id) {
        respond UserPreference.findAllByUserId(id)
    }

    /**
     * Book Recommendations for given user
     */
    def recommendations(Long id) {
        Collection<Book> books = bookRecommendationService.recommend(id)
        respond books
    }

}
