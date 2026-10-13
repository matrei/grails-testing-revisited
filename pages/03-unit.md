---
layout: section
class: section-slide
---

# Unit testing

`implements XxxUnitTest<T>` and you’re done

<!--
⏱ 05:45 — Unit testing, three and a half minutes: the base trait, one table for all the artefact traits, and the controller trait as the worked example.
-->

---
class: code-lh-24
---

# `GrailsUnitTest`: <span class="title-sub">the base of everything</span>

```groovy {all|3-4|6-9|11-13|all}
class MySpec extends Specification implements GrailsUnitTest {

    // a tiny application context, built once per spec by a Spock global extension
    // applicationContext, grailsApplication, config, messageSource

    def beans = {
        // compiled into a nested @Configuration class, e.g.
        // bean('mathHelper', RushHelper)
    }

    Closure doWithConfig() {{ config ->
        // override configuration for this spec only
    }}
}
```

<div class="small muted mt-2">Every other unit trait extends this one. You rarely implement it directly, but its hooks are what you reach for when a test needs a bean or a config value.</div>

<!--
All the unit traits share one base: GrailsUnitTest.

[click] It gives you a real, but tiny, Spring application context: only the core plugin, built once per specification.

[click] The beans block registers beans for this spec. It is new in Grails 8 and replaces doWithSpring, which is now deprecated.

[click] doWithConfig lets you override configuration for this spec only. It’s a method that returns a closure, hence the double braces, which look odd the first time.
-->

---

# One trait per artefact

| Artefact | Trait | You get |
|---|---|---|
| Service | `ServiceUnitTest<T>` | `service`, a real Spring bean |
| Domain class | `DomainUnitTest<T>` | `domain`, validation against the real constraints |
| Controller | `ControllerUnitTest<T>` | `controller`, `view`, `model`, plus request, response, params, flash, session |
| Tag library | `TagLibUnitTest<T>` | `tagLib`, `applyTemplate()` |
| Interceptor | `InterceptorUnitTest<T>` | `interceptor`, `withRequest()` |
| URL mappings | `UrlMappingsUnitTest<T>` | `assertForwardUrlMapping()`, `assertReverseUrlMapping()` |
| Add for GORM | `DataTest` | `mockDomain()`, an in-memory datastore |

<!--
Then there is a specific support trait for each of the different Grails artefact types. Implement it with the class under test as the type parameter, and you get an instance of it as a property, plus helpers for that kind of artefact.

The web traits also share a mock request and response, and you add DataTest when the test needs GORM.

Let’s look at one of them in detail: the controller trait.
-->

---
class: code-lh-15
---

# `ControllerUnitTest<T>`

<TraitProps :own="['controller', 'view', 'model']" web :used="['controller', 'response', 'status']" />

<<< @/demo/src/test/groovy/demo/PersonControllerSpec.groovy#spec groovy {2|4|8-9|12|17-18|21|24-25|all}

<!--
⏱ 07:15

The controller trait gives you the controller, plus the mock request and response every web trait shares.

You can combine it with DataTest when you need to mock domains.

[click] mockDomain in setupSpec: once per spec, not once per test.

[click] Test data goes straight into the in-memory datastore.

[click] Call the action as a plain method, no HTTP, no routing, and for actions that return a model you get the map back directly.

[click] The show action uses respond, so we save a person and tell the response which format we want: JSON.

[click] Then we call the action,

[click] and assert on status, response.text, response.json, view, model, flash, redirectedUrl. All of it comes from the trait, backed by Spring’s mock servlet objects, jakarta flavoured since Grails 7.
-->

---
class: code-lh-16
---

# Also in the box

<div class="grid grid-cols-2 gap-8">
<div>

### `JsonViewUnitTest`
```groovy
class BookViewSpec extends Specification
        implements JsonViewUnitTest {

    void 'renders a book'() {
        when:
            def result = render(view: '/book/_book',
                    model: [book: new Book(title: 'Geb')])

        then:
            result.json.title == 'Geb'
    }
}
```
<span class="small muted">module <code>grails-testing-support-views-gson</code></span>

</div>
<div>

### `HibernateSpec`
```groovy
class BookHibernateSpec extends HibernateSpec {

    List<Class> getDomainClasses() { [Book] }

    void 'real Hibernate, no app booted'() {
        expect:
            Book.count() == 0
    }
}
```
<span class="small muted">module <code>grails-data-hibernate5</code>; H2 in memory, real mapping, real SQL</span>

</div>
</div>

<div class="mt-6 small">
Also: <code>AutowiredTest</code> for plain beans, <code>@Rollback</code> and <code>@Transactional</code> in HibernateSpec specs.
</div>

<!--
JsonViewUnitTest renders JSON views in a unit test. If you use gson views for your APIs, this tests them without booting the app.

HibernateSpec is a base class, not a trait, and it gives you real Hibernate against in-memory H2. That makes it more than a unit test, but it runs with them, in the same JVM and without booting the app. Use it when the query is the thing you’re testing.
-->
