package tds.reversi.reversicompose.domain

import tds.reversi.reversicompose.storage.Storage
import tds.reversi.reversicompose.storage.GameState
import tds.reversi.reversicompose.model.Game

/**
 * The "join" command.
 * Allows a player to join an existing game as the opponent.
 * The player provides the name of the game they want to join.
 *
 * This command checks if the game exists, verifies that the game is in a joinable state,
 * and then joins the game as the opponent.
 */
class Join(private val storage: Storage<String, GameState>) : Command<CommandContext.Empty> {

    /**
     * Executes the "join" command, allowing the player to join an existing game.
     *
     * This method takes the game name from the provided parameters, checks if the game exists in storage,
     * and validates that the game is in a state where joining is allowed (only if the game has no more than one move played).
     * If the conditions are met, the player joins the game as the opponent and a new game instance is created.
     *
     * @param context The current context (empty in this case, as no game is active initially).
     *                This is used to hold any state necessary for the command execution.
     * @param params The parameters passed to the command, which must include the name of the game to join.
     *               The first parameter should be the game name.
     * @return A [CommandResult.Success] containing the updated game state and an optional message.
     *         The success result includes the updated game object with the joined player.
     * @throws CommandException.InvalidParameters If the game name is missing, empty, or if the game does not exist, or if it is not possible to join the game.
     */
    override fun execute(context: CommandContext.Empty, params: List<String>): CommandResult {

        val name = params.getOrNull(0)?.trim()
            ?: throw CommandException.InvalidParameters(this, "Missing game name")

        if (name.isEmpty())
            throw CommandException.InvalidParameters(this, "Game name cannot be empty")

        val state = storage.read(name) ?: throw CommandException.InvalidParameters(this, "Game $name does not exist")

        val canJoin = state.moveCount <= 1
            //state.moveCount == 0 || (state.moveCount == 1 && state.currentPlayer != state.creator)

        if (!canJoin)
            throw CommandException.InvalidParameters(this, "Can't join in game $name")

        val joiner = state.creator.other()
        val game = Game.fromState(name = name, myPlayer = joiner, state = state)
        return CommandResult.Success(game, display = true, message = "")
    }

    /**
     * Provides a help message for the "join" command.
     * This method returns a string detailing the correct usage of the command and its purpose.
     *
     * @return A string with usage instructions and a brief description of what the command does.
     */
    override fun help() = "Usage: JOIN <name>\nDescription: Joins an existing game as the opponent."
}