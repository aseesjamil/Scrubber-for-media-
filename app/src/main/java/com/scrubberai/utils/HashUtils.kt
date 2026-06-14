package com.scrubberai.utils

import android.content.Context
import java.io.*
import java.security.DigestInputStream
import java.security.MessageDigest

object HashUtils {

    fun sha256ForFile(input: InputStream): String {
        val digest = MessageDigest.getInstance("SHA-256")
        DigestInputStream(input, digest).use { dis ->
            val buffer = ByteArray(8192)
            while (dis.read(buffer) != -1) {
                // reading to update digest
            }
        }
        return digest.digest().joinToString("") { "%02x".format(it) }
    }

}
