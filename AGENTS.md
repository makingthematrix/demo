# AGENTS.md

## Project Overview
This is a demo project testing Micronaut with Scala plugin

## Repository Structure
- `src/main/scala/com/example/`: Core implementation
- `src/test/scala/com/example/`: Tests in Scala
- `src/test/java/com/example/`: Tests in Java

## Coding Standards
- Use Scala 3 with braces syntax.
- Use standard Scala library and collections if possible.
- Try to re-use existing code and avoid duplicating functionality.

## AI-Specific Guidelines
- Read `current-work-notes.md` before making changes.
- Do not modify files in `build/`.
- Prefer functional programming patterns.
- Avoid modifying the Gradle configuration files unless necessary.
- Use `./gradlew` for all build tasks.
- Verify changes with `./gradlew clean test`. Keep `clean` after deleting or renaming a bean: the
  Scala plugin can leave its old generated code behind.
- To check the running app by hand, start it with `MICRONAUT_SERVER_PORT=8081 ./gradlew run`.
  Port 8080 may be taken by the maintainer's IDE. Don't kill processes you didn't start.

## Contacts
- Maintainer: [@makingthematrix](https://github.com/makingthematrix)