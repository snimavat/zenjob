package com.zenjob.recommender

import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
class Book {
    Long asin
    String title
    String author
    String genre

    Date dateCreated
    Date lastUpdated

    static constraints = {
        asin nullable: false
        title nullable: false, blank: false
        author nullable: false, blank: false
        genre nullable: false, blank: false
        dateCreated nullable: true
        lastUpdated nullable: true
    }
}
