package dev.mtarchiver.core.common.extension

import java.io.File

fun String.toFile(): File = File(this)

fun String.isValidFileExtension(): Boolean {
    return this.isNotEmpty() && this.length <= 10 && !this.contains("/")
}

fun String.getFileExtension(): String {
    return if (this.contains(".")) {
        this.substringAfterLast(".")
    } else {
        ""
    }
}

fun String.removeFileExtension(): String {
    return if (this.contains(".")) {
        this.substringBeforeLast(".")
    } else {
        this
    }
}

fun String.formatFileSize(): String {
    return when {
        this.toLongOrNull() == null -> this
        this.toLong() < 1024 -> "${this} B"
        this.toLong() < 1024 * 1024 -> "${this.toLong() / 1024} KB"
        this.toLong() < 1024 * 1024 * 1024 -> "${this.toLong() / (1024 * 1024)} MB"
        else -> "${this.toLong() / (1024 * 1024 * 1024)} GB"
    }
}
