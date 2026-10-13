package demo

import grails.testing.spock.OnceBefore
import org.grails.testing.GrailsUnitTest
import org.junit.jupiter.api.BeforeEach
import spock.lang.Specification

// #region spec
class FixtureOrderSpec extends Specification implements GrailsUnitTest {

    static List<String> log = []
    static int onceCount = 0

    @OnceBefore
    void onlyOncePerSpec() { onceCount++ }

    @BeforeEach
    void junitBeforeEach() { log << 'junit' }

    void setup() { log << 'spock' }

    void 'JUnit fixtures run before Spock fixtures'() {
        expect: 'the JUnit fixture ran before setup()'
            log.takeRight(2) == ['junit', 'spock']
            onceCount == 1
    }

    void 'and @OnceBefore really ran only once'() {
        expect: 'the order held and @OnceBefore ran only once'
            log.takeRight(2) == ['junit', 'spock']
            onceCount == 1
    }
}
// #endregion spec
