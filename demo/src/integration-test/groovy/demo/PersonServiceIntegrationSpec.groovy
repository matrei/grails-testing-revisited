package demo

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import spock.lang.Specification

// #region spec
@Rollback
@Integration
class PersonServiceIntegrationSpec extends Specification {

    PersonService personService

    void 'the real service and the real GreetingService are wired'() {
        given: 'a person created through the real service'
            def ada = personService.create('Ada', 'Lovelace')

        expect: 'the real GreetingService greets by full name'
            personService.welcome(ada.id) == 'Hello, Ada Lovelace!'
            Person.count() == 1
    }

    void 'the previous test was rolled back'() {
        expect: 'nothing survived the rollback'
            Person.count() == 0
    }
}
// #endregion spec
