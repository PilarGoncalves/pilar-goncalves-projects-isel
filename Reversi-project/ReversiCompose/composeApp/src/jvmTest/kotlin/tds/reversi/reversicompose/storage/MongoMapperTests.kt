package tds.reversi.reversicompose.storage

import org.bson.Document
import tds.reversi.reversicompose.model.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class MongoMapperTests {

    private val sut = MongoMapper()

    @Test
    fun `toDocument writes all expected fields`() {
        val id = "game1"
        val state = GameState(
            board = Board.create(),
            currentPlayer = Player.BLACK,
            creator = Player.WHITE,
            lastTurnWasPass = true,
            endedByTwoPasses = false,
            moveCount = 7
        )

        val doc = sut.toDocument(id, state)

        assertEquals(id, doc.getString("_id"))
        assertEquals(state.creator.symbol.toString(), doc.getString("creator"))
        assertEquals(state.currentPlayer.symbol.toString(), doc.getString("currentPlayer"))
        assertEquals(true, doc.getBoolean("lastTurnWasPass"))
        assertEquals(false, doc.getBoolean("endedByTwoPasses"))
        assertEquals(7, doc.getInteger("moveCount"))

        val board = doc.getList("board", String::class.java)
        assertNotNull(board)
        assertEquals(state.board.size, board.size)
    }

    @Test
    fun `fromDocument returns GameState for valid document`() {
        val board = Board.create()
        val lines = board.grid.map { row -> row.joinToString("") }

        val doc = Document("_id", "g")
            .append("creator", Player.BLACK.symbol.toString())
            .append("currentPlayer", Player.WHITE.symbol.toString())
            .append("lastTurnWasPass", true)
            .append("endedByTwoPasses", true)
            .append("moveCount", 10)
            .append("board", lines)

        val state = sut.fromDocument(doc)
        assertNotNull(state)
        assertEquals(Player.BLACK, state.creator)
        assertEquals(Player.WHITE, state.currentPlayer)
        assertEquals(true, state.lastTurnWasPass)
        assertEquals(true, state.endedByTwoPasses)
        assertEquals(10, state.moveCount)
        assertEquals(board.grid, state.board.grid)
    }

    @Test
    fun `roundtrip keeps data`() {
        val id = "x"
        val original = GameState(
            board = Board.create(),
            currentPlayer = Player.BLACK,
            creator = Player.WHITE,
            lastTurnWasPass = false,
            endedByTwoPasses = false,
            moveCount = 2
        )

        val doc = sut.toDocument(id, original)
        val restored = sut.fromDocument(doc)

        assertNotNull(restored)
        assertEquals(original.creator, restored.creator)
        assertEquals(original.currentPlayer, restored.currentPlayer)
        assertEquals(original.lastTurnWasPass, restored.lastTurnWasPass)
        assertEquals(original.endedByTwoPasses, restored.endedByTwoPasses)
        assertEquals(original.moveCount, restored.moveCount)
        assertEquals(original.board.grid, restored.board.grid)
    }

    @Test
    fun `fromDocument returns null when board is missing`() {
        val doc = Document("_id", "g")
            .append("creator", Player.BLACK.symbol.toString())
            .append("currentPlayer", Player.WHITE.symbol.toString())

        assertNull(sut.fromDocument(doc))
    }

    @Test
    fun `fromDocument returns null when player symbol invalid`() {
        val board = Board.create()
        val lines = board.grid.map { it.joinToString("") }

        val doc = Document("_id", "g")
            .append("creator", "X")
            .append("currentPlayer", Player.WHITE.symbol.toString())
            .append("board", lines)

        val result = runCatching { sut.fromDocument(doc) }.getOrNull()
        assertNull(result)
    }
}
