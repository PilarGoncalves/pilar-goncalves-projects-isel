package tds.reversi.reversicompose.domain

import tds.reversi.reversicompose.storage.Storage
import tds.reversi.reversicompose.storage.GameState
import tds.reversi.reversicompose.model.Game

/**
 * The "refresh" command.
 * This command updates the game state based on the opponent's move.
 * Typically used in multiplayer games to synchronize the local game state with the opponent's actions.
 * The command checks if the game is multiplayer, ensures it's the opponent's turn,
 * and then fetches the latest game state from storage to synchronize.
 */
class Refresh(private val storage: Storage<String, GameState>) : Command<CommandContext.WithGame> {

    /**
     * Executes the "refresh" command, updating the game state based on the opponent's move.
     *
     * This method checks if the game is local or multiplayer, verifies that it is the opponent's turn,
     * and then reads the updated game state from storage to synchronize the local game state.
     * If the game is over, the result will include the winner's message.
     *
     * @param context The current context, which contains the game state.
     *               This is necessary for accessing the game that needs to be refreshed.
     * @param params The parameters passed to the command (not used in this case).
     *               No parameters are needed for the "refresh" command.
     * @return A [CommandResult.Success] containing the updated game state and an optional message.
     *         The success result includes the updated game state and an appropriate message,
     *         such as a winner message if the game is over.
     * @throws CommandException.InvalidParameters if the game is local, if it is not the opponent's turn,
     *                                            or if the game state cannot be found in storage.
     *         This exception is thrown if the command is executed in the wrong context or the game state is unavailable.
     */
    override fun execute(context: CommandContext.WithGame, params: List<String>): CommandResult {

        val game = context.game

        if (Utils.isLocal(game)) {
            throw CommandException.InvalidParameters(this, "Refresh is only available in multiplayer games")
        }

        if (!Utils.notMyTurn(game)) {
            throw CommandException.InvalidParameters(this, "You can only refresh on opponent's turn")
        }

        val state = storage.read(game.name)
            ?: throw CommandException.InvalidParameters(this, "Game not found")

        val newGame = Game.fromState(
            name = game.name,
            myPlayer = game.myPlayer,
            state = state,
            showTargets = game.showTargets
        )

        val message = if (newGame.isGameOver()) newGame.getWinnerMessage() else ""
        return CommandResult.Success(newGame, message = message)
    }

    /**
     * Provides a help message for the "refresh" command.
     *
     * This method returns a string that explains the correct usage of the command and its purpose.
     * The help message informs the user that the command is used to update the game state with the opponent's move.
     *
     * @return A string detailing the usage and purpose of the "refresh" command.
     */
    override fun help() = "Usage: REFRESH\nDescription: Updates with opponent's move if any."
}