---
layout: section
class: section-slide
---

# The testing landscape

Where tests live and what they pull in

<!--
⏱ 04:30 — The testing landscape, about a minute.
-->

---

# Two phases out of the box

<div class="grid grid-cols-3 gap-5 mt-4">
<div class="layer-card">
  <div class="layer-title">Unit</div>
  <div class="layer-path">src/test/groovy</div>
  <div class="layer-cmd">./gradlew test</div>
  <div class="layer-desc">traits · in-memory datastore · a tiny application context · no server</div>
  <div v-click class="layer-foot">milliseconds per test<br>thousands of them</div>
</div>
<div class="layer-card">
  <div class="layer-title">Integration</div>
  <div class="layer-path">src/integration-test/groovy</div>
  <div class="layer-cmd">./gradlew integrationTest</div>
  <div class="layer-desc">@Integration · the real app on a random port · real database · <b>browser tests with Geb live here too</b></div>
  <div v-click class="layer-foot">seconds per class<br>the app boots once per JVM</div>
</div>
<div class="layer-card optional">
  <div class="layer-title">Your own phases <span class="badge">opt-in</span></div>
  <div class="layer-path">src/functional-test/groovy</div>
  <div class="layer-cmd">testPhases {<br>&nbsp;&nbsp;&nbsp;&nbsp;functionalTest { }<br>}</div>
  <div class="layer-desc">declared with the Grails Gradle plugin’s <code>testPhases</code> (7.1), e.g. to keep Docker-bound browser specs apart</div>
  <div v-click class="layer-foot">same @Integration underneath<br>just a different task and classpath</div>
</div>
</div>

<style>
.layer-card { border: 1px solid var(--grails-orange); border-radius: 10px; padding: 1em 1.1em; background: rgba(254,181,113,0.05); }
.layer-card.optional { border-style: dashed; border-color: rgba(255,255,255,0.35); background: rgba(255,255,255,0.03); }
.layer-title { font-size: 1.4em; font-weight: 700; color: var(--grails-orange); }
.layer-card.optional .layer-title { color: rgba(255,255,255,0.85); }
.layer-path, .layer-cmd { font-family: var(--slidev-code-font-family); font-size: 0.78em; opacity: 0.85; }
.layer-cmd { margin-top: 0.2em; }
.layer-desc { margin-top: 0.8em; font-size: 0.85em; opacity: 0.9; min-height: 3.2em; }
.layer-foot { margin-top: 1em; padding-top: 0.6em; border-top: 1px dashed rgba(255,255,255,0.15); font-size: 0.8em; opacity: 0.7; }
</style>

<!--
Grails gives you exactly two test phases out of the box: unit and integration. There is no “functional” phase in Grails itself.

[click] Unit tests use the traits. They run GORM against an in-memory datastore and spin up a tiny application context per spec. No HTTP, no database.

[click] Integration tests boot the real application on a random port and share it across specs. Browser tests with Geb are integration tests too: same source set, same task.

[click] However, Grails 7.1 made it easy to add your own custom test phases.

For example, if you want to separate your browser tests, this is one way to do it.
-->

