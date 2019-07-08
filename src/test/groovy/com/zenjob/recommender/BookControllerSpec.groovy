package com.zenjob.recommender

import grails.testing.gorm.DataTest
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class BookControllerSpec extends Specification implements ControllerUnitTest<BookController>, DataTest {

    void setup() {
        mockDomains(UserPreference)
        defineBeans({
            bookRecommendationService(BookRecommendationService)
        })
    }

    void testCreatePreference() {
        when:
        request.method = "POST"
        params.id = "1"
        request.json = '{"userId": 1, "preference": 1}'
        request.contentType = JSON_CONTENT_TYPE
        controller.createPreference()

        then:
        response.status == 200
        response.json.userId == 1
        response.json.bookId == 1
        response.json.preference == 1
    }

    void testCreatePreference_failedValidation() {
        when:
        request.method = "POST"
        params.id = "1"
        request.json = '{"userId": 1}'
        request.contentType = JSON_CONTENT_TYPE
        controller.createPreference()

        then:
        response.status == 422
        response.json.message == "Property [preference] of class [class com.zenjob.recommender.web.PreferenceCommand] cannot be null"
    }
}