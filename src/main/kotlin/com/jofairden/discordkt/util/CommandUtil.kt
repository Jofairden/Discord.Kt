package com.jofairden.discordkt.util

import com.jofairden.discordkt.api.command.ChatCommand
import java.security.AccessController
import java.security.PrivilegedAction
import java.util.ArrayList
import java.util.ServiceLoader

/**
 * Method for locating available methods, using JDK [ServiceLoader]
 * facility, along with module-provided SPI.
 *
 *
 * Note that method does not do any caching, so calls should be considered
 * potentially expensive.
 *
 */
// TODO it doesn't work
fun findCommands(classLoader: ClassLoader? = null): List<ChatCommand>? {
    val modules = ArrayList<ChatCommand>()
    val loader = secureGetServiceLoader(
        ChatCommand::class.java,
        classLoader
    )
    for (module in loader) {
        modules.add(module)
    }
    return modules
}

private fun <T> secureGetServiceLoader(
    clazz: Class<T>,
    classLoader: ClassLoader?
): ServiceLoader<T> {
    System.getSecurityManager() ?: return if (classLoader == null) ServiceLoader.load(clazz) else ServiceLoader.load(
        clazz,
        classLoader
    )
    return AccessController.doPrivileged(PrivilegedAction {
        if (classLoader == null) ServiceLoader.load(clazz) else ServiceLoader.load(
            clazz,
            classLoader
        )
    })
}
