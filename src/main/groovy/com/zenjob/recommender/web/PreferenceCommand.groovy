package com.zenjob.recommender.web

import com.zenjob.recommender.UserPreference
import grails.compiler.GrailsCompileStatic
import grails.validation.Validateable

@GrailsCompileStatic
class PreferenceCommand implements Validateable {
    Long userId
    Long bookId
    Integer preference

    static constraints = {
        userId nullable: false
        bookId nullable: false
        preference nullable: false, inList: [UserPreference.LIKE, UserPreference.DISLIKE]
    }
}
