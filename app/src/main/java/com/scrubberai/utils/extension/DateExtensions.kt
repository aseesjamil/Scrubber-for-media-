package com.scrubberai.utils.extension

import java.text.SimpleDateFormat
import java.util.*

fun Long.formatAsDate(): String {
    val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return formatter.format(Date(this))
}

fun Long.formatAsDateTime(): String {
    val formatter = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
    return formatter.format(Date(this))
}
