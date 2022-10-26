# java-dotenv-revised

This project strongly inspired by [dotenv-java][1] which is a great tool, and is practically the only existing library
that provides the ability to work with `.env` files conveniently.

However, there are several aspects that could be improved:

- reading of the `.env` files implemented via manual parsing, which could be considered as simple `.properties` allowing
  to use existing `java.util.Properties` instead of any manual work
- [dotenv-java][1] cannot resolve references between properties

To install the library add:

```gradle
repositories { 
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.arhor:java-dotenv-revised:0.1.1'
}
```

[1]: https://github.com/cdimascio/dotenv-java
