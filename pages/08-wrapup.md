---
layout: section
class: section-slide
---

# Upgrading and what’s next

What to change, what to watch

<!--
⏱ 30:00 — About a minute for the cheat-sheet, two for test fixtures, one for what’s next, then questions.
-->

---

# Upgrade cheat-sheet

<div class="grid grid-cols-2 gap-8 small">
<div>

### 6 → 7
- Java 17, Groovy 4, `jakarta.*`
- Test JVM args are no longer set for you: configure them on the `Test` task
- Test deps are no longer on the production classpath
- Optional: `GebSpec` → `ContainerGebSpec`, needs a container runtime
- Testcontainers 1.x coordinates still fine in 7.0/7.1
- Adopt: `@DatabaseCleanup` (7.0.8), `HttpClientSupport` (7.1), `testPhases { }` (7.1)

</div>
<div>

### 7 → 8
- Java 21, Groovy 5, Spock **2.4-groovy-5.0**
- GORM properties **nullable by default**: revisit validation specs, or set `grails.gorm.default.nullable: false`
- `@MockBean` → `@MockitoBean`, `@SpyBean` → `@MockitoSpyBean`
- `doWithSpring()` is deprecated: declare a `beans` block instead
- Testcontainers 2.x: `testcontainers-selenium` coordinates
- Delete `getPurgeTagLibMetaClass()` overrides, cleanup is automatic

</div>
</div>

<div class="callout green small mt-2">
Most apps: bump Java, recheck validation specs. The traits and <code>GebSpec</code> keep working.
</div>

<!--
The two upgrade guides, condensed to what affects your tests.

Six to seven is mostly Java and Jakarta. ContainerGebSpec and the new modules are optional; adopt them when it suits you.

Seven to eight is Java 21 and Groovy 5, plus the nullable change, which can turn validation tests green without anyone noticing. The MockBean line only matters if you use Spring’s mock annotations; Spock mocks are unaffected. And doWithSpring still works, but move to a beans block when you touch the spec.
-->

---
class: code-lh-18
---

# Test helpers live in test fixtures

```groovy
// build.gradle: only the fixtures you use, only on test configurations
dependencies {
    testImplementation testFixtures('org.apache.grails:grails-core')
    testImplementation testFixtures('org.apache.grails.web:grails-web-common')
    testImplementation testFixtures('org.apache.grails:grails-views-gson')
    integrationTestImplementation testFixtures('org.apache.grails:grails-geb')
}
```

<v-clicks>

- Test-only classes are moving out of each module’s main jar into its **Gradle test fixtures**, so they stay off your app’s runtime classpath
- Grails 7: `grails-geb` ships `ContainerGebSpec` this way
- Grails 8: `MockGrailsPluginManager`, `MockResourceLoader` and friends, `GrailsWebMockUtil`, `JsonViewTest`, the GraphQL `GraphQLSchemaSpec`
- Your tests still compile against them with `testFixtures(…)`: versions come from the Grails BOM. Never on `implementation`

</v-clicks>

<!--
One more upgrade item, because you will hit it as a compile error. We have been moving test-only classes out of the framework’s main jars.

[click] Each affected module gets a testFixtures source set, from Gradle’s java-test-fixtures plugin, and publishes it as a separate variant next to the main jar. Your production classpath no longer carries mock plugin managers and test base classes.

[click] Grails 7 started it with grails-geb: ContainerGebSpec is a test fixture, which is why the generated build says testFixtures for Geb.

[click] Grails 8 moved more: the mock plugin and resource helpers from grails-core, GrailsWebMockUtil from grails-web-common, the JSON view test helpers, and the GraphQL schema spec. In 7 they were in the main jars; in 8 they are only in the fixtures.

[click] If your tests import one of them, you get "unable to resolve class" until you add the matching testFixtures dependency, on testImplementation or integrationTestImplementation. No version needed, the Grails BOM supplies it. Mind the groups: grails-web-common is org.apache.grails.web. If you use JsonViewUnitTest you don’t need to do anything; its module already pulls in the fixtures it needs.
-->

---

# What’s next

<div class="grid grid-cols-2 gap-8">
<div>

### Spock 2.4 <span class="badge green">in Grails 8</span>
- `@Verify` / `@VerifyAll` helper methods, Mockito mock maker: final classes, `SpyStatic()`
- `filter:` blocks, Cartesian products, `@Snapshot`, `verifyEach()`

</div>
<div>

### Spock 2.5 <span class="badge">6 Oct 2026</span>
- Groovy 6 and Spring 7 support
- `final` local variables in `where:` blocks
- `TooManyInvocationsError` explains argument mismatches
- Works in Grails 8 today: `2.5-groovy-5.0`

</div>
</div>

<h3 class="!mt-8">Apache Geb</h3>

- `geb-playwright` <span class="badge">merged, not released</span>: an experimental Playwright-backed `WebDriver`, so your existing pages, modules and `waitFor` run on Playwright instead of Selenium

<!--
⏱ 32:45

Grails 8 ships Spock 2.4, which has the most interesting Spock features in years: @Verify for helper methods, the Mockito mock maker so you can mock final classes and statics, filter blocks and cartesian products for data tables.

And Spock 2.5 came out a few days ago. It adds Groovy 6 and Spring 7 support, final local variables in where blocks, and a TooManyInvocationsError that tells you why an invocation didn’t match.

It is also built for Groovy 5, so a Grails 8 app can move to 2.5-groovy-5.0 today.

Read the breaking changes first: interactions declared outside then are now also active in cleanup methods.

And from Apache Geb, something to watch: geb-playwright. It lets the Geb code you already have run on Playwright instead of Selenium. It is experimental and not released yet.
-->

---
layout: center
class: text-center
---

# Thank you

<div class="mt-8">
<div class="text-sm muted">Slides & demo app</div>
<div class="mt-1 text-lg">github.com/matrei/grails-testing-revisited</div>
</div>

<p class="text-xl" style="margin-top: 3.5rem">Questions?</p>

<!--
⏱ 34:00 — Leave this slide up during questions.

To close where we started: the traits didn’t change, the runtime under them did. The specs you write look much like the ones from 2017; what’s new is around them: @DatabaseCleanup when the app has to see your data, HttpClientSupport to talk to it over HTTP, and ContainerGebSpec so the browser is the same everywhere. Thank you.

If nobody asks: “How do I run the browser tests on CI?” is a good one to seed: any runner with Docker, RECORD_FAILING, and upload build/gebContainer as an artifact.
-->
