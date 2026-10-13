---
theme: seriph
title: Spock testing revisited for Grails 7/8
titleTemplate: '%s'
author: Mattias Reichel
info: |
  ## Spock testing revisited for Grails 7/8
  A walkthrough of Grails testing support, including the new modules and features in Grails 7 and 8.

  Community over Code Europe · Glasgow · October 2026
colorSchema: dark
themeConfig:
  primary: '#FEB571'
fonts:
  sans: Inter
  mono: JetBrains Mono
highlighter: shiki
lineNumbers: false
transition: slide-left
duration: 40min
timer: countdown
drawings:
  persist: false
exportFilename: spock-testing-revisited-grails-7-8
download: 'https://matrei.github.io/grails-testing-revisited/spock-testing-revisited-grails-7-8.pdf'
htmlAttrs:
  lang: en
layout: cover
class: text-center cover-slide
---

<img src="/grails_logo.svg" class="w-24 mx-auto mb-6" alt="Grails" />

# Spock testing revisited<br>for Grails 7/8

<p class="text-xl opacity-80 mt-4">A walkthrough of Grails testing support,<br>including the new modules and features in Grails 7 and 8</p>

<div class="mt-10 text-sm opacity-70">
Mattias Reichel · Apache Grails PMC<br>
Community over Code Europe · Glasgow · October 2026
</div>

<!--
⏱ 00:00 — Start the timer in presenter mode before you say a word. The markers assume about 110 words a minute.

Welcome! This talk is about the testing support available in Grails: how you test a Grails application today, in Grails 7 and the new Grails 8, with Spock.

We’ll start with the unit test traits, then move on to integration tests and browser tests. Along the way I’ll show you what has changed under the hood, and the new testing modules that came with Grails 7 and 8.

All the code on the slides comes from a small Grails 8 app. You’ll find the link at the end.
-->

---
src: ./pages/00-intro.md
---

---
src: ./pages/02-landscape.md
---

---
src: ./pages/03-unit.md
---

---
src: ./pages/04-integration.md
---

---
src: ./pages/05-functional.md
---

---
src: ./pages/06-slicing.md
---

---
src: ./pages/08-wrapup.md
---
