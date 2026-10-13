package demo

import grails.testing.web.UrlMappingsUnitTest
import spock.lang.Specification

// #region spec
class UrlMappingsSpec extends Specification implements UrlMappingsUnitTest<UrlMappings> {

    void setup() { mockController(PersonController) }

    void '/people maps to PersonController.index'() {
        expect: '/people forwards to index'
            assertForwardUrlMapping('/people', controller: 'person', action: 'index')
    }

    void '/hello/Ada passes the name parameter'() {
        expect: 'the name is captured as a parameter'
            assertForwardUrlMapping('/hello/Ada', controller: 'person', action: 'hello') {
                name = 'Ada'
            }
    }

    void 'reverse mapping generates /people'() {
        expect: 'the reverse mapping generates /people'
            assertReverseUrlMapping('/people', controller: 'person', action: 'index')
    }
}
// #endregion spec
