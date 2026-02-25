package controller

import model.*
import storage.Storage
import storage.GameState

/**
 * The "play" command.
 * Executes a move on the board at the specified cell if the move is valid.
 * This command ensures the player's move is valid before applying it to the game.
 */
class Play(private val storage: Storage<String, GameState>) : Command<CommandContext.WithGame> {

    /**
     * Executes the "play" command, placing a piece on the board at the specified cell.
     * @param context The current context, which contains the game state.
     * @param params The parameters passed to the command, which should include the cell where the move will be played.
     * @return A [CommandResult.Success] containing the updated game state and an optional message.
     * @throws CommandException.InvalidParameters if the move is invalid or if the player tries to play out of turn.
     */
    override fun execute(context: CommandContext.WithGame, params: List<String>): CommandResult {
        val game = context.game
        if (params.size != 1) {
            throw CommandException.InvalidParameters(this, "Missing argument\nUse: PLAY <cell>")
        }
        if (Utils.notMyTurn(game)) {
            throw CommandException.InvalidParameters(this, "Not your turn")
        }
        val cell = params[0].uppercase()
        val rowStart = '1'
        val colStart = 'A'
        val rowEnd = (rowStart.code + BOARD_SIDE - 1).toChar()
        val colEnd = (colStart.code + BOARD_SIDE - 1).toChar()

        if (cell.length != 2 || cell[0] !in rowStart..rowEnd || cell[1] !in colStart..colEnd) {
            throw CommandException.InvalidParameters(this, "Invalid cell $cell\nUse: PLAY <cell>")
        }

        val row = cell[0].digitToInt() - 1
        val col = cell[1] - colStart

        if (!game.board.isEmpty(row, col)) {
            throw CommandException.InvalidParameters(this, "Position $cell is not empty")
        }

        if (!game.board.isValidMove(row, col, game.currentPlayer)) {
            throw CommandException.InvalidParameters(this, "Position $cell not playable")
        }

        val newGame = Game.updateFromMove(game, row, col) ?: throw CommandException.InvalidParameters(this, "Invalid move execution")
        Utils.getPlayerInfo(newGame)?.let { println(it) }
        Utils.saveGame(newGame, storage)
        val message = if (newGame.isGameOver()) newGame.getWinnerMessage() else ""
        return CommandResult.Success(newGame, display = true, message = message)
    }

    /**
     * Provides a help message for the "play" command.
     * @return A string detailing the usage and purpose of the command.
     */
    override fun help() = "Usage: PLAY <cell>\nDescription: Places a piece at the specified cell if valid."
}
