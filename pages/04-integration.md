---
layout: section
class: section-slide
---

# Integration testing

The real app, the real database, one JVM

<!--
⏱ 09:15 — Integration testing, about ten and a half minutes.
-->

---
class: code-lh-21
---

# `@Integration`: <span class="title-sub">what you write, what compiles</span>

````md magic-move {lines: false}
```groovy
@Integration
class PersonServiceIntegrationSpec extends Specification {

    PersonService personService

    void 'the real service is wired'() {
        expect:
            personService.welcome(1L) == 'Hello, Ada Lovelace!'
    }
}
```

```groovy
@SpringBootTest(webEnvironment = RANDOM_PORT, useMainMethod = ALWAYS)
@ContextConfiguration(loader = GrailsApplicationContextLoader, classes = Application)
class PersonServiceIntegrationSpec extends Specification {

    PersonService personService

    void 'the real service is wired'() {
        expect:
            personService.welcome(1L) == 'Hello, Ada Lovelace!'
    }
}
```

```groovy
@SpringBootTest(webEnvironment = RANDOM_PORT, useMainMethod = ALWAYS)
@ContextConfiguration(loader = GrailsApplicationContextLoader, classes = Application)
class PersonServiceIntegrationSpec extends Specification implements ApplicationContextAware {

    @Value('${local.server.port}')
    Integer serverPort

    PersonService personService   // autowired by name from the real context

    void 'the real service is wired'() {
        expect:
            personService.welcome(1L) == 'Hello, Ada Lovelace!'
    }
}
```
````

<!--
Of all the annotations in Grails’ testing support, @Integration is the only one that rewrites your class at compile time. This is what you write.

[click] At compile time the annotation becomes a Spring Boot test: a random port, the Grails application context loader and your Application class.

[click] And your spec gets two more things: a serverPort property, injected from the running server, and ApplicationContextAware, which is how your properties are autowired by name from the real context.
-->

---
class: code-lh-20
---

# `@Rollback`: <span class="title-sub">the classic</span>

<<< @/demo/src/integration-test/groovy/demo/PersonServiceIntegrationSpec.groovy#spec groovy {2|1|5|9-13|16-19|all}

<!--
@Integration on the class.

[click] Putting the @Rollback annotation on the class wraps every feature method in a transaction that is rolled back at the end.

[click] Autowiring by property name, like in a controller.

[click] So this test creates a person and sees it,

[click] and the next test sees a clean table. Nothing was ever committed.

[click] The limitation is obvious the day you hit it: anything that runs outside your transaction doesn’t see your data. A controller you call over HTTP. A background job. That is what the next slide is for.
-->

---
class: code-lh-18
---

# `@DatabaseCleanup` <span class="badge">new in 7.0.8</span>

<<< @/demo/src/integration-test/groovy/demo/PersonCleanupSpec.groovy#spec groovy {3|7-10|12-13|16-18}

<!--
⏱ 11:15

Database cleanup is a Spock extension that truncates your tables after each test, instead of rolling back.

One annotation on the class, or on individual methods.

[click] The test commits for real, in its own transaction, so the row is in the database before anything else asks for it.

[click] And the running app, in a transaction of its own, lists the person over HTTP. Under @Rollback this is exactly what fails: the row would only exist inside the test’s transaction, and the app would never see it.

[click] After the test, every table was truncated, so the next test starts clean and the app lists nobody. With @Rollback and the same commit, it is this second test that fails instead: a rollback can’t undo a commit.
-->

---
class: code-lh-20-5
---

# `@DatabaseCleanup`: <span class="title-sub">the details</span>

<div class="grid grid-cols-2 gap-10">
<div>

```groovy
// only after this feature method
@DatabaseCleanup
void 'imports the catalogue'() { … }

// name data sources, and their type
@DatabaseCleanup(['dataSource:h2',
                  'other:postgresql'])

// once, after the whole spec
@DatabaseCleanup(cleanupAfterSpec = true)
class ImportSpec extends Specification { … }
```

</div>
<div>

<v-clicks>

