package com.scrubberai.utils.extension

import java.text.CharacterIterator
import java.text.StringCharacterIterator

fun Long.toReadableFileSize(): String {
    if (this <= 0) return "0 B"
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    val digitGroups = (Math.log10(this.toDouble()) / Math.log10(1024.0)).toInt()
    return String.format("%.1f %s", this / Math.pow(1024.0, digitGroups.toDouble()), units[digitGroups])
}

fun Long.formatAsPercentage(): String = String.format("%.1f%%", this / 100.0)
