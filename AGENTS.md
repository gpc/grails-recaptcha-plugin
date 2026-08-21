# AGENTS.md - grails-recaptcha-plugin

## Project Overview

The **Grails ReCaptcha Plugin** adds [Google ReCaptcha](https://www.google.com/recaptcha) support to
Grails applications: a tag library that renders the captcha widget, and a service that verifies the
answer the user submitted.

- **Language:** Groovy 4.0.30 on Java 17
- **Framework:** Grails 7.x
- **Build System:** Gradle 8.14.4 (with wrapper)
- **Artifact:** `io.github.gpc:grails-recaptcha`
- **Current Version:** 8.0.0-SNAPSHOT
- **License:** Apache 2.0

This repository is built on
[grails-plugin-template](https://github.com/grails-plugins/grails-plugin-template). Files under
`build-logic/`, `gradle/`, `.github/` and `.agents/` are synced from the template — do not edit them
here; change them upstream. Plugin-specific guidance belongs in this file.

## Skill Files (Best Practices)

Detailed best practices are documented as skills in `.agents/skills/` (`.claude` is a symlink to `.agents`):

| Skill                                                                                  | Purpose                                                 |
|----------------------------------------------------------------------------------------|---------------------------------------------------------|
| [`repository-structure`](.agents/skills/repository-structure/SKILL.md)                 | Canonical directory layout and architectural rules      |
| [`gradle-best-practices`](.agents/skills/gradle-best-practices/SKILL.md)               | Gradle best practices, convention plugins, and idioms   |
| [`plugin-project`](.agents/skills/plugin-project/SKILL.md)                             | Plugin project scope: source code + unit tests only     |
| [`example-apps`](.agents/skills/example-apps/SKILL.md)                                 | Example app patterns: integration & functional tests    |
| [`enhance-plugin-with-template`](.agents/skills/enhance-plugin-with-template/SKILL.md) | Migrate an existing plugin onto the template structure   |

**Read these skill files before making structural changes to the repository.**

## Critical Rules

1. **NEVER add code to the root `build.gradle` to configure subprojects.** No `subprojects {}`, `allprojects {}`, or
   `configure()` blocks. All shared configuration goes through convention plugins in `build-logic/`.
2. **The plugin project contains ONLY plugin code and unit tests.** No integration tests, no functional tests, no
   example controllers or views.
3. **Example apps under `examples/` host all integration and functional tests.** They depend on the plugin via
   `implementation project(':grails-recaptcha')` and test it as a real consumer would.
4. **Use Gradle convention plugins to deduplicate.** If two or more subprojects share build logic, extract it into a
   convention plugin in `build-logic/`.
5. **Always use lazy Gradle APIs** to avoid eager initialization (`tasks.register()`, `tasks.named()`, `configureEach`,
   `provider {}`).
6. **Never contact Google from a test.** Unit tests stub `Post`; integration tests assert on rendered markup only.
   Google's public test keys are configured in the example app so nothing needs a real ReCaptcha account.

## Repository Structure

```
grails-recaptcha-plugin/
├── .agents/skills/      # Agent skill files (.claude is a symlink to .agents)
├── plugin/              # The plugin (artifact: grails-recaptcha)
│   ├── grails-app/      #   RecaptchaService, RecaptchaTagLib, plugin.yml
│   └── src/             #   ReCaptcha, net/, util/, plugin descriptor, unit tests
├── examples/app1/       # Example Grails app hosting the integration tests
├── docs/                # Asciidoctor documentation
├── build-logic/         # Gradle convention plugins (composite build)
├── code-coverage/       # JaCoCo aggregation module
├── build.gradle         # Root build file (docs + root-publish ONLY)
├── settings.gradle      # Multi-project settings
├── gradle.properties    # Version properties
└── project.yml          # Project metadata (POM, docs, release notes, version index)
```

## Build and Test Commands

```bash
# Full build (compile + unit tests + integration tests)
./gradlew build

# Run only unit tests (plugin module)
./gradlew :grails-recaptcha:test

# Run integration tests (example app)
./gradlew :app1:integrationTest

# Skip tests
./gradlew build -PskipTests

# Run the example app at http://localhost:8080/captcha
./gradlew :app1:bootRun

# Generate documentation
./gradlew docs

# Clean build
./gradlew clean build

# Run code style checks only
./gradlew codeStyle

# Skip code style checks
./gradlew build -PskipCodeStyle
```

## SDK Requirements

Use SDKMAN to install the correct tool versions (see `.sdkmanrc`):

- Java: `17.0.18-librca`
- Gradle: `8.14.4`
- Groovy: `4.0.30`

Run `sdk env install` to set up the environment.

## Architecture

1. **`RecaptchaTagLib`** exposes the `recaptcha` tag namespace and delegates all rendering to the service.
2. **`RecaptchaService`** resolves the plugin configuration, holds a single `ReCaptcha` instance, and verifies answers.
3. **`ReCaptcha`** builds the widget markup and performs the `siteverify` call through `Post`.

### Core Classes

| Class                    | Location                                                  | Purpose                                                     |
|--------------------------|-----------------------------------------------------------|-------------------------------------------------------------|
| `RecaptchaGrailsPlugin`  | `plugin/src/main/groovy/com/megatome/grails/`             | Plugin descriptor                                           |
| `RecaptchaTagLib`        | `plugin/grails-app/taglib/com/megatome/grails/`           | `recaptcha:` namespace tags                                 |
| `RecaptchaService`       | `plugin/grails-app/services/com/megatome/grails/`         | Config resolution, markup creation, answer verification     |
| `ReCaptcha`              | `plugin/src/main/groovy/com/megatome/grails/recaptcha/`    | Widget markup and the `siteverify` call                     |
| `Post`                   | `plugin/src/main/groovy/com/megatome/grails/recaptcha/net/`| HTTP POST with proxy and timeout support                    |
| `QueryParams`            | `plugin/src/main/groovy/com/megatome/grails/recaptcha/net/`| Query string building                                       |
| `AuthenticatorProxy`     | `plugin/src/main/groovy/com/megatome/grails/recaptcha/net/`| Proxy configuration and authentication                      |
| `ConfigHelper`           | `plugin/src/main/groovy/com/megatome/grails/util/`         | Lenient boolean coercion of config values                   |

## Configuration

The plugin reads the `recaptcha` block of the consuming application's `application.yml`
(`publicKey`, `privateKey`, `includeScript`, `includeNoScript`, `enabled`, plus the `proxy` and
`timeoutConfig` sub-blocks). If no `recaptcha` config is present, `RecaptchaService` falls back to
loading a `RecaptchaConfig` Groovy script from the classpath; the template for it ships in
`plugin/src/main/templates/`.

`RecaptchaService` caches both the resolved config and the `ReCaptcha` instance on first use, so
configuration changes at runtime are not picked up.

## Testing

### Unit Tests (`plugin/src/test/`)

Spock specs on the JUnit Platform. `PostTests` skips the proxy and timeout cases when no local
proxy is available, so a partly-skipped run is expected.

### Integration Tests (`examples/app1/`)

`RecaptchaServiceIntegrationSpec` asserts the service is wired into the application context and
renders the configured site key. `RecaptchaTagLibIntegrationSpec` requests a real page from the
running application and asserts the tag output. Neither makes a network call to Google.

## CI/CD

- **CI** (`.github/workflows/ci.yml`): Builds and tests on push/PR; publishes snapshots to Maven Central Snapshots on
  push to release branches.
- **Release** (`.github/workflows/release.yml`): 4-stage pipeline triggered by GitHub release — stage artifacts, release
  to Maven Central, publish docs to GitHub Pages, bump version.
- **Release Notes** (`.github/workflows/release-notes.yml`): Auto-drafts release notes using release-drafter with
  category labels.

## Code Conventions

- Groovy source files use standard Grails conventions (services and taglibs in `grails-app/`, other classes in
  `src/main/groovy/`).
- **Use `def` for local variables** where the type is inferred from the right-hand side. Explicit types should only be
  used for local variables when the type cannot be inferred or when needed for `@CompileStatic` compilation. This
  applies to both production code and tests.
- Service and taglib artefacts get a `log` field injected by Grails — never declare one, it is a compile error.
- When writing Gradle, always use the latest best practices to avoid eager initialization.
