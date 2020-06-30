# Discord.Kt [![Discord](https://img.shields.io/discord/663780943609331733.svg?style=flat-square)](https://discord.gg/RkBVCmy) [![Publish release](https://github.com/Jofairden/Discord.Kt/workflows/Publish%20release/badge.svg)](https://github.com/Jofairden/Discord.Kt/actions?query=workflow%3A%22Publish+release%22+is%3Asuccess)
A Discord library written in Kotlin for Kotlin. The purpose of the library is to be easily able to create a Discord bot in Kotlin with minimal amount of code using Kotlin DSL.

## Support status
**This lib will likely only ever work on a JVM**
*   JVM: ✓
*   JS: ❌
*   Native: ❌

# How to use
The library is still in early alpha stage. Use at your own discretion.

### Repositories
```groovy
repositories {
    /**
     * Ensure you have at least these repositories
     */
    mavenCentral()
    jcenter()
    maven {
        name = "Github PKG"
        url = uri("https://maven.pkg.github.com/jofairden/discord.kt")
        credentials {
            /**
             * In order to access Github PKG, you need to enter your username and access token with the read:packages grant
             * To create a personal access token, go to: https://github.com/settings/tokens
             */
            username = System.getenv("MAVEN_PKG_GITHUB_ACTOR")
            password = System.getenv("MAVEN_PKG_GITHUB_TOKEN")
        }
    }
}
```
### Dependencies
### Gradle
(Bare minumum)
```groovy
dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.3.3")
    /**
     * Add Discord.Kt as a dependency
     */
    implementation("com.jofairden:discord.kt:LATEST_VERSION")
}
```
### Maven
Locate the final package in the [packages page](https://github.com/Jofairden/Discord.Kt/packages) on Github and follow the instructions.
**You still need to add the repository as shown in the gadle section.**

# Basic example
For current accurate examples, please see the [example bot](https://github.com/Jofairden/Discord.Kt-Example-Bot).

# Feature set
Current lib features: (list incomplete)
- [x] Connect with Discord
- [x] Send messages including embeds
- [x] Edit messages
- [x] Remove messages
- [x] Pin/unpin messages
- [x] Ban/unban users
- [x] React to messages
- [x] Remove reactions
- [x] Add/delete/modify roles from guild
- [x] Change users' roles/nickname
- [x] Add/delete/modify channels
- [x] Add/delete guild invites
- [x] Prune members
- [x] Hook into any discord event (excluding voice events)
- [x] Change bot details (name, presence etc)

# Technical dev task list
List of things completed, and things yet to be done: (the list may not accurately represent the current state of the lib)
- [x] Connect with Discord & Send heartbeats
- [x] Handle connection resumption
- [x] Message Dispatcher
- [x] Event Dispatcher (high prio)
- [x] Event hooking (high prio)
- [ ] Sharding (low prio)
- [ ] Rate limiting (low prio)
- [x] WebSocket failure reporter
- [ ] Required permission checking (high prio)
- [x] Retrofit services for all resources
- [x] Storing resources (cache) (high prio) (not final)
- [x] Model classes for Guild, User, Role and all other associated resources
- [x] Command module registration
- [ ] Split generic classes into more specific classes (e.g. DiscordChannel -> GuildChannel -> GuildTextChannel etc.)

# Libraries
Discord.kt was built with the following libraries: (the listed libraries may not be final)

| Library       | Version       | License       |
| ------------- | ------------- | ------------- |
| [OkHttp](https://github.com/square/okhttp)  | 4.3.0 | Apache 2.0 |
| [Jackson](https://github.com/FasterXML/jackson)  | 2.10.2| Apache 2.0 |
| [Retrofit](https://github.com/square/retrofit) | 2.7.1 | Apache 2.0 |
| [Kotlin-Logging](https://github.com/MicroUtils/kotlin-logging) | 1.7.7 | Apache 2.0 |
| [Caffeine](https://github.com/ben-manes/caffeine) | 2.8.0 | Apache 2.0 |
| [Koin](https://github.com/InsertKoinIO/koin) | 2.0.1 | Apache 2.0 |

# How to get in touch

If you would like to receive help with the lib or help develop the lib, it's best to join our [Discord server](https://discord.gg/RkBVCmy). Discord is similar to IRC chats. To use Discord you will need to register a new account on [their website](https://discordapp.com/).

# License
[Apache 2.0 License](https://github.com/Jofairden/Discord.Kt/blob/master/LICENSE)
```
Copyright 2020 Daniël Zondervan

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

