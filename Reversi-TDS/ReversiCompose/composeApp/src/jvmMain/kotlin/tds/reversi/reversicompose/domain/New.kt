package tds.reversi.reversicompose.domain

import tds.reversi.reversicompose.storage.Storage
import tds.reversi.reversicompose.storage.GameState
import tds.reversi.reversicompose.model.BLACK_SYMBOL
import tds.reversi.reversicompose.model.Game
import tds.reversi.reversicompose.model.Player
import tds.reversi.reversicompose.model.WHITE_SYMBOL

/**
 * The "new" command.
 * Starts a new game with the specified player symbol and an optional game name.
 * The first player’s symbol is specified by the user, and an optional game name can be provided.
 * This command will create a new game instance, allowing the first player to choose their symbol and start the game.
 */
class New(private val storage: Storage<String, GameState>) : Command<CommandContext.Empty> {

    /**
     * Executes the "new" command, creating a new game with the specified player symbol and name.
     *
     * This method checks if the player symbol is valid (either `#` or `@`), then creates a new game instance
     * with the provided symbol and optional game name. It also validates that the game name does not already exist
     * in the storage.
     *
     * @param context The current context (empty in this case, as no game is active initially).
     *               This context is passed as an argument to all command executions, even when not used.
     * @param params The parameters passed to the command, which should include the player symbol and an optional game name.
     *               The first parameter should be the symbol (`#` or `@`) representing the first player.
     *               The second parameter is the optional game name.
     * @return A [CommandResult.Success] containing the new game state and an optional message.
     *         The result indicates the success of creating the new game.
     * @throws CommandException.InvalidParameters if the player symbol is missing, invalid, or the game name already exists.
     *         This exception is thrown if the parameters are incorrect or if the game name already exists.
     */
    override fun execute(context: CommandContext.Empty, params: List<String>): CommandResult {

        val symStr = params.getOrNull(0)?.trim()
            ?: throw CommandException.InvalidParameters(this, "Missing player symbol")

        val playerSym = symStr.singleOrNull()
            ?: throw CommandException.InvalidParameters(this, "Player symbol must be a single character")

        val player = try {
            Player.fromSymbol(playerSym)
        } catch (_: IllegalArgumentException) {
            throw CommandException.InvalidParameters(this, "Invalid player symbol $playerSym")
        }

        val nameChosen = params.getOrNull(1)?.trim() ?: ""
        if (nameChosen.isNotEmpty() && storage.read(nameChosen) != null) {
            throw CommandException.InvalidParameters(this, "Game $nameChosen already exists")
        }
        val game = Game.createInitial(name = nameChosen, myPlayer = player)
        if (nameChosen.isNotEmpty()) {
            storage.create(nameChosen, game.toGameState())
        }
        return CommandResult.Success(game, display = true, message = "")
    }

    /**
     * Provides a help message for the "new" command.
     *
     * This method returns a string that explains the command usage, including the format for the player symbol and game name.
     *
     * @return A string with usage instructions and a brief description of the command.
     */
    override fun help() = "Usage: NEW (${BLACK_SYMBOL}|${WHITE_SYMBOL}) [<name>]\nDescription: Starts a new game. First player (${BLACK_SYMBOL} or ${WHITE_SYMBOL}) and optional name."
}