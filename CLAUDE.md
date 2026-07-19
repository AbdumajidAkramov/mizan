# Mizan

Android personal finance/budget-tracking app. Kotlin + Jetpack Compose, multi-module Gradle. namespace `dev.esbi.mizan`. Migrating users from legacy "Money Manager" SQLite backups (`.mmbak`).

No Python or Telegram API in this codebase — `scripts/migrate_mmbak.py` is a one-off local migration utility, not a runtime service.

## Structure
- `app/` — application module, entry point, DI wiring, signing/build config
- `core/` — `dagger` (DI), `design`, `model`, `mvi-kotlin` (MVI base classes), `ocr-scanner`, `voice-recognition`
- `domain/`, `data/`, `presentation/`, `ui-kit/` — shared layers
- `features/<name>/{data,domain,presentation}` — feature modules (currently `dashboard`, `add-transaction`)
- `build-logic/` — convention plugins (`convention-android-app`, etc.) controlling module build config
- `design/` — design system docs/assets
- `readme/`, `docs/` — implementation notes and bugfix logs (historical, not API docs)
- `configs/` — local secrets (keystore). Gitignored. Never read or print its contents.

## Build/Test
- Build debug APK: `./gradlew assembleDebug`
- Build release APK: `./gradlew assembleRelease`
- Unit tests: `./gradlew test` (or `./gradlew :module:testDebugUnitTest` for one module)
- Instrumented tests (needs device/emulator): `./gradlew connectedAndroidTest`
- Lint: `./gradlew lint`
- Single module build: `./gradlew :features:dashboard:build`

Note: a full local `./gradlew tasks` run currently fails in this environment on a bare `26.0.1` error (looks like an SDK/NDK mismatch, not code) — verify toolchain before relying on build output.

## Response constraints (token efficiency)
- Show only the changed/added lines (diff-style or minimal snippet), never reprint a whole file, unless the user asks for the full file.
- No greetings, no filler, no restating the request. Lead with the result.
- Keep explanations to what's non-obvious; skip narrating routine steps.
- Never echo contents of `gradle.properties` or `configs/` — both hold live credentials.
