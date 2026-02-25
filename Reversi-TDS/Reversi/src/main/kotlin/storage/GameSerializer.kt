package storage

import model.Board
import model.EMPTY
import model.Player

/**
 * The GameSerializer class.
 * This class implements the Serializer interface to convert a [GameState] into a string representation and vice versa.
 * It handles the serialization and deserialization of game data, including the board and player information.
 */
class GameSerializer : Serializer<GameState> {

    /**
     * Serializes the [GameState] object into a string.
     * The string representation includes the creator's symbol, the current player's symbol, and the board grid.
     * @param data The [GameState] object to be serialized.
     * @return A string representation of the game state.
     */
    override fun serialize(data: GameState): String {
        val boardGrid = data.board.grid.joinToString("\n") { row ->
            row.joinToString(" ")
        }
        return "${data.creator.symbol}\n${data.currentPlayer.symbol}\n$boardGrid"
    }

    /**
     * Deserializes a string into a [GameState] object.
     * This method parses the string and reconstructs the game state, including the board and player information.
     * @param text The string representing the serialized game state.
     * @return The reconstructed [GameState] object, or null if the string format is invalid.
     */
    override fun deserialize(text: String): GameState? {
        val lines = text.trim().lines()
        if (lines.size < 3) return null

        val creatorSym = lines[0].singleOrNull() ?: return null
        val currentPlayerSym = lines[1].singleOrNull() ?: return null

        val creator = Player.fromSymbol(creatorSym)
        val currentPlayer = Player.fromSymbol(currentPlayerSym)

        val gridLines = lines.drop(2)
        val size = gridLines.size
        if (gridLines.any { it.split(" ").size != size }) return null

        val grid = gridLines.map { line ->
            line.split(" ").map { cell ->
                if (cell.isNotEmpty()) cell[0] else EMPTY
            }
        }

        val board = Board.createFromGrid(size, grid) ?: return null
        return GameState(board, currentPlayer, creator)
    }
}