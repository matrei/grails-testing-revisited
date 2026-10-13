---
layout: two-cols
layoutClass: gap-10
---

# whoami

- **Mattias Reichel**, Sweden
- Apache Grails committer & PMC member
- Grailsy things I’ve touched lately
  - <span class="when">Oct 2024</span>Containerized Geb
  - <span class="when">2025–26</span>Grails Spring Security Plugins
  - <span class="when">Feb 2026</span>Grails Website plugin page
  - <span class="when">Mar 2026</span>HttpClient Testing Support
  - <span class="when">Sep 2026</span>Grails Website search
  - <span class="when">Now</span>First conference talk

::right::

<div class="mt-16 text-center">
  <img src="/grails_logo.svg" class="w-40 mx-auto opacity-90" alt="Grails" />
  <p class="mt-6 text-sm opacity-70">github.com/matrei<br>x.com/mattias_reichel<br>grails.apache.org</p>
</div>

<style>
li:has(> .when) { padding-left: 4.4em; text-indent: -4.4em; }
.when { display: inline-block; width: 6.1em; white-space: nowrap; text-indent: 0; font-size: 0.72em; opacity: 0.55; font-variant-numeric: tabular-nums; }
</style>

<!--
⏱ 00:45

A few words about me. I’m a committer and PMC member on Apache Grails.

My day job is at a company that is primarily a printing business. We use Grails for some of our in-house systems and for solutions tailored to our customers.

Most of my contributions in the last two years have been in the build and the testing corner of the framework.

That is also why this talk exists: I spent a lot of time inside the testing support and thought it was worth a fresh walkthrough.
-->

---
clicks: 2
---

# Three generations, one test

<div class="gen-steps">
  <span :class="{ on: $clicks === 0 }">1 · Base classes</span>
  <span :class="{ on: $clicks === 1 }">2 · Mixins</span>
  <span :class="{ on: $clicks === 2 }">3 · Traits</span>
</div>

<div class="gen-grid">
<div class="gen-card" :class="{ show: $clicks === 0 }">

### 2009 · Grails 1.1

```groovy
class BookControllerTests extends ControllerUnitTestCase {

    void testIndexAction() {
        mockDomain(Book, [
            new Book(title: 'Geb')
        ])

        def model = controller.index()

        assert model.books.size() == 1
    }
}
```

JUnit 3 via `GroovyTestCase` · `MockUtils` · Spock is a plugin

</div>
<div class="gen-card" :class="{ show: $clicks === 1 }">

### 2011 · Grails 2.0 – 3.2

```groovy
@Mock(Book)
@TestFor(BookController)
class BookControllerSpec extends Specification {

    void 'the index action lists books'() {
        given: 'a saved book'
            new Book(title: 'Geb').save()

        when: 'the index action is called'
            def model = controller.index()

        then: 'the book is in the model'
            model.books.size() == 1
    }
}
```

AST transforms weave the mixins in · JUnit 4 or Spock

</div>
<div class="gen-card" :class="{ show: $clicks === 2 }">

### 2017 · Grails 3.3 – 8

```groovy
class BookControllerSpec extends Specification
        implements DataTest, ControllerUnitTest<BookController> {

    void setupSpec() { mockDomain(Book) }

    void 'the index action lists books'() {
        given: 'a saved book'
            new Book(title: 'Geb').save()

        when: 'the index action is called'
            def model = controller.index()

        then: 'the book is in the model'
            model.books.size() == 1
    }
}
```

Plain traits · Spock extensions, no AST · what you write today

</div>
</div>

