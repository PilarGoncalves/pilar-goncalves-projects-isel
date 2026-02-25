package tds.reversi.reversicompose.domain

import tds.reversi.reversicompose.storage.Storage
import tds.reversi.reversicompose.storage.GameState
import tds.reversi.reversicompose.model.Game

/**
 * The "play" command.
 * Executes a move on the board at the specified cell if the move is valid.
 * This command ensures the player's move is valid before applying it to the game.
 * It handles the validation of the move, updates the game state, and returns a success result.
 */
class Play(private val storage: Storage<String, GameState>) : Command<CommandContext.WithGame> {

    /**
     * Executes the "play" command, placing a piece on the board at the specified cell.
     *
     * This method checks whether it is the current player's turn, validates the move, and then applies the move to the game.
     * It updates the game state after the move and checks whether the game has ended, providing the appropriate message.
     *
     * @param context The current context, which contains the game state.
     *               This context is required to access the active game and validate the move.
     * @param params The parameters passed to the command, which should include the cell where the move will be played.
     *               The cell is provided as a string (e.g., "A1", "B3") and is parsed into row and column indices.
     * @return A [CommandResult.Success] containing the updated game state and an optional message.
     *         If the game ends, the winner message is returned; otherwise, an empty string is returned.
     * @throws CommandException.InvalidParameters if the move is invalid or if the player tries to play out of turn.
     *         This exception is thrown if the player is not allowed to play or the selected position is invalid.
     */
    override fun execute(context: CommandContext.WithGame, params: List<String>): CommandResult {
        val game = context.game
        if (Utils.notMyTurn(game)) {
            throw CommandException.InvalidParameters(this, "Not your turn")
        }
        val cell = params[0].uppercase()
        val colStart = 'A'

        val row = cell[0].digitToInt() - 1
        val col = cell[1] - colStart

        if (!game.board.isEmpty(row, col)) {
            throw CommandException.InvalidParameters(this, "Position $cell is not empty")
        }

        if (!game.board.isValidMove(row, col, game.currentPlayer)) {
            throw CommandException.InvalidParameters(this, "Position $cell not playable")
        }

        val newGame = Game.updateFromMove(game, row, col) ?: throw CommandException.InvalidParameters(this, "Invalid move execution")
        Utils.saveGame(newGame, storage)
        val message = if (newGame.isGameOver()) newGame.getWinnerMessage() else ""
        return CommandResult.Success(newGame, display = true, message = message)
    }

    /**
     * Provides a help message for the "play" command.
     *
     * This method returns a string that explains the command usage, including the format for specifying the cell to play.
     * The help message informs the user how to enter the cell coordinates and the expected command behavior.
     *
     * @return A string detailing the usage and purpose of the "play" command.
     */
    override fun help() = "Usage: PLAY <cell>\nDescription: Places a piece at the specified cell if valid."
}
