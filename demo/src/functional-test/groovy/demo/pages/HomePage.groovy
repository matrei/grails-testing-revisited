package demo.pages

import geb.Page

// #region page
class HomePage extends Page {

    static url = '/'
    static at = {
        title == 'Welcome to Grails'
    }

    static content = {
        heading { $('h1').text() }
    }
}
// #endregion page
