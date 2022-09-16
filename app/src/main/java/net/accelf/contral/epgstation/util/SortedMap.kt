package net.accelf.contral.epgstation.util

import java.util.*

fun <K, V> SortedMap<K, V>.firstKeyOrNull() = runCatching { firstKey() }.getOrNull()
fun <K, V> SortedMap<K, V>.lastKeyOrNull() = runCatching { lastKey() }.getOrNull()
