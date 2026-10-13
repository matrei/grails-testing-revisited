package demo

import grails.testing.web.taglib.TagLibUnitTest
import spock.lang.Specification

// #region spec
class DemoTagLibSpec extends Specification
        implements TagLibUnitTest<DemoTagLib> {

    void setup() {
        tagLib.greetingService = Stub(GreetingService) {
            greet(_) >> { String name -> "Hello, $name!" }
        }
    }

    void 'renders a greeting'() {
        expect: 'the tag renders the greeting'
            applyTemplate('<demo:greeting name="Glasgow"/>') == 'Hello, Glasgow!'
    }
}
// #endregion spec
