package net.accelf.contral.epgstation.util

import kotlin.reflect.KProperty

class Reference<T : Any> {

    private lateinit var target: T

    operator fun getValue(thisRef: Any, property: KProperty<*>) = target

    operator fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        target = value
    }
}
