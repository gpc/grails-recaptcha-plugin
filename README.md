# Grails ReCaptcha Plugin

[![Maven Central](https://img.shields.io/maven-central/v/io.github.gpc/grails-recaptcha.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.gpc/grails-recaptcha)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![CI](https://github.com/gpc/grails-recaptcha/actions/workflows/ci.yml/badge.svg)](https://github.com/gpc/grails-recaptcha/actions/workflows/ci.yml)

Adds [Google ReCaptcha](https://www.google.com/recaptcha) support to Grails applications: a tag
library that renders the captcha widget, and a service that verifies the answer the user submitted.
Protects your forms from spam and abuse while letting real people pass through with ease.

## Installation

```groovy
dependencies {
    implementation 'io.github.gpc:grails-recaptcha:8.0.0'
}
```

## Documentation

Full documentation — installation, configuration, the tag library reference, verification, and
examples — is published at:

**https://gpc.github.io/grails-recaptcha/**

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) for how to set up a development environment and submit changes.

## License

Released under the [Apache License, Version 2.0](LICENSE.txt).
