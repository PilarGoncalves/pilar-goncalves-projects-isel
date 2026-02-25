package tds.reversi.reversicompose.domain

import tds.reversi.reversicompose.model.Game

/**
 * Represents a command that can be executed by the application.
 * This interface defines the structure for all commands, allowing them to execute and provide feedback.
 * Each command must implement the `execute` and `help` methods.
 */
sealed interface Command<in T : CommandContext> {

    /**
     * Executes the command with the provided context and parameters.
     *
     * @param context The current context, containing the state information required to execute the command.
     *               For example, it could be the game state in progress.
     * @param params The parameters passed to the command, typically from user input.
     *               These are the specific arguments that influence the command's behavior.
     * @return The result of the command execution, which could either be a successful execution or a command to exit the application.
     * @throws CommandException.InvalidParameters If the command cannot be executed due to invalid parameters.
     *         This exception is thrown when something goes wrong, such as missing or invalid data.
     */
    fun execute(context: T, params: List<String>): CommandResult

    /**
     * Provides a help message describing the usage and purpose of the command.
     * This method helps users understand how to properly invoke the command.
     *
     * @return A string that details the usage instructions for the command.
     *         This should include information about the required parameters and any important notes.
     */
    fun help(): String
}

/**
 * Represents the result of executing a command.
 * The result can either be a successful execution, including a game state update, or an exit signal to terminate the application.
 */
sealed interface CommandResult {

    /**
     * Represents the exit command, which terminates the application.
     * This command is used when the user requests to exit or quit the application.
     */
    object Exit : CommandResult

    /**
     * Represents the successful execution of a command.
     * It includes the updated game state, whether the board should be displayed, and an optional message.
     *
     * @property game The updated game state after the command execution.
     * @property display A flag indicating whether the board should be displayed. Defaults to `true` if the board needs to be shown.
     * @property message An optional message that can provide feedback to the user. This can be used to display a status update or result.
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
     * This can occur at the start or when no game is currently in progress.
     */
    object Empty : CommandContext

    /**
     * Represents the context when a game is in progress.
     * It contains the current game state, which can be used by the command to interact with the game.
     *
     * @param game The current game instance that is in progress.
     *             This is required for commands that manipulate or query the game.
     */
    data class WithGame(val game: Game) : CommandContext
}