- **Requires `@Integration`**, it needs the application context
- **Truncates `BootStrap` data too**: create what your test needs
- **One module per database**: PostgreSQL uses `TRUNCATE … RESTART IDENTITY CASCADE`; H2 switches referential integrity off and keeps its sequences; a `DatabaseCleaner` SPI for others
- Set the `grails.testing.cleanup.debug` system property to log each cleanup: every table and its row count

</v-clicks>

</div>
</div>

<!--
Besides on the class, you can put it on a single feature method, name the data sources it should clean, or clean only once, after the whole spec.

[click] It needs @Integration, because it finds the data sources in the application context.

[click] It truncates everything, BootStrap data included, so create the data each test needs.

[click] There is one module per database, because each truncates differently. PostgreSQL resets the ids; H2 keeps counting, so don’t assert on ids across tests. For other databases there is an SPI, a Service Provider Interface, so you can plug in your own cleaner.

[click] And if you want to see what it does, a system property makes it log every table it truncated and how many rows it had.
-->

---
class: code-lh-24
clicks: 2
---

# `HttpClientSupport` <span class="badge">new in 7.1</span>

<div class="api-steps">
  <span :class="{ on: $clicks === 0 }">1 · GET</span>
  <span :class="{ on: $clicks === 1 }">2 · POST</span>
  <span :class="{ on: $clicks === 2 }">3 · XML</span>
</div>

<div class="api-step" :class="{ show: $clicks === 0 }">

<<< @/demo/src/integration-test/groovy/demo/BookApiSpec.groovy#health groovy

</div>
<div class="api-step" :class="{ show: $clicks === 1 }">

<<< @/demo/src/integration-test/groovy/demo/BookApiSpec.groovy#post groovy

</div>
<div class="api-step" :class="{ show: $clicks === 2 }">

<<< @/demo/src/integration-test/groovy/demo/BookApiSpec.groovy#xml groovy

</div>

