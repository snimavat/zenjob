package com.zenjob.recommender

import com.zenjob.recommender.web.PreferenceCommand
import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
class BookController {
	static responseFormats = ['json']
    static allowedMethods = [createPreference: "POST"]

    BookRecommendationService bookRecommendationService

    /**
     * Create user preference for given book
     */
    def createPreference(PreferenceCommand command) {
        command.bookId = params.long('id')
        command.validate()

        if (command.hasErrors()) {
            respond command.errors
            return
        }

        respond bookRecommendationService.createPreference(command.userId, command.bookId, command.preference)
    }

}
