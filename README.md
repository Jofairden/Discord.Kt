# Discord.Kt [![Discord](https://img.shields.io/discord/663780943609331733.svg?style=flat-square)](https://discord.gg/RkBVCmy)
A Discord library written in Kotlin for Kotlin. The purpose of the library is to be easily able to create a Discord bot in Kotlin with minimal amount of code using Kotlin DSL.

## Support status
This lib will likely only ever work on a JVM
*   JVM: ✓
*   JS: ❌
*   Native: ❌

# How to use
The library is not yet released.
To use it you must pull the repository and locally install it.

# Basic example
Ping-Pong
```kotlin
fun main() {
    DiscordClient.buildAndRun(getBotToken()) {
        onMessageCreate { msg ->
            if (msg.content == ".ping") {
                with(msg) {
                    reply("pong!")
                }
            }
        }
    }
}
```
Ping-Pong with message update
```kotlin
fun main() {
    DiscordClient.buildAndRun(getBotToken()) {
        onMessageCreate { msg ->
            if (msg.author.id == botUser.id
                && msg.content == "pong!"
            ) {
                with(msg) {
                    edit(
                        EditMessageBody(
                            "pong! (${Date().time - msg.timestamp.time} ms)"
                        )
                    )
                }
            } else if (msg.content == ".ping") {
                with(msg) {
                    reply("pong!")
                }
            }
        }
    }
}
```

# Feature set
Current lib features: (list incomplete)
- [x] Connect with Discord
- [x] Reply to messages
- [x] Send embeds
- [x] Hook into any discord event (excluding voice events)
- [ ] Change bot details (name, presence etc)

# Dev task list
List of things completed, and things yet to be done.
(The list may not accurately represent the current state of the lib)
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
- [ ] Command module registration

# Libraries
The listed libraries may not be final

| Library       | Version       | License       |
| ------------- | ------------- | ------------- |
| OkHttp  | 4.3.0 | Apache 2.0 |
| Jackson  | 2.10.2| Apache 2.0 |
| Retrofit | 2.7.1 | Apache 2.0 |
| Kotlin-Logging | 1.7.7 | Apache 2.0 |
| Caffeine | 2.8.0 | Apache 2.0 |

# How to get in touch

If you would like to receive help with the lib or help develop the lib, it's best to join our [Discord server](https://discord.gg/RkBVCmy). Discord is similar to IRC chats. To use Discord you will need to register a new account on [their website](https://discordapp.com/).

# License
[Apache2.0 License](https://github.com/Jofairden/Discord.Kt/blob/master/LICENSE)
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

