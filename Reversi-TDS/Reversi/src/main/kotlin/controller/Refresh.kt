package controller

import model.*
import storage.Storage
import storage.GameState

/**
 * The "refresh" command.
 * This command updates the game state based on the opponent's move.
 * Typically used in multiplayer games to synchronize the local game state with the opponent's actions.
 */
class Refresh(private val storage: Storage<String, GameState>) : Command<CommandContext.WithGame> {

    /**
     * Executes the "refresh" command, updating the game state based on the opponent's move.
     * @param context The current context, which contains the game state.
     * @param params The parameters passed to the command (not used in this case).
     * @return A [CommandResult.Success] containing the updated game state and an optional message.
     * @throws CommandException.InvalidParameters if the game is local or if the game state cannot be found.
     */
    override fun execute(context: CommandContext.WithGame, params: List<String>): CommandResult {
        val game = context.game
        if (Utils.isLocal(game)) {
            throw CommandException.InvalidParameters(this, "Nothing to refresh")
        }

        val state = storage.read(game.name) ?: throw CommandException.InvalidParameters(this, "Game not found")

        if (state.board.grid == game.board.grid && state.currentPlayer == game.currentPlayer) {
            val message = if (game.currentPlayer == game.myPlayer) "It is your turn" else "No changes"
            return CommandResult.Success(game, display = false, message = message)
        }

        val wasOpponentPass = (state.board.grid == game.board.grid) && (state.currentPlayer != game.currentPlayer)
        val newGame = Game.fromState(
            name = game.name,
            myPlayer = game.myPlayer,
            state = state,
            showTargets = game.showTargets,
            lastTurnWasPass = wasOpponentPass
        )
        Utils.getPlayerInfo(newGame)?.let { println(it) }

        val message = if (newGame.isGameOver()) newGame.getWinnerMessage() else ""
        return CommandResult.Success(newGame, display = true, message = message)
    }

    /**
     * Provides a help message for the "refresh" command.
     * @return A string detailing the usage and purpose of the command.
     */
    override fun help() = "Usage: REFRESH\nDescription: Updates with opponent's move if any."
}