<style>
.gen-steps { position: absolute; top: 3.1rem; right: 3.5rem; display: flex; gap: 0.4rem; font-size: 0.8rem; }
.gen-steps span { padding: 0.25em 0.8em; border-radius: 999px; border: 1px solid rgba(255,255,255,0.18); opacity: 0.45; white-space: nowrap; }
.gen-steps span.on { opacity: 1; border-color: var(--grails-orange); background: var(--grails-orange); color: #101418; font-weight: 700; }
.gen-card { display: none; flex-wrap: wrap; align-items: baseline; column-gap: 1.2em; }
.gen-card.show { display: flex; }
.gen-card h3 { font-size: 1.2em; font-weight: 700; color: var(--grails-orange); order: 0; margin: 0; white-space: nowrap; }
.gen-card p { font-size: 0.8em; opacity: 0.8; order: 1; margin: 0; line-height: 1.35; }
.gen-card .slidev-code-wrapper { order: 2; flex-basis: 100%; margin-top: 0.5em !important; }
.gen-grid > div:nth-child(1) { --slidev-code-font-size: 20.5px; --slidev-code-line-height: 29px; }
.gen-grid > div:nth-child(2) { --slidev-code-font-size: 17px; --slidev-code-line-height: 24px; }
.gen-grid > div:nth-child(3) { --slidev-code-font-size: 15.5px; --slidev-code-line-height: 22px; }
</style>

<!--
⏱ 01:45

Grails testing support has gone through three generations. Here I’ll go through the same test in each generation.

What we test is a controller whose index action returns a model with the list of books. The test puts one book into an in-memory datastore, calls the action as a plain method, and checks that the model holds that one book. Only the scaffolding around it changes between the generations: how the test gets a controller, and how it mocks the domain class.

Grails 1.1, 2009: the unit-test base classes arrived. You extended GrailsUnitTestCase or ControllerUnitTestCase, both JUnit 3 underneath.

[click] Grails 2.0, 2011, replaced inheritance with annotations. @TestFor and @Mock, and an AST transformation mixed the right behaviour into your class at compile time.

This also made it runner-agnostic: JUnit 4 or Spock, which became part of the core distribution in 2.3.

[click] And here is the same test with the current generation: traits instead of annotations, Spock extensions instead of AST transforms.

Across the three: base classes lasted three years, mixins six, and the traits are now nine years old, from 3.3 through 8. The third design is the one that stuck.
-->

---
layout: center
class: text-center
---

# History of the current generation

<div class="grid grid-cols-6 gap-2 mt-4 text-left era-grid">
<div class="era">

### 2017 · Grails 3.3

<div class="versions"><span>Spock</span><b>1.1</b><span>Geb</span><b>1.1</b><span>Groovy</span><b>2.4</b><span>Gradle</span><b>3.5</b></div>

- Trait-based testing support replaces the mixin framework
- ControllerUnitTest, DomainUnitTest, DataTest & friends

</div>
<div class="era">

### 2019 · Grails 4

<div class="versions"><span>Spock</span><b>1.2</b><span>Geb</span><b>2.3</b><span>Groovy</span><b>2.5</b><span>Gradle</span><b>5.1</b></div>

</div>
<div class="era">

### 2021 · Grails 5

<div class="versions"><span>Spock</span><b>2.0</b><span>Geb</span><b>2.3</b><span>Groovy</span><b>3.0</b><span>Gradle</span><b>7.2</b></div>

</div>
<div class="era">

### 2023 · Grails 6

<div class="versions"><span>Spock</span><b>2.1</b><span>Geb</span><b>5.1</b><span>Groovy</span><b>3.0</b><span>Gradle</span><b>7.6</b></div>

</div>
<div class="era">

### 2025 · Grails 7

<div class="versions"><span>Spock</span><b>2.3</b><span>Geb</span><b>8.0</b><span>Groovy</span><b>4.0</b><span>Gradle</span><b>8.14</b></div>

- Geb in containers
- @DatabaseCleanup
- HttpClientSupport
- MongoDB containers

</div>
<div class="era">

### 2026 · Grails 8

<div class="versions"><span>Spock</span><b>2.4</b><span>Geb</span><b>8.0</b><span>Groovy</span><b>5.1</b><span>Gradle</span><b>9.8</b></div>

- New modules: latency, embedded MongoDB
- Redis containers (8.0.1)

</div>
</div>

<div v-click class="callout mt-5 text-left">
<b>The traits didn’t change. The runtime under them did.</b><br><span class="third">And a few new pieces arrived that are easy to miss.</span>
</div>

<div class="tiny muted mt-2">Versions read from a generated app’s resolved test classpath and Gradle wrapper, GA release of each line: 3.3.0, 4.0.0, 5.0.0, 6.0.0, 7.0.0 and 8.0.0.</div>

<style>
.era { position: relative; border: 1px solid var(--grails-orange); border-radius: 10px; padding: 0.55em 0.55em; background: rgba(254,181,113,0.05); }
.era-badge { position: absolute; top: -0.75em; right: 0.8em; margin: 0; }
.era h3 { font-size: 0.74em; font-weight: 700; color: var(--grails-orange); margin: 0 0 0.5em; white-space: nowrap; }
.versions { display: grid; grid-template-columns: auto 1fr; column-gap: 0.6em; row-gap: 0.15em; font-size: 0.68em; padding-bottom: 0.55em; margin-bottom: 0.55em; border-bottom: 1px dashed rgba(255,255,255,0.18); }
.versions span { opacity: 0.65; }
.third { white-space: nowrap; }
.versions:last-child { border-bottom: none; padding-bottom: 0; margin-bottom: 0; }
.versions b { font-family: var(--slidev-code-font-family); font-weight: 600; }
.era ul { font-size: 0.6em; margin: 0; padding-left: 0; list-style: none; }
.era li { position: relative; margin-left: 0; padding-left: 0.95em; line-height: 1.4; }
.era li::before { content: ''; position: absolute; left: 0.1em; top: 0.53em; width: 0.34em; height: 0.34em; border-radius: 1px; background: var(--grails-orange); opacity: 0.85; }
.era li + li { margin-top: 0.3em; }
.era code { font-size: 0.8em; padding: 0; background: transparent; }
</style>

<!--
⏱ 03:30

Here we see what happened to Grails testing support over the last nine years, from Grails 3.3 in 2017 to Grails 8 now, with the Spock, Geb, Groovy and Gradle versions each release came with.

The traits arrived in 3.3. Grails 5 moved the tests to the JUnit Platform. Grails 7 put browser tests in a container and added new testing modules, and Grails 8 adds a few more.

[click] Every version on these cards changed, yet a test written for 3.3 still looks the same today.
-->
