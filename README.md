[![License](https://img.shields.io/github/license/toolarium/toolarium-enum-configuration)](https://github.com/toolarium/toolarium-enum-configuration/blob/master/LICENSE)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.toolarium/toolarium-enum-configuration/1.3.0)](https://search.maven.org/artifact/com.github.toolarium/toolarium-enum-configuration/1.3.0/jar)
[![javadoc](https://javadoc.io/badge2/com.github.toolarium/toolarium-enum-configuration/javadoc.svg)](https://javadoc.io/doc/com.github.toolarium/toolarium-enum-configuration)

# toolarium-enum-configuration

Defines the toolarium enumeration configuration or in short enum configuration.

## Getting Started

This project implements a so-called enumeration configuration that turns a Java enumeration into a set of a configuration that can be persisted.

The basic idea behind this is to solve the chicken-and-egg problem: Your software defines or expect some configuration values that are e.g.
in a database. Now, in your code, you typically define a constant with which you retrieve the value. In the database you have to add the same
constant with the corresponding value. From various experiences, there are often a lot of problems with.

Here comes a possible solution with this project:
1) You simply define an enum in Java.
2) Then add the EnumConfiguration or the EnunmValueConfigutation. Use the annotation attributes for more information.
3) Use the EnumConfigurationProcessor to generate the output you use, e.g. SQL queries

The big advantage is that you only have one place (source code) where you define your "constant".

### Installing

Add the annoation processor to you project or simple use the common build.

## Built With

* [cb](https://github.com/toolarium/common-build) - The toolarium common build

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/toolarium/toolarium-enum-configuration/tags). 

### Gradle:

```groovy
dependencies {
    implementation "com.github.toolarium:toolarium-enum-configuration:1.3.0"
}
```

### Maven:

```xml
<dependency>
    <groupId>com.github.toolarium</groupId>
    <artifactId>toolarium-enum-configuration</artifactId>
    <version>1.3.0</version>
</dependency>
```