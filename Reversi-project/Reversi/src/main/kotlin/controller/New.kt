package controller

import model.*
import storage.Storage
import storage.GameState

/**
 * The "new" command.
 * Starts a new game with the specified player symbol and an optional game name.
 * The first player’s symbol is specified by the user, and an optional game name can be provided.
 */
class New(private val storage: Storage<String, GameState>) : Command<CommandContext.Empty> {

    /**
     * Executes the "new" command, creating a new game with the specified player symbol and name.
     * @param context The current context (empty in this case, as no game is active initially).
     * @param params The parameters passed to the command, including the player symbol and optional game name.
     * @return A [CommandResult.Success] containing the new game state and an optional message.
     * @throws CommandException.InvalidParameters if the player symbol is missing, invalid, or the game name already exists.
     */
    override fun execute(context: CommandContext.Empty, params: List<String>): CommandResult {
        if (params.isEmpty()) {
            throw CommandException.InvalidParameters(this, "Missing argument\nUse: NEW (${BLACK_SYMBOL}|${WHITE_SYMBOL}) [<name>]")
        }
        val playerSym = params[0].single()
        val player = try {
            Player.fromSymbol(playerSym)
        } catch (e: IllegalArgumentException) {
            throw CommandException.InvalidParameters(this, "Invalid player ${params[0]}\nUse: NEW ($BLACK_SYMBOL|$WHITE_SYMBOL) [<name>]")
        }
        val nameChosen = params.getOrNull(1) ?: ""
        if (nameChosen.isNotEmpty() && storage.read(nameChosen) != null) {
            throw CommandException.InvalidParameters(this, "Game $nameChosen already exists")
        }
        val game = Game.createInitial(name = nameChosen, myPlayer = player)
        if (nameChosen.isNotEmpty()) {
            storage.create(nameChosen, game.toGameState())
        }
        Utils.getPlayerInfo(game)?.let { println(it) }
        return CommandResult.Success(game, display = true, message = "")
    }

    /**
     * Provides a help message for the "new" command.
     * @return A string detailing the usage and purpose of the command.
     */
    override fun help() = "Usage: NEW (${BLACK_SYMBOL}|${WHITE_SYMBOL}) [<name>]\nDescription: Starts a new game. First player (${BLACK_SYMBOL} or ${WHITE_SYMBOL}) and optional name."
}