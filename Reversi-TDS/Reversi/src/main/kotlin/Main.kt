import controller.*
import storage.TextFileStorage
import storage.GameSerializer
import storage.Storage
import storage.GameState
import view.display

/**
 * Supported commands for the Reversi game application.
 * - new <#|@> [<name>]: Starts a new game. Argument: First player to play (Black pieces = # or White pieces = @), and optionally the game name.
 *   If a game name is provided, the opponent joins using the "join" command. If no name is given, the game is local.
 * - join <name>: Joins an existing game. Argument: Game <name> created by the "new" command in another instance.
 * - play <cell>: Makes a move. Argument: Position <cell> where the piece will be placed.
 * - pass: Passes the turn to the opponent if no move is possible. The game ends if the opponent also passes.
 * - refresh: Updates the game state with the opponent's move.
 * - targets [ON|OFF]: Controls the display of valid move positions. Argument: ON or OFF to toggle visibility, or no argument to show the current state.
 * - show: Displays the current game state again.
 * - exit: Exits the application.
 */

fun main() {
    println("Welcome to the Reversi console application!")

    var context: CommandContext = CommandContext.Empty
    val storage: Storage<String, GameState> = TextFileStorage(GameSerializer())

    while (true) {
        print("> ")
        val input = readlnOrNull() ?: continue
        val trimmedInput = input.trim()
        if (trimmedInput.isBlank()) continue

        val parts = trimmedInput.split("\\s+".toRegex())
        val commandStr = parts[0].lowercase()
        val params = parts.drop(1)

        try {
            val command = commandStr.toCommand(storage)

            val result = when (commandStr) {
                "new", "join" -> {
                    val effectiveContext = CommandContext.Empty
                    command.execute(effectiveContext, params)
                }
                "play", "pass", "targets", "refresh", "show" -> {
                    if (context !is CommandContext.WithGame) {
                        throw CommandException.InvalidParameters(command, "Game not started")
                    }
                    command.execute(context, params)
                }
                "exit" -> command.execute(context, params)
                else -> command.execute(context, params)
            }

            when (result) {
                is CommandResult.Exit -> {
                    println("Bye!")
                    break
                }
                is CommandResult.Success -> {
                    context = CommandContext.WithGame(result.game)
                    if (result.display) {
                        display(result.game.board, result.game.showTargets, result.game.currentPlayer)
                    }
                    if (result.message.isNotEmpty()) {
                        println(result.message)
                    }
                }
            }
        } catch (e: CommandException.InvalidParameters) {
            val errorMsg = e.message?.ifBlank { e.command.help() } ?: e.command.help()
            println(errorMsg)
        } catch (e: CommandException.Unknown) {
            println("Invalid command ${parts[0].uppercase()}")
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }
}