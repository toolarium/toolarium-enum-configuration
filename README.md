[![License](https://img.shields.io/github/license/toolarium/toolarium-enum-configuration)](https://github.com/toolarium/toolarium-enum-configuration/blob/master/LICENSE)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.toolarium/toolarium-enum-configuration/1.3.3)](https://search.maven.org/artifact/com.github.toolarium/toolarium-enum-configuration/1.3.3/jar)
[![javadoc](https://javadoc.io/badge2/com.github.toolarium/toolarium-enum-configuration/javadoc.svg)](https://javadoc.io/doc/com.github.toolarium/toolarium-enum-configuration)

# toolarium-enum-configuration

Defines the toolarium enumeration configuration or in short enum configuration.

## Getting Started

This project implements a so-called enumeration configuration that turns a Java enumeration into a set of a configuration that can be persisted.

The basic idea behind this is to solve the chicken-and-egg problem: Your software defines or expect some configuration values that are e.g.
in a database. Now, in your code, you typically define a constant with which you retrieve the value. In the database you have to add the same
constant with the corresponding value. From various experiences, there are often a lot of problems with this approach.

Here comes a possible solution with this project:
1) You simply define an enum in Java.
2) Then add the EnumConfiguration or the EnumValueConfiguration. Use the annotation attributes for more information.
3) Use the EnumConfigurationProcessor to generate the output you use, e.g. SQL queries

The big advantage is that you only have one place (source code) where you define your "constant".

### Installing

Add the annotation processor to your project or simply use the common build.

## Built With

* [cb](https://github.com/toolarium/common-build) - The toolarium common build

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/toolarium/toolarium-enum-configuration/tags). 

### Gradle:

```groovy
dependencies {
    implementation "com.github.toolarium:toolarium-enum-configuration:1.3.3"
}
```

### Maven:

```xml
<dependency>
    <groupId>com.github.toolarium</groupId>
    <artifactId>toolarium-enum-configuration</artifactId>
    <version>1.3.3</version>
</dependency>
```

## Jackson Version Compatibility

This library depends on Jackson. Starting with Jackson 2.21.x, `jackson-core` introduces a transitive BOM reference that can cause unintended version downgrades (e.g. `jackson-core:2.21.x` being resolved to `2.18.x`) when mixed with earlier Jackson releases.

To ensure all Jackson modules are aligned on the same version, import the Jackson BOM in your Gradle build.

### Align to Jackson 2.21.x (recommended):

```groovy
dependencies {
    implementation platform("com.fasterxml.jackson:jackson-bom:2.21.5")
    implementation "com.fasterxml.jackson.core:jackson-databind"
    // add other jackson modules as needed, versions are managed by the BOM
}
```

### Pin to an older version (e.g. Jackson 2.18.x):

```groovy
dependencies {
    implementation platform("com.fasterxml.jackson:jackson-bom:2.18.6")
    implementation "com.fasterxml.jackson.core:jackson-databind"
    // all jackson modules will resolve to 2.18.x
}
```

Using the BOM ensures consistent versions across all Jackson modules and prevents accidental version mismatches between `jackson-databind` and `jackson-core`.