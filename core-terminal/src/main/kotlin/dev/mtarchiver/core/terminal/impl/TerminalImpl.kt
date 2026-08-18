package dev.mtarchiver.core.terminal.impl

import dev.mtarchiver.core.terminal.api.Terminal
import dev.mtarchiver.core.terminal.api.CommandResult
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.io.BufferedReader
import java.io.InputStreamReader

@Singleton
class TerminalImpl @Inject constructor() : Terminal {

    private var process: Process? = null
    private var isActive = false

    override suspend fun initialize(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                isActive = true
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    override suspend fun executeCommand(command: String): CommandResult {
        return withContext(Dispatchers.IO) {
            try {
                val process = Runtime.getRuntime().exec(command)
                val output = BufferedReader(InputStreamReader(process.inputStream)).use { it.readText() }
                val error = BufferedReader(InputStreamReader(process.errorStream)).use { it.readText() }
                val exitCode = process.waitFor()

                CommandResult(
                    exitCode = exitCode,
                    output = output,
                    error = error
                )
            } catch (e: Exception) {
                CommandResult(
                    exitCode = -1,
                    output = "",
                    error = e.message ?: "Unknown error"
                )
            }
        }
    }

    override suspend fun writeInput(input: String) {
        withContext(Dispatchers.IO) {
            try {
                process?.outputStream?.write(input.toByteArray())
                process?.outputStream?.flush()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun readOutput(): Flow<String> {
        return flowOf("Terminal output stream")
    }

    override suspend fun close() {
        withContext(Dispatchers.IO) {
            try {
                process?.destroy()
                isActive = false
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun isActive(): Boolean {
        return isActive
    }
}
