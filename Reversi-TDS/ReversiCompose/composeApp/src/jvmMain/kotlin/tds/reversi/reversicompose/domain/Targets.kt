package tds.reversi.reversicompose.domain

/**
 * The "targets" command.
 * This command controls the visualization of valid moves on the board by toggling target markers.
 *
 * The target markers indicate where a player can place their piece on the board:
 * - `targets on`  → enables target markers for the current player, showing valid move positions.
 * - `targets off` → disables target markers, hiding the valid move indicators.
 * - `targets`     → shows the current state of target markers without changing it (useful to see the current status).
 */
class Targets : Command<CommandContext.WithGame> {

    /**
     * Executes the "targets" command, either enabling or disabling the visualization of valid moves on the board.
     *
     * This method checks the provided parameters to determine whether to turn on or off the visualization of valid moves.
     * It updates the game state accordingly and returns the updated state with an appropriate message.
     *
     * @param context The current context, which contains the game state.
     *               This context is needed to access and modify the current game state.
     * @param params The parameters passed to the command, which may specify the action ("on", "off", or none).
     *               The first parameter determines whether to enable or disable the target markers.
     * @return A [CommandResult.Success] containing the updated game state and an optional message.
     *         The success result includes the updated game state, whether the board should be displayed,
     *         and a message indicating whether the targets are turned on or off.
     * @throws CommandException.InvalidParameters if the parameters are missing, invalid, or incorrectly formatted.
     *         This exception is thrown if the user does not provide valid arguments or the command is malformed.
     */
    override fun execute(context: CommandContext.WithGame, params: List<String>): CommandResult {

        val game = context.game

        val arg = params.getOrNull(0)?.trim()?.lowercase()
            ?: throw CommandException.InvalidParameters(this, "Missing option. Use TARGETS ON or TARGETS OFF")

        if (params.size != 1)
            throw CommandException.InvalidParameters(this, "Use TARGETS ON or TARGETS OFF")

        return when (arg) {
            "on" -> {
                val newGame = game.setTargets(true)
                CommandResult.Success(newGame, display = true, message = "Targets ON")
            }
            "off" -> {
                val newGame = game.setTargets(false)
                CommandResult.Success(newGame, display = true, message = "Targets OFF")
            }
            else -> throw CommandException.InvalidParameters(this, "Invalid option '$arg'. Use ON or OFF")
        }
    }

    /**
     * Provides a help message for the "targets" command.
     *
     * This method returns a string that explains how to use the command and its functionality.
     * The help message informs the user that they can toggle the display of valid move markers with the `on` and `off` options.
     *
     * @return A string detailing the usage and purpose of the "targets" command.
     */
    override fun help() = "Usage: TARGETS [ON|OFF]\nDescription: Toggles valid move visualization. No arg shows state."
}