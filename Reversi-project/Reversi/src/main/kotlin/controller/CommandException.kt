package controller

/**
 * Base class for exceptions that occur during the execution of a command.
 * This class is used to represent errors that occur when executing a command, such as invalid parameters or unrecognized commands.
 */
sealed class CommandException(message: String) : Exception(message) {

    /**
     * Represents an error that occurs when the command cannot be executed due to invalid parameters.
     * This might occur when parameters provided to the command are missing or incorrect.
     *
     * @param command The command that caused the error.
     * @param message An optional message providing additional details about the error.
     */
    class InvalidParameters(val command: Command<*>, message: String = "") : CommandException(message)

    /**
     * Represents an error that occurs when a command is not recognized.
     * This might happen if the user enters an unknown command.
     *
     * @param message An optional message providing additional details about the error.
     */
    class Unknown(message: String = "") : CommandException(message)
}
