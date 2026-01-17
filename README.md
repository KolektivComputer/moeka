# Moeka
A multiplatform, modular framework for writing bots across Discord, Matrix, and
other platforms. Originally written for [Pitohui][pito_source].

## Including Moeka in Your Project:
Whether you are writing a module or a bot, you'll need to add my repository:
```kt
// build.gradle.kts, settings.gradle.kts, wherever you define your repositories,
// add this declaration:
maven {
    url = uri("https://repo.lizainslie.dev/repository/maven-public/")
}
```

The latest Moeka version at the time of writing is `0.0.2`, however I won't
promise this page will stay up to date with the latest version. instead, please
view the list of [available releases][moeka_releases]

### Creating a Module
If you are developing a module, do not shade the core or platform module, use
`api` instead of `implementation`:

```kt
dependencies {
    api("dev.lizainslie.moeka:moeka-core:$version")
    api("dev.lizainslie.moeka:moeka-discord:$version")
}
```

You can look at the [example module][moeka_example_module] for an example module
implementation.

### Creating a Bot

Creating a bot is more complicated than creating a module, but to get started,
you will want to shade the Moeka core and any necessary platforms into your jar.

```kt
dependencies {
    implementation("dev.lizainslie.moeka:moeka-core:$version")
    implementation("dev.lizainslie.moeka:moeka-discord:$version")
}
```

## Documentation?
**Soon:tm:** (when I feel like it)

## Legal
Moeka is released under the permissive [MIT License](LICENSE).

[pito_source]: https://git.lizainslie.dev/crackcafe/pitohui
[moeka_releases]: https://repo.lizainslie.dev/#browse/browse:maven-releases:dev%2Flizainslie%2Fmoeka
[moeka_example_module]: https://git.lizainslie.dev/crackcafe/moeka-example-module