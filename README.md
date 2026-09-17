# Moeka

A multiplatform, modular engine for writing bots across Discord, Matrix, and other platforms. Built for [Pitohui](https://github.com/Crack-Cafe/pitohui) and other bots.

Discord is live today. Matrix is planned as an own-protocol client and appservice path, not a wrapper around a single vendor SDK. Platform adapters stay library-agnostic behind an SPI.

## Roadmap (locked)

**Platform bridge (v1)** — Discord ↔ Matrix. Bridge communities and selected chats. Users may link multiple accounts and reach their communities across those platforms. Consent and mod surfaces stay explicit. E2EE limits get disclosed, not papered over.

**Module unload** — Unload soft-disables the module: data and tables stay, serving stops. After a configurable idle period it enters quarantine. After a further configurable period, admins are prompted to archive or purge. Plugins ship their own migrations. Archives restore across versions by replaying that migration chain.

**Extensions vs modules** — Extensions are build-time (dashboard, HTTP, CDM, platform-bridge). Modules are hotloadable feature packs that plug into those hooks.

Tracking: [epic #2](https://github.com/KolektivComputer/moeka/issues/2).

## Include Moeka

```kt
maven {
    url = uri("https://repo.lizainslie.dev/repository/maven-public/")
}
```

Latest release at time of writing: `0.0.2`. Prefer the [release browser](https://repo.lizainslie.dev/#browse/browse:maven-releases:dev%2Flizainslie%2Fmoeka). Coordinates remain `dev.lizainslie.moeka:*` until KolektivComputer publishing lands.

### Module

```kt
dependencies {
    api("dev.lizainslie.moeka:moeka-core:$version")
    api("dev.lizainslie.moeka:moeka-discord:$version")
}
```

See [moeka-example-module](https://github.com/KolektivComputer/moeka-example-module).

### Bot

```kt
dependencies {
    implementation("dev.lizainslie.moeka:moeka-core:$version")
    implementation("dev.lizainslie.moeka:moeka-discord:$version")
}
```

## License

[MIT](LICENSE) · [KolektivComputer/moeka](https://github.com/KolektivComputer/moeka)