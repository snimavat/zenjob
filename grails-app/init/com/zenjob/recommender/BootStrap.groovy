package com.zenjob.recommender

import grails.util.Environment
import groovy.util.logging.Slf4j

@Slf4j
class BootStrap {
    BookImporter bookImporter

    def init = { servletContext ->
        if(Environment.current == Environment.DEVELOPMENT) {
            bookImporter.import(new FileReader("data/books.txt"))
        }
    }

}
