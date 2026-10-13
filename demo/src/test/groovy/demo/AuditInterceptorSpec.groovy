package demo

import grails.testing.web.interceptor.InterceptorUnitTest
import spock.lang.Specification

// #region spec
class AuditInterceptorSpec extends Specification implements InterceptorUnitTest<AuditInterceptor> {

    void 'matches the person controller'() {
        when: 'a request hits the person controller'
            withRequest(controller: 'person', action: 'index')

        then: 'the interceptor matches'
            interceptor.doesMatch()
    }

    void 'does not match other controllers'() {
        when: 'a request hits another controller'
            withRequest(controller: 'book')

        then: 'the interceptor does not match'
            !interceptor.doesMatch()
    }

    void 'before() marks the request as audited'() {
        when: 'a matching request is intercepted'
            withRequest(controller: 'person')

        then: 'before() lets it through and marks it'
            interceptor.before()
            request.audited == true
    }
}
// #endregion spec
