package controller

import storage.Storage
import storage.GameState

/**
 * The "pass" command.
 * This command allows a player to pass their turn to the opponent if no valid moves are available.
 * The command ensures that the player can only pass when no valid moves are possible.
 */
class Pass(private val storage: Storage<String, GameState>) : Command<CommandContext.WithGame> {

    /**
     * Executes the "pass" command, passing the turn to the opponent if no valid moves are available.
     * @param context The current context, which contains the game state.
     * @param params The parameters passed to the command (not used in this case).
     * @return A [CommandResult.Success] containing the updated game state and an optional message.
     * @throws CommandException.InvalidParameters if it is not the player's turn, or they have a valid move to make.
     */
    override fun execute(context: CommandContext.WithGame, params: List<String>): CommandResult {
        val game = context.game
        if (Utils.notMyTurn(game)) {
            throw CommandException.InvalidParameters(this, "Not your turn")
        }
        if (!game.canPass()) {
            val validMoves = game.board.getValidMoves(game.currentPlayer)
            val exampleMove = validMoves.first()
            val exampleCell = game.formatCell(exampleMove.first, exampleMove.second)
            throw CommandException.InvalidParameters(this, "Can't pass. At least can play at $exampleCell")
        }
        val pair = game.passTurn()
        val newGame = pair.first
        val endedByTwoPasses = pair.second
        Utils.getPlayerInfo(newGame)?.let { println(it) }
        Utils.saveGame(newGame, storage)
        val message = if (endedByTwoPasses) newGame.getWinnerMessage() else "Turn passed"
        return CommandResult.Success(newGame, display = true, message = message)
    }

    /**
     * Provides a help message for the "pass" command.
     * @return A string detailing the usage and purpose of the command.
     */
    override fun help() = "Usage: PASS\nDescription: Passes the turn if no valid moves."
}