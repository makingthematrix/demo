---
id: current-work-notes
type: task-spec
status: active
title: Current work notes
parent: micronaut-scala-demo
---

# Current work notes

Rules for agents working on this project right now. They come from problems already hit and
decisions already made. Some are tied to Micronaut Scala plugin 1.0.0-M1 and stop applying when
the plugin changes. The reasoning behind them is in the goal doc (`micronaut-scala-demo`,
`goal-and-requirements.md`), mostly under Tech Notes.

## 1. Don't put `@Client` on an `@Inject var`

Plugin 1.0.0-M1 (and the 1.0.0-SNAPSHOT of 2026-11-12) drops `@Client(...)` from an
`@Inject var`. The injected client has no URL, and every request fails with `NoHostException`.
`@Named` and `@Value` on a `var` are not affected.

- In tests, take the client as a test-method parameter, as
  `WelcomeTest.rootServesTheWelcomePage` does: `def test(@Client("/") client: HttpClient)`.
- In general, prefer constructor injection over `@Inject var`, as the plugin's guide recommends.
- The upstream bug report is drafted in
  `.thinkrail/context/ISSUE-micronaut-scala-client-on-inject-var.md` and hasn't been filed yet.

## 2. Don't "fix" the two bug tests

- `WelcomeTest.clientOnInjectVarLosesItsUrl` is meant to expect the `NoHostException`: it pins
  the bug from §1.
- `WelcomeJavaTest.clientOnInjectFieldKeepsItsUrl` is its control. The same field in Java,
  compiled by javac rather than the plugin, gets a working client.

Don't delete either test or flip its assertions. When a plugin upgrade makes the Scala test fail
while the Java one still passes, the bug is fixed. Then remove both tests and the workaround from
§1, and update the goal doc.

## 3. Java only for the control test

`src/test/java/com/example/WelcomeJavaTest.java` is the only Java source in the project, and it's
there on purpose (§2). All other code, main and test, is Scala.

## 4. The goal doc is the source of truth

Read `goal-and-requirements.md` before making a change, and update it in the same change when a
capability is added or removed. Its Non-Goals were removed from the scaffold on purpose. Don't
add them back:

- native image, Micronaut AOT and Docker packaging;
- a database or Micronaut Data;
- Scala 2.

## 5. Build settings that must stay

In `build.gradle.kts`:

- **The plugin's artifact name carries the exact Scala version**, as in
  `micronaut-inject-scala_3.9.0`. Upgrading Scala means changing both together.
- **The plugin goes in `scalaCompilerPlugins`**, never as an ordinary dependency. As an ordinary
  dependency it isn't loaded by the compiler and no Micronaut code is generated. It must also stay
  off the runtime classpath.
- **The `java.compiler` flags stay** (`-J--add-modules=java.compiler` and the fork JVM argument).
  The plugin needs them.
- **`-no-indent` stays.** It's what enforces braces syntax: indented code fails to compile.
  To convert indented code, compile once with `-no-indent -rewrite`.
- **`micronautVersion=5.2.6` is the platform version**, not Micronaut Core. It ships Core 5.2.23,
  while the plugin is built against 5.2.21.
