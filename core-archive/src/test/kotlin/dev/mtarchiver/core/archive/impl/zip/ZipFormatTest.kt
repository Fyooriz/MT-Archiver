package dev.mtarchiver.core.archive.impl.zip

import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import java.io.File
import java.nio.file.Files

class ZipFormatTest {
    @Test
    fun create_list_extract_roundtrip() = runBlocking {
        val tmpDir = Files.createTempDirectory("mta-test").toFile()
        val fileA = File(tmpDir, "a.txt").apply { writeText("hello") }
        val subDir = File(tmpDir, "sub/dir").apply { mkdirs() }
        val fileB = File(subDir, "b.txt").apply { writeText("world") }

        val archive = File(tmpDir, "out.zip")
        val zip = ZipFormat()

        val createRes = zip.create(listOf(fileA, subDir), archive, compressionLevel = 6, password = null, progressListener = null)
        assertTrue("create should succeed", createRes.success)

        val entries = zip.listEntries(archive)
        assertTrue("entries should include a.txt", entries.any { it.name.endsWith("a.txt") })

        val dest = File(tmpDir, "unpack")
        val extractRes = zip.extract(archive, dest, null, null)
        assertTrue("extract should succeed", extractRes.success)
        assertEquals("hello", File(dest, "a.txt").readText())
        assertEquals("world", File(dest, "sub/dir/b.txt").readText())
    }
}
