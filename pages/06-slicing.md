---
layout: section
class: section-slide
---

# Slicing the test suite

Phases split tests by kind, tags split them within a phase

<!--
⏱ 27:00 — Slicing, three minutes. Both features are about choosing what runs, not about how you write a test.
-->
---

# `testPhases { }`: <span class="title-sub">one line, a whole phase</span> <span class="badge">7.1</span>

<div class="grid grid-cols-2 gap-10 phase-grid">
<div>

```groovy
// build.gradle
testPhases {
    functionalTest { }
}

dependencies {
    functionalTestImplementation testFixtures('…:grails-geb')
}
```

Grails ships `test` and `integrationTest`, nothing else. Since 7.1 `integrationTest` is itself built on `testPhases`, so everything here applies to it too.

</div>
<div>

That one line gives you:

<v-clicks>

- a source set in `src/functional-test/groovy`
- `functionalTestImplementation` and `functionalTestRuntimeOnly`, extending the test configurations
- a `functionalTest` task, wired into `check`
- a system property, `is.grails.functional.test=true`
- IntelliJ marking the folder as test sources
- **your `src/test` classes on the classpath**, so shared base specs and helpers just work

</v-clicks>

</div>
</div>

<style>
.phase-grid li { font-size: 0.92em; }
</style>

<!--
testPhases is a Gradle extension from the Grails Gradle plugin. Grails itself only ships two phases, test and integrationTest.

[click] The phase name becomes the source folder: functionalTest reads from src/functional-test.

[click] It gets its own implementation and runtimeOnly configurations, extending the test ones, so Spock is already there and you only add what this phase needs, here grails-geb.

[click] A Test task with the same name, hooked into check.

[click] A system property your code can read to know which phase is running. More on that in a second.

[click] The IDE picks up the folder as test sources without any extra config.

[click] And the phase classpath includes the output of src/test. A base spec or a test data builder you wrote for unit tests is available to your browser specs as well, no copying.
-->

---
class: code-lh-17
---

# Customize, detect and run a phase

<div class="grid grid-cols-2 gap-10">
<div>

```groovy
// build.gradle
testPhases {
    smokeTest {
        sourceFolderName = 'src/smoke-tests'
        systemPropertyName = 'is.smoke.test'
        ideaIntegration = false
    }
}
```

| Property | Default |
|---|---|
| `sourceFolderName` | `src/smoke-test` |
| `systemPropertyName` | `is.grails.smoke.test` |
| `ideaIntegration` | `true` |

</div>
<div>

```groovy
// shared test or app code
if (Boolean.getBoolean('is.grails.functional.test')) {
    // e.g. another datasource, shorter timeouts,
    // an interceptor switched off
}
```

```bash
./gradlew smokeTest
./gradlew functionalTest --tests 'demo.HomeSpec'
./gradlew check            # every phase
```

<div class="small muted">One merged HTML report for all phases: <code>build/reports/tests/index.html</code></div>

</div>
</div>

<!--
Three knobs per phase, all optional. Where the sources live, which system property is set, and whether IntelliJ should be told about the folder. The defaults are generated from the phase name.

Running a phase is just its task name. The check task runs every phase, unit tests included, and the plugin merges all their results into one HTML report under build/reports/tests; the per-phase HTML reports are switched off.
-->


---
class: code-lh-21
---

# Test slicing with JUnit Platform tags

```groovy
import spock.lang.Tag   // Spock’s mapping of JUnit Platform tags

// ContainerGebSpec already carries @Tag('geb') and @Tag('container-geb')
@Tag('slow')
@Integration
class BigReportSpec extends ContainerGebSpec { … }
```

```groovy
// build.gradle: pick what runs
tasks.named('functionalTest') {
    useJUnitPlatform {
        if (project.hasProperty('excludeTestTags')) {
            excludeTags(project.excludeTestTags.split(','))
        }
    }
}
```

```bash
./gradlew functionalTest -PexcludeTestTags=slow
```

<!--
Another way to slice tests is by using tags.

Tags are not a Grails feature: they come from the JUnit Platform. Spock 2 runs on the platform and exposes them as a @Tag annotation, and Gradle filters on them with includeTags and excludeTags in the useJUnitPlatform configuration.

What Grails adds, since 7.0.11, is that ContainerGebSpec tags every spec as geb and container-geb for you.

The command on the slide skips the spec we tagged ourselves, slow.
-->
