# Component Version Parser

[![Maven Central](https://img.shields.io/maven-central/v/io.github.ghtcd93/component-version-parser.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:io.github.ghtcd93%20AND%20a:component-version-parser)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

A simple, zero-dependency Java library to parse component identifiers. It intelligently handles both simple component strings (e.g., "spring-boot-2.7.5") and standardized **Package URLs (PURLs)** (e.g., "pkg:maven/org.springframework/spring-core@5.3.23").

## Features

- **Intelligent Parsing**: Automatically detects and parses both simple name-version strings and standard Package URLs (PURLs).
- **Rich PURL Support**: Extracts detailed information from PURLs, including type, namespace, name, version, qualifiers, and subpath.
- **Flexible Heuristics**: Parses simple strings with various version formats (SemVer, date-based, etc.) and separators (`-` or `@`).
- **Modern Java**: Written in modern Java (16+) and uses `Optional` for safe handling of parsing results.
- Written in modern Java (16+).
- Zero external dependencies.

## Getting Started

You can add the library to your project as a dependency from Maven Central.

### Maven

```xml
<dependency>
    <groupId>io.github.ghtcd93</groupId>
    <artifactId>component-version-parser</artifactId>
    <version>1.0.2</version>
</dependency>
```

### Gradle

```groovy implementation 'io.github.ghtcd93:component-version-parser:1.0.2'
``` 

## Usage Example

The `ComponentParser` provides a single, unified entry point for all parsing operations.

```java
import io.github.ghtcd.ComponentParser;
import io.github.ghtcd.ParseResult;
import java.util.Optional;

// The parser intelligently handles both simple strings and PURLs.
Optional<ParseResult> result1 = ComponentParser.parse("my-awesome-lib-v1.2.0-alpha");
Optional<ParseResult> result2 = ComponentParser.parse("pkg:maven/org.apache.commons/commons-lang3@3.3.2#foo/bar");

// Example 1: Handling a simple component string
result1.ifPresent(res -> {
    res.getComponentVersion().ifPresent(cv -> {
        System.out.println("Simple Name: " + cv.name());     // "my-awesome-lib"
        System.out.println("Simple Version: " + cv.version()); // "v1.2.0-alpha"
    });
});

// Example 2: Handling a Package URL (PURL)
result2.ifPresent(res -> {
    res.getPackageURL().ifPresent(purl -> {
        System.out.println("PURL Type: " + purl.type());                   // "maven"
        System.out.println("PURL Namespace: " + purl.namespace().orElse(null)); // "org.apache.commons"
        System.out.println("PURL Name: " + purl.name());                   // "commons-lang3"
        System.out.println("PURL Version: " + purl.version().orElse(null));     // "3.3.2"
        System.out.println("PURL Subpath: " + purl.subpath().orElse(null));     // "foo/bar"
    });
});
```

## Contributing
Contributions are welcome! Please feel free to submit a pull request or create an issue.

## License
This project is licensed under the Apache License 2.0. See the LICENSE file for details.