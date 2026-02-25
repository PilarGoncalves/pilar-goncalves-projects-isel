package controller

import model.*
import storage.Storage
import storage.GameState

/**
 * The "join" command.
 * Allows a player to join an existing game as the opponent.
 * The player provides the name of the game they want to join.
 */
class Join(private val storage: Storage<String, GameState>) : Command<CommandContext.Empty> {

    /**
     * Executes the "join" command, allowing the player to join an existing game.
     * @param context The current context (empty in this case, as no game is active initially).
     * @param params The parameters passed to the command, which must include the name of the game to join.
     * @return A [CommandResult.Success] containing the updated game state and an optional message.
     * @throws CommandException.InvalidParameters if the game name is missing or the game does not exist.
     */
    override fun execute(context: CommandContext.Empty, params: List<String>): CommandResult {
        if (params.isEmpty()) {
            throw CommandException.InvalidParameters(this, "Missing argument\nUse: JOIN <name>")
        }
        val name = params[0]
        val state = storage.read(name) ?: throw CommandException.InvalidParameters(this, "Game $name does not exist")
        val joiner = state.creator.other()
        val game = Game.fromState(name = name, myPlayer = joiner, state = state)
        Utils.getPlayerInfo(game)?.let { println(it) }
        return CommandResult.Success(game, display = true, message = "")
    }

    /**
     * Provides a help message for the "join" command.
     * @return A string detailing the usage and purpose of the command.
     */
    override fun help() = "Usage: JOIN <name>\nDescription: Joins an existing game as the opponent."
}