package demo

import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

// #region spec
class GreetingServiceSpec extends Specification
        implements ServiceUnitTest<GreetingService> {

    Closure doWithConfig() {{ config ->
        config['demo.greeting.prefix'] = 'Hi'
    }}

    void 'greets using the configured prefix'() {
        expect: 'the prefix from doWithConfig is used'
            service.greet('Glasgow') == 'Hi, Glasgow!'
    }
}
// #endregion spec
