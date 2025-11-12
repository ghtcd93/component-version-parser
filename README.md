# Component Version Parser

[![Maven Central](https://img.shields.io/maven-central/v/io.github.ghtcd93/component-version-parser.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:io.github.ghtcd93%20AND%20a:component-version-parser)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

A simple, zero-dependency Java library to parse a component string (e.g., "spring-boot-2.7.5") into its name ("spring-boot") and version ("2.7.5").

## Features

- Parses component name and version separated by a hyphen.
- Supports various version formats (SemVer, date-based, qualifiers, etc.).
- Handles edge cases like `null` or unparseable strings gracefully by returning `Optional.empty()`.
- Written in modern Java (16+).
- Zero external dependencies.

## Getting Started

You can add the library to your project as a dependency from Maven Central.

### Maven

```xml
<dependency>
    <groupId>io.github.ghtcd93</groupId>
    <artifactId>component-version-parser</artifactId>
    <version>1.0.1</version>
</dependency>
```

### Gradle

```groovy
implementation 'io.github.ghtcd93:component-version-parser:1.0.1'
```

> **Note**: Please replace `LATEST_VERSION` with the latest version number found on Maven Central.

## Usage Example

```java
import io.github.ghtcd.ComponentVersionParser;
import io.github.ghtcd.ComponentVersion;
import java.util.Optional;

// Successful parsing
Optional<ComponentVersion> result = ComponentVersionParser.parse("my-awesome-lib-v1.2.0-alpha");

result.ifPresent(cv -> {
    System.out.println("Name: " + cv.name());       
    System.out.println("Version: " + cv.version()); 
});

// Failed parsing
Optional<ComponentVersion> emptyResult = ComponentVersionParser.parse("invalid-component");
System.out.println(emptyResult.isPresent());
```

## Contributing
Contributions are welcome! Please feel free to submit a pull request or create an issue.

## License
This project is licensed under the Apache License 2.0. See the LICENSE file for details.