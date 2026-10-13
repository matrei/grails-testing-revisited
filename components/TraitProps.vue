<script setup lang="ts">
// The properties a unit-test trait gives you, as pills in the slide header.
// `own` are the trait's own, `web` adds the ones every web trait shares,
// and `used` outlines the ones the code on the slide actually uses.
defineProps<{
  own?: string[]
  web?: boolean
  used?: string[]
}>()

const webProps = ['request', 'response', 'params', 'flash', 'session', 'status', 'views', 'servletContext']
</script>

<template>
  <div class="trait-props">
    <div v-if="own?.length" class="trait-props-row">
      <code v-for="p in own" :key="p" :class="{ on: used?.includes(p) }">{{ p }}</code>
    </div>
    <div v-if="web" class="trait-props-row web">
      <span class="trait-props-label">web</span>
      <code v-for="p in webProps" :key="p" :class="{ on: used?.includes(p) }">{{ p }}</code>
    </div>
  </div>
</template>

<style scoped>
.trait-props { position: absolute; top: 2.45rem; right: 3.5rem; max-width: 29.5rem; display: flex; flex-direction: column; align-items: flex-end; gap: 0.3rem; }
.trait-props-row { display: flex; flex-wrap: wrap; justify-content: flex-end; align-items: center; gap: 0.25rem; }
.trait-props code { font-size: 0.55rem; padding: 0.1em 0.6em; border-radius: 999px; border: 1px solid rgba(255,255,255,0.18); background: transparent; opacity: 0.5; white-space: nowrap; }
.trait-props code.on { opacity: 1; border-color: var(--grails-orange); color: var(--grails-orange); }
.trait-props-label { font-size: 0.5rem; text-transform: uppercase; letter-spacing: 0.08em; opacity: 0.4; margin-right: 0.15rem; }
</style>
