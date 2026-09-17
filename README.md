# Moeka

A multiplatform, modular engine for writing bots across Discord, Matrix, and other platforms. Built for [Pitohui](https://github.com/LizAinslie/pitohui) and other bots.

Discord is live today. Matrix is planned as an own-protocol client and appservice path, not a wrapper around a single vendor SDK. Platform adapters stay library-agnostic behind an SPI.

## Include Moeka

Add the Maven repository that currently hosts the artifacts:

```kt
maven {
    url = uri("https://repo.lizainslie.dev/repository/maven-public/")
}
```

Latest release at time of writing: `0.0.2`. Prefer the [release browser](https://repo.lizainslie.dev/#browse/browse:maven-releases:dev%2Flizainslie%2Fmoeka) over trusting this number forever. Coordinates will move to KolektivComputer publishing in a follow-up; until then they remain `dev.lizainslie.moeka:*`.

### Module

Do not shade core or platform modules. Use `api`:

```kt
dependencies {
    api("dev.lizainslie.moeka:moeka-core:$version")
    api("dev.lizainslie.moeka:moeka-discord:$version")
}
```

See [moeka-example-module](https://github.com/KolektivComputer/moeka-example-module).

### Bot

Shade core and platforms into your jar:

```kt
dependencies {
    implementation("dev.lizainslie.moeka:moeka-core:$version")
    implementation("dev.lizainslie.moeka:moeka-discord:$version")
}
```

## Docs

Not yet. Source of truth is this repo and the example module until a docs site exists.

## License

[MIT](LICENSE) · [KolektivComputer/moeka](https://github.com/KolektivComputer/moeka)
