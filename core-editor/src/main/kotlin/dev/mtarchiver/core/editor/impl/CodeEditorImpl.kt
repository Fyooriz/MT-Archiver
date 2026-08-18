package dev.mtarchiver.core.editor.impl

import dev.mtarchiver.core.editor.api.CodeEditor
import dev.mtarchiver.core.editor.api.FindResult
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

@Singleton
class CodeEditorImpl @Inject constructor() : CodeEditor {

    private var currentFile: File? = null
    private var content: String = ""

    override suspend fun openFile(file: File): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                if (file.exists() && file.isFile) {
                    currentFile = file
                    content = file.readText()
                    true
                } else {
                    false
                }
            } catch (e: Exception) {
                false
            }
        }
    }

    override suspend fun saveFile(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                currentFile?.writeText(content)
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    override suspend fun getContent(): String {
        return content
    }

    override suspend fun setContent(content: String) {
        this.content = content
    }

    override suspend fun getLine(lineNumber: Int): String? {
        return content.lines().getOrNull(lineNumber - 1)
    }

    override suspend fun getLineCount(): Int {
        return content.lines().size
    }

    override suspend fun find(text: String, caseSensitive: Boolean): List<FindResult> {
        return withContext(Dispatchers.Default) {
            val results = mutableListOf<FindResult>()
            val lines = content.lines()
            val searchText = if (caseSensitive) text else text.lowercase()

            lines.forEachIndexed { index, line ->
                val searchLine = if (caseSensitive) line else line.lowercase()
                var column = 0
                while (true) {
                    val pos = searchLine.indexOf(searchText, column)
                    if (pos == -1) break
                    results.add(
                        FindResult(
                            lineNumber = index + 1,
                            column = pos,
                            text = line.substring(pos, (pos + text.length).coerceAtMost(line.length))
                        )
                    )
                    column = pos + 1
                }
            }
            results
        }
    }

    override suspend fun replace(search: String, replace: String, all: Boolean): Int {
        return withContext(Dispatchers.Default) {
            val count = if (all) {
                content.split(search).size - 1
            } else {
                1
            }
            content = if (all) {
                content.replace(search, replace)
            } else {
                content.replaceFirst(search, replace)
            }
            count
        }
    }
}
