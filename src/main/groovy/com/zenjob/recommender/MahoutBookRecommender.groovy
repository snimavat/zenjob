/**
package com.zenjob.recommender

import grails.compiler.GrailsCompileStatic

import groovy.util.logging.Slf4j
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity
import org.apache.mahout.cf.taste.model.DataModel
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood
import org.apache.mahout.cf.taste.recommender.RecommendedItem
import org.apache.mahout.cf.taste.recommender.Recommender
import org.apache.mahout.cf.taste.similarity.UserSimilarity
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

@GrailsCompileStatic
@Slf4j
class MahoutBookRecommender implements BookRecommender {

    Recommender recommender

    @PostConstruct
    void init() {
        DataModel model = new FileDataModel(new File("data/preferences.txt"));
        UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
        UserNeighborhood neighborhood = new NearestNUserNeighborhood(10, similarity, model);
        recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
    }

    @Override
    Collection<Long> recommend(Long userId, Integer numRecommendations = 20) {
        List<RecommendedItem> recommendations = recommender.recommend(userId, numRecommendations)
        return recommendations.collect({it.itemID})
    }
}
**/