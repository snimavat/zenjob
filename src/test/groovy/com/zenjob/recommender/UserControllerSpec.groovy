package com.zenjob.recommender

import grails.testing.gorm.DataTest
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class UserControllerSpec extends Specification implements ControllerUnitTest<UserController>, DataTest {

    void setup() {
        BookRecommendationService service = Mock()
        controller.bookRecommendationService = service
        mockDomains(UserPreference)
    }

    void testPreferences() {
        setup:
        UserPreference.createLike(1,1)
        UserPreference.createLike(1,2)

        when:
        params.id = "1"
        controller.preferences()

        then:
        response.status == 200
        response.json.size() == 2
    }

    void testRecommendations() {
        when:
        params.id = "1"
        controller.recommendations()

        then:
        1 * controller.bookRecommendationService.recommend(1L) >> [new Book(id:1, title: "test")]
        response.status == 200
        response.json.size() == 1
        response.json[0].title == "test"
    }
}