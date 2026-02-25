package tds.reversi.reversicompose.domain

import tds.reversi.reversicompose.storage.Storage
import tds.reversi.reversicompose.storage.GameState

/**
 * The "pass" command.
 * This command allows a player to pass their turn to the opponent if no valid moves are available.
 * The command ensures that the player can only pass when no valid moves are possible, maintaining the integrity of the game flow.
 * If a player cannot make a move, they are allowed to pass, and the game proceeds to the next turn.
 */
class Pass(private val storage: Storage<String, GameState>) : Command<CommandContext.WithGame> {

    /**
     * Executes the "pass" command, passing the turn to the opponent if no valid moves are available.
     *
     * This method checks if it is the player's turn and if they have no valid moves left to make.
     * If both conditions are satisfied, the turn is passed to the opponent, and the game state is updated accordingly.
     *
     * @param context The current context, which contains the game state.
     *               This is required as the command needs to modify the active game.
     * @param params The parameters passed to the command (not used in this case).
     *               No parameters are needed for the "pass" command.
     * @return A [CommandResult.Success] containing the updated game state and an optional message.
     *         The success result includes the updated game state, whether the board should be displayed,
     *         and a message such as "Turn passed" or the game over message if the game ends.
     * @throws CommandException.InvalidParameters if it is not the player's turn, or if they still have valid moves to make.
     *         This exception is thrown if the player tries to pass when they are not allowed.
     */
    override fun execute(context: CommandContext.WithGame, params: List<String>): CommandResult {

        val game = context.game

        if (Utils.notMyTurn(game)) {
            throw CommandException.InvalidParameters(this, "Not your turn")
        }

        if (!game.canPass()) {
            throw CommandException.InvalidParameters(this, "You have valid moves. You cannot pass")
        }

        val newGame = game.passTurn()
        Utils.saveGame(newGame, storage)

        val message = if (newGame.isGameOver()) newGame.getWinnerMessage() else "Turn passed"
        return CommandResult.Success(game = newGame, display = true, message = message)
    }

    /**
     * Provides a help message for the "pass" command.
     *
     * This method returns a string that describes how to use the "pass" command and its functionality.
     * The help message informs the user that they can pass their turn if they have no valid moves available.
     *
     * @return A string detailing the usage and purpose of the "pass" command.
     */
    override fun help() = "Usage: PASS\nDescription: Passes the turn if no valid moves."
}