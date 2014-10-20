Why android-garden?
----
Because android ideas germinate, grow and die here like trees in garden.

How to use?
----
Just add link to repository and dependency:
```
repositories {
    maven { url 'https://github.com/danikula/android-garden/raw/mvn-repo' }
}
...
compile ('com.danikula:android-garden:VERSION') {
    exclude group: 'com.google.android'
}
```
, where *VERSION* is version of library. See full list of versions [here](https://github.com/danikula/android-garden/tree/mvn-repo/com/danikula/android-garden).

Useful commands
----
- `gradlew test` run JUnit tests
- `mvn package` (from **library** directory) package jar
- `mvn clean deploy` deploy to github repo. Credentials for repo should exist in `~/.m2/settings.xml`. 