package tds.reversi.reversicompose.domain

/**
 * Base class for exceptions that occur during the execution of a command.
 * This class is used to represent errors that occur when executing a command, such as invalid parameters or unrecognized commands.
 * It extends the standard `Exception` class, allowing it to be thrown and caught like any other exception.
 */
sealed class CommandException(message: String) : Exception(message) {

    /**
     * Represents an error that occurs when the command cannot be executed due to invalid parameters.
     * This might occur when parameters provided to the command are missing, incorrect, or fail validation.
     *
     * @param command The command that caused the error.
     *               This provides context on which command was being executed when the exception occurred.
     * @param message An optional message providing additional details about the error.
     *               This can be used to give a more specific explanation about why the parameters were invalid.
     */
    class InvalidParameters(val command: Command<*>, message: String = "") : CommandException(message)
}
