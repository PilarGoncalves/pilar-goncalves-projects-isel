package controller

/**
 * The "show" command.
 * Displays the current state of the game, including the board and player information.
 * This command is typically used to show the game status at any point during gameplay.
 */
class Show : Command<CommandContext.WithGame> {

    /**
     * Executes the "show" command, displaying the current game state.
     * @param context The current context, which contains the game state.
     * @param params The parameters passed to the command (not used in this case).
     * @return A [CommandResult.Success] containing the current game state and an optional message.
     */
    override fun execute(context: CommandContext.WithGame, params: List<String>): CommandResult {
        val game = context.game
        Utils.getPlayerInfo(game)?.let { println(it) }
        return CommandResult.Success(game, display = true, message = "")
    }

    /**
     * Provides a help message for the "show" command.
     * @return A string detailing the usage and purpose of the command.
     */
    override fun help() = "Usage: SHOW\nDescription: Shows the current game state."
}