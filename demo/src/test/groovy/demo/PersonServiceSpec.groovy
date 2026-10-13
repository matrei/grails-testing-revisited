package demo

import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

// #region spec
class PersonServiceSpec extends Specification
        implements ServiceUnitTest<PersonService>, DataTest {

    void setupSpec() { mockDomain(Person) }

    void setup() {
        service.greetingService = Mock(GreetingService)
    }

    void 'welcome delegates to the greeting service'() {
        given: 'a person created in the in-memory datastore'
            def person = service.create('Ada', 'Lovelace')

        when: 'the service welcomes them'
            def message = service.welcome(person.id)

        then: 'the mocked GreetingService is used once'
            1 * service.greetingService.greet('Ada Lovelace') >> 'Welcome, Ada!'
            message == 'Welcome, Ada!'
    }
}
// #endregion spec
