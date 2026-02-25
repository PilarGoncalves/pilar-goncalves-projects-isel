package controller

/**
 * The "exit" command.
 * This command is responsible for terminating the application when executed.
 */
class Exit : Command<CommandContext> {

    /**
     * Executes the "exit" command, terminating the application.
     * @param context The current context in which the command is executed.
     * @param params The parameters passed to the command (not used in this case).
     * @return The result of the command execution, which is a signal to exit the application.
     */
    override fun execute(context: CommandContext, params: List<String>): CommandResult {
        return CommandResult.Exit
    }

    /**
     * Provides a help message for the "exit" command.
     * @return A string detailing the usage and purpose of the command.
     */
    override fun help() = "Usage: EXIT\nDescription: Exits the application."
}
