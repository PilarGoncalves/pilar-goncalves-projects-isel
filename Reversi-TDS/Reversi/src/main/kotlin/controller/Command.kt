package controller

import model.Game

/**
 * Represents a command that can be executed by the application.
 * This interface defines the structure for all commands, allowing them to execute and provide feedback.
 */
sealed interface Command<in T : CommandContext> {

    /**
     * Executes the command with the provided context and parameters.
     * @param context The current context, containing the state information required to execute the command.
     * @param params The parameters passed to the command, typically from user input.
     * @return The result of the command execution, which could either be a successful execution or a command to exit the application.
     * @throws CommandException.InvalidParameters If the command cannot be executed due to invalid parameters.
     */
    fun execute(context: T, params: List<String>): CommandResult

    /**
     * Provides a help message describing the usage and purpose of the command.
     * @return A string that details the usage instructions for the command.
     */
    fun help(): String
}

/**
 * Represents the result of executing a command.
 * It can either be a successful execution, including a game state update, or an exit signal to terminate the application.
 */
sealed interface CommandResult {

    /**
     * Represents the exit command, which terminates the application.
     */
    object Exit : CommandResult

    /**
     * Represents the successful execution of a command.
     * It includes the updated game state, whether the board should be displayed, and an optional message.
     */
    data class Success(
        val game: Game,
        val display: Boolean = true,
        val message: String = ""
    ) : CommandResult
}

/**
 * Represents the context in which a command is executed.
 * This context can either be empty (when no game is active) or contain the current game state.
 */
sealed interface CommandContext {

    /**
     * Represents an empty context, which is used when no game is active.
     */
    object Empty : CommandContext

    /**
     * Represents the context when a game is in progress.
     * It contains the current game state, which can be used by the command to interact with the game.
     */
    data class WithGame(val game: Game) : CommandContext
}
