package com.zenjob.recommender

import com.opencsv.CSVParser
import com.opencsv.CSVReader
import com.opencsv.CSVReaderBuilder
import grails.gorm.transactions.Transactional
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Component

/**
 * Import's book records from the csv file.
 */

@Component
@CompileStatic
@Transactional
@Slf4j
class BookImporter {
    char separator = ';'

    void 'import'(Reader input) {
        log.info("Importing book records")

        CSVParser parser = new CSVParser(separator)
        //skips first header row
        CSVReader reader = new CSVReaderBuilder(input).withCSVParser(parser).withSkipLines(1).build()
        String[] row

        while ((row = reader.readNext()) != null) {
            insertRecord(row)
        }

        log.info("${reader.recordsRead} Books imported")
    }

    private insertRecord(String[] row) {
        Book book = new Book()
        book.asin = row[0] as Long
        book.title = row[1]
        book.author = row[2]
        book.genre = row[3]
        book.save()
    }
}
