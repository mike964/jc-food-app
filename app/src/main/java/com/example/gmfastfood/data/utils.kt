package com.example.gmfastfood.data


import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

import java.util.UUID


fun generateRandomId(length: Int ): String {
//    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    val allowedChars = ('A'..'Z')  + ('0'..'9')
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")

//    return UUID.randomUUID().toString()
}



@Composable
fun formatTimestamp(timestampMillis: Long, formatPattern:  String? ):String {
    // 1. Memoize the formatted string to avoid parsing on every recomposition
    val formattedDate = remember(timestampMillis) {
        val instant = Instant.ofEpochMilli(timestampMillis)
        val formatPattern = formatPattern ?: "dd MMM yyyy, hh:mm a"
        val formatter = DateTimeFormatter.ofPattern(formatPattern   , Locale.getDefault())

        // Convert to system default timezone and format
        instant.atZone(ZoneId.systemDefault()).format(formatter)
    }

    // 2. Display the formatted string in your UI
    return formattedDate
}