<style>
.api-steps { position: absolute; top: 3.1rem; right: 3.5rem; display: flex; gap: 0.4rem; font-size: 0.8rem; }
.api-steps span { padding: 0.25em 0.8em; border-radius: 999px; border: 1px solid rgba(255,255,255,0.18); opacity: 0.45; white-space: nowrap; }
.api-steps span.on { opacity: 1; border-color: var(--grails-orange); background: var(--grails-orange); color: #101418; font-weight: 700; }
.api-step { display: none; }
.api-step.show { display: block; }
</style>

<!--
The HTTP client module is for integration tests that talk to the running app over HTTP, without starting a browser.

It uses the JDK’s own HTTP client, so it brings in no extra HTTP library, and it knows the server port from @Integration.

Implement the trait and send a GET request with the http() method: a path, and headers as named arguments. Each assertion on the response returns it again, so you can chain them: the status, then the Content-Type header, then that the body contains UP.

Or check all three in one call: assertContains also takes the status and the headers.

[click] httpPostJson turns a map into JSON and posts it. assertJsonContains checks the status and part of the JSON in one call.

[click] XML works the same way: httpPostXml builds the body with a MarkupBuilder closure, and response.xml() lets you assert on the result.
-->

---
class: code-lh-14
---

# `HttpClientSupport`: <span class="title-sub">more of the API</span>

<div class="grid grid-cols-2 gap-10">
<div>

```groovy
// every verb, plain / JSON / XML bodies
http('/books')                          // GET
httpPostJson('/books', [title: 'Geb'])
httpPutXml('/books/1') { book { title('Geb') } }
httpPatchJson(…) · httpDelete(…) · httpHead(…)

// form fields, new in 8.0
httpPostForm('/hello', [name: 'Ada'])

// multipart
httpPostMultipart('/upload', MultipartBody.builder()
        .addPart('name', 'cover')
        .addPart('file', 'cover.png', 'image/png', bytes)
        .build())

// a custom client, built from the defaults
def client = newHttpClientWith { followRedirects(NEVER) }
http('/old-page', client).assertStatus(302)

// keep cookies, e.g. a login session
def withCookies = newHttpClientWith {
    cookieHandler(new CookieManager())
}
```

</div>
<div>

```groovy
response
    .assertStatus(200)
    .assertHeadersIgnoreCase('content-type': 'application/json')
    .assertJson([id: 1, title: 'Geb'])          // whole tree
    .assertJsonContains([title: 'Geb'])         // subset
    .assertContains('Geb')
    .assertNotContains('error')

response.json()      // Map
response.jsonList()  // List
response.xml()       // GPathResult
```

<v-clicks>

- Relative paths go to the running app: `localhost:<serverPort>` plus context path
- One shared JDK client: redirects followed, 60 s timeouts, `grails.http.client.timeout` to change

</v-clicks>

</div>
</div>

<!--
The module API is broader than there is time to show in code. Every HTTP verb, each with plain, JSON and XML body variants, and multipart support through a small builder. And new in Grails 8, httpPostForm sends form fields the way an HTML form does.

Every method also takes an optional custom configured HttpClient, for the cases where you want different redirect or timeout behaviour.

A newHttpClientWith method builds a client from the module’s defaults, and you change only what you need, for example: don’t follow redirects, so you can assert on the 302 itself.

Cookies are another reason: the shared default client keeps no session between requests, so for a login flow you build one with a CookieManager.

On the response side, assertJson compares the whole tree, key order doesn’t matter, and assertJsonContains checks a subset. When an assertion isn’t enough, json, jsonList and xml give you the parsed body.

[click] You never write a host or port: relative paths are resolved against the running app’s random port, including any context-path.

[click] By default there is one shared client for the whole run, for connection reuse. It follows redirects and times out after 60 seconds; the grails.http.client.timeout system property changes the request timeout.
-->

---
class: code-lh-16
---

# MongoDB and Redis in a container <span class="badge">7.0</span>

```groovy
testImplementation 'org.apache.grails.testing:grails-testing-support-mongodb'
```

<div class="grid grid-cols-2 gap-10">
<div>

```groovy
// ./gradlew test, no app: your spec gets host and port
class BookSpec extends AutoStartedMongoSpec {

    @Shared
    @AutoCleanup
    MongoDatastore datastore

    void setupSpec() {
        datastore = new MongoDatastore(Book,
                'grails.mongodb.host': mongoHost,
                'grails.mongodb.port': mongoPort)
    }

    void 'GORM writes a real MongoDB document'() {
        when: 'a book is saved through GORM'
            new Book(title: 'Geb').save(flush: true)

        then: 'the MongoDB driver finds it'
            Book.collection.find().first().title == 'Geb'
    }
}
```

</div>
<div class="text-[0.92em]">

<v-clicks>

- **In `integrationTest`:** an ordinary `@Integration` spec. The module on the classpath is enough: one container per run, handed to the app through `grails.mongodb.host` / `port`. Already running on `27017`? It uses that instead
- Pin the image with the `mongodbContainerVersion` system property
- **`EmbeddedReplicaSetSpec`** <span class="badge">8.0</span>: a real `mongod` **without Docker**, as a one-node replica set, so transactions work
- **Redis** <span class="badge">8.0.1</span>: `grails-testing-support-redis`, same group and idea, sets `REDIS_HOST` / `REDIS_PORT`; expected on Maven Central with 8.0.1

</v-clicks>

</div>
</div>

<!--
If your app uses MongoDB, you can test against a real MongoDB instead of the in-memory datastore. The testing module starts MongoDB in a Testcontainers container. It has been there since 7.0.

In the test phase you extend AutoStartedMongoSpec, and your spec gets the container’s host and port. Strictly this isn’t a unit test: it talks to a real database. It can run with the unit tests because it doesn’t need the app.

You build a datastore in setupSpec, and then it is real GORM against real MongoDB: the save goes to the server, and the native driver reads the document back.

[click] If you run it as an @Integration spec, the module on the classpath is enough: a global Spock extension starts one container for the run and hands its host and port to the app.

That is the real difference: in the test phase your spec talks to MongoDB, in integrationTest the app does.

If something already listens on 27017, like a CI service container, it uses that instead.

[click] Pin the image version with a system property, for example on CI.

[click] New in Grails 8: EmbeddedReplicaSetSpec, for when there is no Docker. It downloads a real mongod and runs it as a one-node replica set, so transactions work, just like in the container. You add grails-data-mongodb-embedded and the flapdoodle library to the test classpath yourself.

[click] And the same idea for Redis: a global extension starts a Redis container for integration tests and exposes host and port. It should reach Maven Central with 8.0.1.
-->
