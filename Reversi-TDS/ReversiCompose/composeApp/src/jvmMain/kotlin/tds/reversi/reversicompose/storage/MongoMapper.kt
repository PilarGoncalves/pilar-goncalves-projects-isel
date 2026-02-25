package tds.reversi.reversicompose.storage

import tds.reversi.reversicompose.model.Board
import tds.reversi.reversicompose.model.Player
import tds.reversi.reversicompose.model.EMPTY
import org.bson.Document

/**
 * MongoMapper class implements the [DocumentMapper] interface for `GameState`.
 * This class is responsible for mapping the `GameState` data object to and from MongoDB `Document` format.
 * It is used to serialize and deserialize the game state when storing or retrieving the data from MongoDB.
 */
class MongoMapper : DocumentMapper<GameState> {

    /**
     * Converts a `GameState` object into a MongoDB `Document`.
     *
     * This method serializes the properties of the `GameState` object into a `Document` format
     * that can be stored in MongoDB. The board is serialized as a list of strings, where each string represents
     * a row on the board.
     *
     * @param id The unique identifier for the game (usually the game name).
     * @param data The `GameState` object to be converted into a `Document`.
     * @return A `Document` representing the serialized game state, including the game properties.
     */
    override fun toDocument(id: String, data: GameState): Document {
        val boardAsStrings = data.board.grid.map { it.joinToString("") }

        return Document("_id", id)
            .append("creator", data.creator.symbol.toString())
            .append("currentPlayer", data.currentPlayer.symbol.toString())
            .append("lastTurnWasPass", data.lastTurnWasPass)
            .append("endedByTwoPasses", data.endedByTwoPasses)
            .append("moveCount", data.moveCount)
            .append("board", boardAsStrings)
    }

    /**
     * Converts a MongoDB `Document` into a `GameState` object.
     *
     * This method deserializes the `Document` retrieved from MongoDB into a `GameState` object.
     * It reads the values from the `Document` and converts them into the appropriate types for the `GameState` class.
     *
     * @param doc The MongoDB `Document` containing the game state data.
     * @return A `GameState` object constructed from the data in the `Document`, or `null` if the document is invalid.
     */
    override fun fromDocument(doc: Document): GameState? {
        val creatorSym = doc.getString("creator")?.singleOrNull() ?: return null
        val currentPlayerSym = doc.getString("currentPlayer")?.singleOrNull() ?: return null
        val lastTurnWasPass = doc.getBoolean("lastTurnWasPass") ?: false
        val endedByTwoPasses = doc.getBoolean("endedByTwoPasses") ?: false
        val moveCount = doc.getInteger("moveCount") ?: 0
        val boardLines = doc.getList("board", String::class.java) ?: return null

        val creator = Player.fromSymbol(creatorSym)
        val currentPlayer = Player.fromSymbol(currentPlayerSym)

        val size = boardLines.size
        val grid = boardLines.map { line ->
            line.map { ch -> if (ch == '.') EMPTY else ch }
        }

        val board = Board.createFromGrid(size, grid) ?: return null

        return GameState(board, currentPlayer, creator, lastTurnWasPass, endedByTwoPasses, moveCount)
    }
}
