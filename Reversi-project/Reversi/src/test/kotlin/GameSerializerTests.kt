import model.*
import storage.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class GameSerializerTests {

    @Test
    fun `serialize on a game state produces the correct string`() {
        val sut = GameSerializer()
        val board = Board.create()
        val state = GameState(
            board = board,
            currentPlayer = Player.BLACK,
            creator = Player.WHITE
        )

        val expected = """
            @
            #
            . . . . . . . .
            . . . . . . . .
            . . . . . . . .
            . . . @ # . . .
            . . . # @ . . .
            . . . . . . . .
            . . . . . . . .
            . . . . . . . .
        """.trimIndent()

        val actual = sut.serialize(state)
        assertEquals(expected, actual)
    }

    @Test
    fun `deserialize on a string produces the correct game state`() {
        val sut = GameSerializer()

        val text = """
            @
            #
            . . . . . . . .
            . . . . . . . .
            . . . . . . . .
            . . . @ # . . .
            . . . # @ . . .
            . . . . . . . .
            . . . . . . . .
            . . . . . . . .
        """.trimIndent()

        val state = sut.deserialize(text)
        assertNotNull(state)

        val backToText = sut.serialize(state)
        assertEquals(text, backToText)
    }
}
