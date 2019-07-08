package com.zenjob.recommender

import groovy.transform.CompileStatic

@CompileStatic
interface BookRecommender {

    /**
     * Returns collections of recommended books for given user.
     *
     * @param userId
     * @param numRecommendations
     * @return Collection of recommended book ids
     */
    Collection<Long> recommend(Long userId, Integer numRecommendations);
}
