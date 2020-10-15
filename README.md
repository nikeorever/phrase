Phrase
===========
A Gradle plugin which generates compile-safe format string builders.

The reason why I created this library
=====================================
Since [Paraphrase](https://github.com/JakeWharton/paraphrase) has been in a state of **experimental**, 
and the project is too old, it seems difficult to maintain, so I created this library. 

The goal of this library is consistent with [Paraphrase](https://github.com/JakeWharton/paraphrase)

Sample
=====

**Do** write your format strings like this (**Support Comment**):
```xml
<!--This is a comment of greeting string-->
<string name="greeting">Hello, {other_name}! My name is {my_name}.</string>
```

Execute gradle task 'generate{**variant**}PhraseClasses'
```shell script
./gradlew :{module}:generate{variant}PhraseClasses
```

After successful execution, Gradle Plugin will generate a Kotlin File named **Phrase.kt** for you
```kotlin
// Automatically generated file. DO NOT MODIFY
package cn.nikeo.reparaturapplication

import android.content.Context
import cn.nikeo.phrase.runtime.AbstractPhrase
import kotlin.CharSequence
import kotlin.Suppress

@Suppress("ClassName")
class Phrase_greeting(
  context: Context
) : BasePhrase(context = context, stringResId = R.string.greeting) {
  fun my_name(value: CharSequence): Phrase_greeting {
    put("my_name", value)
    return this
  }

  fun other_name(value: CharSequence): Phrase_greeting {
    put("other_name", value)
    return this
  }
}

/**
 * This is a comment of greeting string
 *
 * The string resource corresponding to [greeting] is in
 * /home/xianxueliang/AndroidStudioProjects/ReparaturApplication/app/src/main/res/values/strings.xml
 */
fun greeting(context: Context): Phrase_greeting = Phrase_greeting(context)
```

**Do** enjoy formatting them like this:
```kotlin
text.text = greeting(this)
    .my_name("Jake Wharton")
    .other_name("GitHub user")
    .format()
```
Or
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
        classpath "cn.nikeo.phrase:gradle-plugin:1.1.2"
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

