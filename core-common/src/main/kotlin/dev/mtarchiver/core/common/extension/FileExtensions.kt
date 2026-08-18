package dev.mtarchiver.core.common.extension

import java.io.File
import kotlin.math.ln
import kotlin.math.pow

fun File.getReadableFileSize(): String {
    if (!exists()) return "0 B"
    val bytes = length()
    if (bytes <= 0) return "0 B"
    val k = 1024.0
    val sizes = arrayOf("B", "KB", "MB", "GB", "TB")
    val i = (ln(bytes.toDouble()) / ln(k)).toInt()
    return (bytes / k.pow(i.toDouble())).toInt().toString() + " " + sizes[i]
}

fun File.isArchiveFile(): Boolean {
    val archiveExtensions = listOf("zip", "7z", "tar", "gz", "bz2", "xz", "rar", "iso", "mta", "z")
    return archiveExtensions.contains(extension.lowercase())
}

fun File.deleteRecursively(): Boolean {
    if (!exists()) return true
    return if (isDirectory) {
        listFiles()?.all { it.deleteRecursively() } == true && delete()
    } else {
        delete()
    }
}

fun File.countFiles(): Int {
    if (!isDirectory) return 0
    var count = 0
    listFiles()?.forEach { file ->
        if (file.isDirectory) {
            count += file.countFiles()
        } else {
            count++
        }
    }
    return count
}
