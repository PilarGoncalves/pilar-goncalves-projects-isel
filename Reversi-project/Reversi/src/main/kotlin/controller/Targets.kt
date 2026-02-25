package controller

/**
 * The "targets" command.
 * This command controls the visualization of valid moves on the board by toggling target markers.
 * - `targets on`  → enables target markers for the current player.
 * - `targets off` → disables target markers.
 * - `targets`     → shows the current state of target markers without changing it.
 */
class Targets : Command<CommandContext.WithGame> {

    /**
     * Executes the "targets" command, either enabling or disabling the visualization of valid moves on the board.
     * @param context The current context, which contains the game state.
     * @param params The parameters passed to the command, which may specify the action ("on", "off", or none).
     * @return A [CommandResult.Success] containing the updated game state and an optional message.
     */
    override fun execute(context: CommandContext.WithGame, params: List<String>): CommandResult {
        val game = context.game
        val arg = params.getOrNull(0)?.lowercase()
        return when (arg) {
            "on" -> {
                val newGame = game.setTargets(true)
                Utils.getPlayerInfo(newGame)?.let { println(it) }
                CommandResult.Success(newGame, display = true, message = "")
            }
            "off" -> {
                val newGame = game.setTargets(false)
                Utils.getPlayerInfo(newGame)?.let { println(it) }
                CommandResult.Success(newGame, display = true, message = "")
            }
            else -> {
                val status = if (game.isTargetsOn()) "ON" else "OFF"
                CommandResult.Success(game, display = false, message = "Targets are $status")
            }
        }
    }

    /**
     * Provides a help message for the "targets" command.
     * @return A string detailing the usage and purpose of the command.
     */
    override fun help() = "Usage: TARGETS [ON|OFF]\nDescription: Toggles valid move visualization. No arg shows state."
}