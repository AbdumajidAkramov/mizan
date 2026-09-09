# Mizan

Android personal finance/budget-tracking app. Kotlin + Jetpack Compose, multi-module Gradle. namespace `dev.esbi.mizan`. Solo developer workflow — optimized for fast builds and low boilerplate.

## Structure
- `app/` — entry point, navigation, DI graph wiring, signing/build config
- `core/` — models, Room DB, repositories, MVI stores/executors, DI helpers (packages: `domain`, `data`, `presentation`, `mvikotlin`, `dagger`)
- `ui/` — design system, shared composables, theme, drawables (namespace `dev.esbi.mizan.design`)
- `features/dashboard/` — dashboard screen (flat, single module)
- `features/add-transaction/` — add-transaction screen (flat, single module)
- `build-logic/` — convention plugins (`convention-android-app`, `convention-android-library`, etc.)
- `configs/` — local secrets (keystore). Gitignored. Never read or print its contents.

## Build/Test
- Build debug APK: `./gradlew assembleDebug`
- Build release APK: `./gradlew assembleRelease`
- Unit tests: `./gradlew test`
- Lint: `./gradlew lint`
- Single module: `./gradlew :core:assembleDebug`

Requires JDK 21 (pinned in `~/.gradle/gradle.properties`). KSP warning about Kotlin version mismatch is non-blocking.

## Response constraints (token efficiency)
- Show only the changed/added lines (diff-style or minimal snippet), never reprint a whole file.
- No greetings, no filler, no restating the request. Lead with the result.
- Keep explanations to what's non-obvious; skip narrating routine steps.
- Never echo contents of `gradle.properties` or `configs/` — both hold live credentials.
