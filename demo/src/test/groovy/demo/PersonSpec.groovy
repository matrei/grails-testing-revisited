package demo

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

// #region spec
class PersonSpec extends Specification implements DomainUnitTest<Person> {

    void 'blank names are rejected'() {
        when: 'a person has a blank first name'
            domain.firstName = ''
            domain.lastName = 'Lovelace'

        then: 'validation fails on that property'
            !domain.validate()
            domain.errors['firstName'].code == 'blank'
    }

    void 'GORM uses an in-memory datastore, no database needed'() {
        given: 'two saved people'
            new Person(firstName: 'Ada', lastName: 'Lovelace').save()
            new Person(firstName: 'Grace', lastName: 'Hopper').save()

        expect: 'the in-memory datastore answers queries'
            Person.count() == 2
            Person.findByLastName('Hopper').fullName == 'Grace Hopper'
    }
}
// #endregion spec
