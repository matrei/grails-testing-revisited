package demo

import grails.core.GrailsApplication
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

// #region spec
class PersonServiceBeansSpec extends Specification
        implements ServiceUnitTest<PersonService>, DataTest {

    def beans = {
        bean(GreetingService) { GrailsApplication grailsApplication ->
            new GreetingService(grailsApplication: grailsApplication)
        }
    }

    Closure doWithConfig() {{ config ->
        config['demo.greeting.prefix'] = 'Hi'
    }}

    void setupSpec() { mockDomain(Person) }

    void 'welcome uses the real greeting service from the beans block'() {
        given: 'a person created in the in-memory datastore'
            def person = service.create('Ada', 'Lovelace')

        when: 'the service welcomes them'
            def message = service.welcome(person.id)

        then: 'the greeting service bean was injected by name'
            message == 'Hi, Ada Lovelace!'
    }
}
// #endregion spec
