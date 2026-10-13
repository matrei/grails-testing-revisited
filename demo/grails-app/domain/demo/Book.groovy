package demo

import grails.rest.Resource

@Resource(uri = '/api/books', formats = ['json', 'xml'])
class Book {

    String title
    String author

    static constraints = {
        title(blank: false)
        author(blank: false)
    }
}
