package demo

import grails.testing.gorm.DataTest
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

// #region spec
class PersonControllerSpec extends Specification
        implements ControllerUnitTest<PersonController>, DataTest {

    void setupSpec() { mockDomain(Person) }

    void 'index lists people sorted by last name'() {
        given: 'two people'
            new Person(firstName: 'Ada', lastName: 'Lovelace').save()
            new Person(firstName: 'Grace', lastName: 'Hopper').save()

        expect: 'index sorts them by last name'
            controller.index().people*.lastName == ['Hopper', 'Lovelace']
    }

    void 'show responds with JSON'() {
        given: 'a person and a JSON request'
            def ada = new Person(firstName: 'Ada', lastName: 'Lovelace').save()
            response.format = 'json'

        when: 'the show action is called'
            controller.show(ada.id)

        then: 'the person is rendered as JSON'
            status == 200
            response.json.firstName == 'Ada'
    }
}
// #endregion spec
