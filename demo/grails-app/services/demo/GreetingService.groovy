package demo

import grails.core.GrailsApplication

class GreetingService {

    GrailsApplication grailsApplication

    String greet(String name) {
        def prefix = grailsApplication.config.getProperty('demo.greeting.prefix', String, 'Hello')
        "$prefix, $name!"
    }
}
