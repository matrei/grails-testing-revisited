# Spock testing revisited for Grails 7/8

Slides and demo application for the talk at Community over Code Europe, Glasgow, October 2026:
a walkthrough of Grails testing support, including the new modules and features in Grails 7 and 8.

- **Slides in the browser:** <https://matrei.github.io/grails-testing-revisited/>
- **Slides as PDF:** [spock-testing-revisited-grails-7-8.pdf](public/spock-testing-revisited-grails-7-8.pdf)
  (every click step is a page), also attached to the [v1.0 release](https://github.com/matrei/grails-testing-revisited/releases/tag/v1.0)
- **Demo app:** [`demo/`](demo), a Grails 8.0.0 app whose specs are the code on the slides

## Slides (Slidev)

```bash
npm install
npm run dev         # http://localhost:3030, presenter view at /presenter
npm run build       # static site in dist/
npm run export      # PDF backup (playwright-chromium is already a dev dependency)
```

The deck is `slides.md` plus one file per section in `pages/`. Code on the slides is imported
straight from the demo app with `<<< @/demo/...#region`, so what you see is what ran.

## Demo app (`demo/`)

A Grails 8.0.0 application with one spec per testing trait. Requires Java 21 and Docker.

```bash
cd demo
./gradlew test                       # unit: src/test
./gradlew integrationTest            # @Integration, @Rollback, @DatabaseCleanup, HttpClientSupport
./gradlew functionalTest             # ContainerGebSpec in a Selenium container (our own phase via testPhases {})
./gradlew functionalTest -Precord    # ...and record MP4s to build/gebContainer/recordings
```
