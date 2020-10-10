Phrase
===========
A Gradle plugin which generates compile-safe format string builders.

The reason why I created this library
=====================================
Since [Paraphrase](https://github.com/JakeWharton/paraphrase) has been in a state of **experimental**, 
and the project is too old, it seems difficult to maintain, so I created this library. 

The purpose of this library is the same as [Paraphrase](https://github.com/JakeWharton/paraphrase) 

Sample
=====

write your format strings like this:
```xml
<string name="greeting">Hello, {other_name}! My name is {my_name}.</string>
```

execute gradle task 'generateDebugPhraseClasses' for debug build type
```shell script
./gradlew :app:generateDebugPhraseClasses
```

enjoy formatting them like this:
```kotlin
text.text = greeting(this)
    .my_name("Jake Wharton")
    .other_name("GitHub user")
    .format()
```
or
```kotlin
greeting(this)
    .my_name("Jake Wharton")
    .other_name("GitHub user")
    .into(text)
```

Download
--------

#### Top-level build file
```groovy
buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath "cn.nikeo.phrase:gradle-plugin:1.0.0"
    }
}
```

#### App-module build file
```groovy
apply plugin: 'com.android.application'
apply plugin: "cn.nikeo.phrase"
```

#### Library-module build file
```groovy
apply plugin: 'com.android.library'
apply plugin: "cn.nikeo.phrase"
```
License
-------

Apache License, Version 2.0, ([LICENSE](https://github.com/nikeorever/phrase/blob/trunk/LICENSE) or [https://www.apache.org/licenses/LICENSE-2.0](https://www.apache.org/licenses/LICENSE-2.0))

