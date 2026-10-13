package demo.pages

import geb.Page

class PeoplePage extends Page {

    static url = '/people'
    static at = { title == 'People' }

    static content = {
        people { $('ul#people li')*.text() }
    }
}
