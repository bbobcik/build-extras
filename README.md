# build-extras

This project implements useful additions to a Maven build process:
 - additional rules for [Maven Enforcer](https://maven.apache.org/enforcer/maven-enforcer-plugin/index.html);
 - facility for intelligent scheduling of JUnit5 tests.

## License

Apache License, Version 2.0 (see [LICENSE](LICENSE) file).

---

## Enforcer Rules

Add the following to your `pom.xml` section that defines an execution of the `enforce` goal
of the `maven-enforcer-plugin`:

```xml
<dependencies>
    <dependency>
        <groupId>cz.auderis</groupId>
        <artifactId>build-extras</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

Then you can use the rules described in the following sections.

### `banExplicitDependencyVersions`

This rule checks that no dependency declared in the `<dependencies>` section
of the project POM file uses an explicit version number.
The goal is to have all versions defined in `<dependencyManagement>` section
(preferably via imported BOMs) and only use dependency coordinates in
the main dependency section.

---

## Test scheduling
