package demo

import demo.pages.HomePage
import demo.pages.PeoplePage
import grails.plugin.geb.ContainerGebSpec
import grails.testing.mixin.integration.Integration

// #region spec
@Integration
class HomeSpec extends ContainerGebSpec {

    void 'the home page renders in a containerized browser'() {
        when: 'visiting the home page'
            def page = to(HomePage)

        then: 'the heading is there'
            page.heading.contains('Welcome')
    }

    void '/people lists people created directly through GORM'() {
        given: 'the app runs in the same JVM, so GORM is right here'
            Person.withNewTransaction {
                new Person(firstName: 'Ada', lastName: 'Lovelace')
                        .save(flush: true)
            }

        when: 'visiting the people page'
            def page = to(PeoplePage)

        then: 'the person created through GORM is listed'
            page.people == ['Ada Lovelace']
    }
}
// #endregion spec
