package com.zenjob.recommender

import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
class UserPreference implements Serializable {

    final static Integer LIKE = 1
    final static Integer DISLIKE = -1

    Long userId
    Long bookId

    /**
     * -1 represents dislike, +1 represents like
     */
    Integer preference

    Date dateCreated

    static constraints = {
        userId nullable: false
        bookId nullable: false
        preference nullable: false
    }

    static mapping = {
        table "user_preferences"
        id composite:['userId', 'bookId']
    }

    static UserPreference createLike(Long uId, Long bId) {
        return createPreference(uId, bId, LIKE)
    }

    static UserPreference createDislike(Long uId, Long bId) {
       return createPreference(uId, bId, DISLIKE)
    }

    static UserPreference createPreference(Long uId, Long bId, Integer pref) {
        if(!UserPreference.countByUserIdAndBookId(uId, bId)) {
            return new UserPreference(userId: uId, bookId: bId, preference: pref).save()
        } else {
            UserPreference existing = UserPreference.findByUserIdAndBookId(uId, bId)
            existing.preference = pref
            existing.save()
            return existing
        }
    }
}
