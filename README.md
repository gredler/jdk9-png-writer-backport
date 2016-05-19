# JDK9 PNG Writer Backport [![License](https://img.shields.io/badge/license-GPL2+CE-blue.svg)](http://openjdk.java.net/legal/gplv2+ce.html) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/net.gredler/jdk9-png-writer-backport/badge.svg)](https://maven-badges.herokuapp.com/maven-central/net.gredler/jdk9-png-writer-backport)

This is a backport of the standard ImageIO PNG writer available in Java 9, which includes
[a very important configurability and performance enhancement](https://bugs.openjdk.java.net/browse/JDK-6488522).
This library can be used on both Java 7 and Java 8.

Prior to Java 9, the ZLIB deflater used internally by PNGImageWriter always used the `BEST_COMPRESSION`
compression level, which tries to achieve optimal file sizes regardless of the performance penalty. This
behavior was not configurable.

In Java 9, the ZLIB deflater used by PNGImageWriter uses a more sane compression level default (4),
and allows the compression level to be customized with the standard `ImageWriteParam` compression
attributes.

This library contains and automatically registers this improved PNG writer with the ImageIO system,
directing the ImageIO service registry to prioritize it over the built-in standard PNG writer.

Many thanks to [Laurent Bourgès](https://github.com/bourgesl) for implementing this Java 9 enhancement
to begin with.

###Building

`gradlew check`: Compiles and runs all quality checks, including the unit tests.  
`gradlew jar`: Builds the JAR file.  
`gradlew uploadArchives`: Deploys to Maven Central (requires a modified gradle.properties file).  
