---
id: micronaut-scala-demo
type: goal-and-requirements
status: active
title: Micronaut Scala demo
---

# Micronaut Scala demo

A small Scala 3 Micronaut service proving that the new Micronaut Scala compiler plugin carries an ordinary JSON REST API.

## Overview

A small Micronaut 5 HTTP service written in Scala 3.9.0, built with Gradle on JDK 25. It exists to
prove that the Micronaut Scala compiler plugin (1.0.0-M1) can carry a realistic JSON REST API:
beans with constructor injection, controllers, `@Serdeable` case classes (with `Option` and
collections), and Micronaut HTTP tests. Its audience is developers deciding whether Micronaut +
Scala is a viable stack.

## Problem

Until now Micronaut only worked with Java, Kotlin and Groovy, because its compile-time processing
runs as a Java annotation processor and scalac can't run those. The Scala compiler plugin that
closes the gap is days old, and no one has yet shown an ordinary service working end to end on it.

## Capabilities

- **Welcome page**: `GET /` (locally `http://localhost:8080/`) serves a minimal HTML page titled
  "Welcome" with the heading "Welcome!". It is served from a Scala controller.

A notes REST API. A note has an id, a title, an optional body and a list of tags.

- **CRUD over HTTP**: list, get, create, update and delete notes as JSON.
- **Unknown id → 404** on get, update and delete.
- **Invalid input → 400** through bean validation (e.g. a blank title), with a readable error.
- **Injected service layer**: the controller depends on a separate `@Singleton` note store through
  constructor injection, so Micronaut's DI on Scala classes is visible in the code.
- **In-memory storage**: notes live only for the lifetime of the process.

## Success Conditions

- The app compiles from Scala sources only (no Java left from the scaffold), starts on the JVM and
  serves the API.
- Micronaut HTTP tests written in Scala exercise every capability above and pass.

## Non-Goals

- **Native image, Micronaut AOT and Docker packaging.** The proof is JVM-only, and the scaffold's
  configuration for these is removed so it doesn't imply an unproven claim.
- **Persistence.** No database or Micronaut Data. Storage stays in memory.
- **Scala 2.** The plugin supports Scala 3 only.

## Alternatives Considered

| Alternative | Gap | URL |
|---|---|---|
| micronaut-scala `test-suite-scala*` | Checks one feature at a time inside the plugin's own build. It is not a runnable app someone could copy. | https://github.com/micronaut-projects/micronaut-scala |
| Micronaut Core docs PR #12981 | Scala snippets for the framework guide. Fragments, not a running service. | https://github.com/micronaut-projects/micronaut-core/pull/12981 |
| Core wiki "Scala Support" | An old sbt proof of concept built against a locally published plugin. Out of date. | https://github.com/micronaut-projects/micronaut-core/wiki/Scala-Support |

## Tech Notes

| Aspect | Choice | Rationale |
|---|---|---|
| Language | Scala 3.9.0 | The plugin's primary LTS compiler. The plugin artifact carries the full compiler version (`micronaut-inject-scala_3.9.0`), so the two upgrade together. |
| Framework | Micronaut platform 5.2.6 (Core 5.2.23), Netty runtime, plain HTTP on 8080 | The plugin 1.0.0-M1 is built against Core 5.2.21. Patch releases within 5.2.x are compatible, and pinning Core down would put it out of step with the platform's other modules. HTTPS is left to whatever fronts the app. |
| Compile-time processing | `scalaCompilerPlugins("io.micronaut.scala:micronaut-inject-scala_3.9.0")` + `runtimeOnly("io.micronaut.scala:micronaut-runtime-scala")` | The plugin replaces the Java annotation processors. It must stay off the runtime classpath, because it bundles Micronaut and ASM classes. |
| Serialization | Micronaut Serialization (Jackson) | Uses the plugin's introspections. `micronaut-runtime-scala` adds `Option`, collection and enum support. Optional fields default to `None`, because an omitted `Option` property otherwise arrives as `null` (micronaut-serialization#1416). |
| Build | Gradle (Kotlin DSL), JDK 25 | Kept from the scaffold. The Gradle path is the only one the plugin's repository tests. |
| Tests | JUnit 5 + `@MicronautTest`, in Scala. Inject `@Client` as a test-method parameter, never as an `@Inject var` | Micronaut Test has no ScalaTest integration, and the plugin guide points application tests at the normal Micronaut testing APIs. Plugin 1.0.0-M1 drops `@Client` from an `@Inject var`: the generated setter argument has no annotation metadata, so the client has no URL (`NoHostException`). `@Named` and `@Value` on a `var` are unaffected. |
