# java-dotenv-revised

[![Release](https://jitpack.io/v/arhor/java-dotenv-revised.svg)](https://jitpack.io/#arhor/java-dotenv-revised)

This project strongly inspired by [dotenv-java][1] which is a great tool, and is practically the only existing library
that provides the ability to work with `.env` files conveniently.

However, there are several aspects that could be improved:

- reading of the `.env` files implemented via manual parsing, which could be considered as simple `.properties` allowing
  to use existing `java.util.Properties` instead of any manual work
- [dotenv-java][1] cannot resolve references between properties

In other words, instead of reimplementing bicycle and parsing `.env` file manually using regular expressions, etc., it's
loaded just like the usual `.properties` file. In my implementation, I tried to concentrate on the functionality that,
in my opinion, is exactly what is missing - resolving references.

Consider for example a file with the following content:

```properties
A=1
B=2
C=3
D=${A} ${B} ${C}
```

Trying to get property `D` will lead to value `1 2 3` be resolved.

## Installation

Here you can find the instructions for installing the library using various build systems.

### Gradle

1. Add Jitpack repository to your repositories list
    ```groovy
    repositories {
        // ...other repositories you use...
        maven { url 'https://jitpack.io' }
    }
    ```

2. Add library dependency to you dependencies list
    ```groovy
    dependencies {
        implementation 'com.github.arhor:java-dotenv-revised:0.1.5'
    }
    ```

### Maven

1. Add Jitpack repository to your repositories list
    ```xml
    <repositories>
        <!-- ...other repositories you use... -->
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>
    ```

2. Add library dependency to you dependencies list
    ```xml
    <dependency>
        <groupId>com.github.arhor</groupId>
        <artifactId>java-dotenv-revised</artifactId>
        <version>0.1.5</version>
    </dependency>
    ```

[1]: https://github.com/cdimascio/dotenv-java
