package demo

import grails.testing.mixin.integration.Integration
import org.apache.grails.testing.http.client.HttpClientSupport
import spock.lang.Specification

// #region spec
// #region health
@Integration
class BookApiSpec extends Specification implements HttpClientSupport {

    void 'the app reports that it is up'() {
        when: 'the health endpoint is called'
            def response = http('/actuator/health', Accept: 'application/json')

        then: 'status, content type and body are checked one by one'
            response.assertStatus(200)
                    .assertHeaders('Content-Type': 'application/json')
                    .assertContains('UP')

        and: 'it can all be checked in one call'
            response.assertContains(200, 'Content-Type': 'application/json', 'UP')
    }
    // #endregion health

    // #region post
    void 'POST creates a book'() {
        when: 'a book is posted as JSON'
            def response = httpPostJson(
                    '/api/books',
                    [title: 'Groovy in Action', author: 'Paul King']
            )

        then: 'it is created'
            response.assertJsonContains(
                    201,
                    [title: 'Groovy in Action']
            )
    }
    // #endregion post

    // #region xml
    void 'POST and verify XML'() {
        when: 'a book is posted as XML, built with a MarkupBuilder closure'
            def response = httpPostXml('/api/books', Accept: 'application/xml') {
                book {
                    title('Grails in Action')
                    author('Glen Smith')
                }
            }

        then: 'the XML response is checked through GPath'
            response.assertStatus(201)
            with(response.xml()) {
                title.text() == 'Grails in Action'
                author.text() == 'Glen Smith'
            }
    }
    // #endregion xml
}
// #endregion spec
