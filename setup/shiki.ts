import { defineShikiSetup } from '@slidev/types'

// The Groovy grammar colours assertContains as an old GroovyTestCase helper
// (support.function.testing.groovy), so it would stand out from the other
// HttpClientSupport assertions. Drop the token's own colour so it inherits
// the plain code colour like every other method call.
export default defineShikiSetup(() => ({
    transformers: [{
        name: 'plain-assert-contains',
        span(node, _line, _col, _lineElement, token) {
            if (token.content === 'assertContains') {
                delete node.properties.style
            }
        },
    }],
}))
