package dev.mtarchiver.core.terminal.api

import kotlinx.coroutines.flow.Flow

/**
 * Terminal emulator interface.
 */
interface Terminal {
    /**
     * Initialize terminal session.
     */
    suspend fun initialize(): Boolean

    /**
     * Execute shell command.
     */
    suspend fun executeCommand(command: String): CommandResult

    /**
     * Write input to terminal.
     */
    suspend fun writeInput(input: String)

    /**
     * Read terminal output stream.
     */
    fun readOutput(): Flow<String>

    /**
     * Close terminal session.
     */
    suspend fun close()

    /**
     * Check if terminal is active.
     */
    fun isActive(): Boolean
}

data class CommandResult(
    val exitCode: Int,
    val output: String,
    val error: String = ""
)

interface SSHTerminal : Terminal {
    /**
     * Connect to SSH server.
     */
    suspend fun connect(host: String, port: Int, username: String, password: String): Boolean

    /**
     * Disconnect from SSH server.
     */
    suspend fun disconnect()
}
