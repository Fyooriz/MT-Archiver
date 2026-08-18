package dev.mtarchiver.core.editor.api

import java.io.File

/**
 * Code editor interface with LSP support.
 */
interface CodeEditor {
    /**
     * Open file for editing.
     */
    suspend fun openFile(file: File): Boolean

    /**
     * Save file.
     */
    suspend fun saveFile(): Boolean

    /**
     * Get current file content.
     */
    suspend fun getContent(): String

    /**
     * Set file content.
     */
    suspend fun setContent(content: String)

    /**
     * Get line at position.
     */
    suspend fun getLine(lineNumber: Int): String?

    /**
     * Get total line count.
     */
    suspend fun getLineCount(): Int

    /**
     * Find text.
     */
    suspend fun find(text: String, caseSensitive: Boolean = false): List<FindResult>

    /**
     * Replace text.
     */
    suspend fun replace(search: String, replace: String, all: Boolean = false): Int
}

data class FindResult(
    val lineNumber: Int,
    val column: Int,
    val text: String
)

/**
 * Git interface for version control.
 */
interface GitManager {
    /**
     * Initialize git repository.
     */
    suspend fun initRepository(directory: File): Boolean

    /**
     * Add files to staging.
     */
    suspend fun add(files: List<File>): Boolean

    /**
     * Commit changes.
     */
    suspend fun commit(message: String, author: String, email: String): Boolean

    /**
     * Push to remote.
     */
    suspend fun push(remote: String = "origin", branch: String = "main"): Boolean

    /**
     * Pull from remote.
     */
    suspend fun pull(remote: String = "origin", branch: String = "main"): Boolean

    /**
     * Get commit log.
     */
    suspend fun getLog(maxEntries: Int = 10): List<CommitInfo>

    /**
     * Get current branch.
     */
    suspend fun getCurrentBranch(): String
}

data class CommitInfo(
    val hash: String,
    val message: String,
    val author: String,
    val timestamp: Long,
    val changes: Int
)
