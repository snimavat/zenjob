package com.zenjob.recommender

import grails.testing.web.UrlMappingsUnitTest
import spock.lang.Specification
import zenjob.UrlMappings

class UrlMappingsSpec extends Specification implements UrlMappingsUnitTest<UrlMappings> {

    void setup() {
        mockController(UserController)
        mockController(BookController)
    }

    void testUrlMappings() {

        expect:
        verifyUrlMapping("/users/1/preferences", controller: 'user', action: 'preferences', method: 'GET') {
            id = "1"
        }

        verifyUrlMapping("/users/1/recommendations", controller: 'user', action: 'recommendations', method: 'GET') {
            id = "1"
        }

        when:
        //GET
        !verifyUrlMapping("/books/1/preferences", controller: 'book', action: 'createPreference', method: 'POST') {
            id = "1"
        }

        //POST
        request.method = "POST"
        verifyUrlMapping("/books/1/preferences", controller: 'book', action: 'createPreference') {
            id = "1"
        }

        then:
        noExceptionThrown()
    }
}
