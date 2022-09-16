package net.accelf.contral.epgstation.util

internal fun Int.nonNegativeOrNull() = if (this < 0) null else this
