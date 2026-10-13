package demo

import grails.testing.mixin.integration.Integration
import org.apache.grails.testing.cleanup.core.DatabaseCleanup
import org.apache.grails.testing.http.client.HttpClientSupport
import spock.lang.Specification
import spock.lang.Stepwise

// #region spec
@Stepwise
@Integration
@DatabaseCleanup
class PersonCleanupSpec extends Specification implements HttpClientSupport {

    void 'the running app sees committed data'() {
        given: 'a person, committed in its own transaction'
            Person.withNewTransaction {
                new Person(firstName: 'Ada', lastName: 'Lovelace').save(failOnError: true)
            }

        expect: 'the app lists the person over HTTP'
            http('/people').assertContains(200, 'Lovelace')
    }

    void 'the tables were truncated after the previous test'() {
        expect: 'the app lists nobody'
            http('/people').assertNotContains(200, 'Lovelace')
    }
}
// #endregion spec
