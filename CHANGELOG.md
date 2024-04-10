# 2.0.0 (April 10, 2024)

* Retargeted at Java 17 + Gradle 8
* No more reliance on the external BuildScripts repository to build this project
* Migrated to JUnit 5
* Added Spotbugs code coverage to the build
* Uses of `java.util.Random` have been replaced by `ThreadLocalRandom`

BREAKING CHANGES:

```java
PaymentCardGenerator.generateByPrefix(int howManyOfEachPrefix, List<Integer> lengths, Set<Long> prefixes)
```
now uses a `Set<Integer>` for `lengths` rather than `List<Integer>`
  

# 1.0.1 (May 8, 2021)

There are no code changes in this release.  The project has been updated to be compatible with the latest [BuildScripts](https://github.com/kloverde/BuildScripts) and Gradle 7.0.


# 1.0 (December 21, 2016)

* First release
