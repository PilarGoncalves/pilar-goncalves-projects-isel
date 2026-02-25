import model.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class BoardTest {

    @Test
    fun `create initializes 8x8 with center pieces`() {
        val board = Board.create()
        assertEquals(8, board.size)
        assertEquals(WHITE_SYMBOL, board.getCell(3, 3))
        assertEquals(BLACK_SYMBOL, board.getCell(3, 4))
        assertEquals(2, board.score()[Player.BLACK])
        assertEquals(2, board.score()[Player.WHITE])
    }

    @Test
    fun `createFromGrid creates valid custom board`() {
        val grid = listOf(
            listOf(EMPTY, EMPTY, EMPTY),
            listOf(EMPTY, BLACK_SYMBOL, WHITE_SYMBOL),
            listOf(EMPTY, EMPTY, EMPTY)
        )
        val board = Board.createFromGrid(3, grid)
        assertNotNull(board)
        assertEquals(BLACK_SYMBOL, board.getCell(1, 1))
    }

    @Test
    fun `createFromGrid returns null on invalid`() {
        val invalidGrid = listOf(listOf('X'))
        assertNull(Board.createFromGrid(1, invalidGrid))
    }

    @Test
    fun `isValidMove true for initial 3d black`() {
        val board = Board.create()
        assertTrue(board.isValidMove(2, 3, Player.BLACK))
    }

    @Test
    fun `withMove flips and returns new board using public API only`() {
        val board = Board.create()
        val newBoard = board.applyMove(2, 3, Player.BLACK)
        assertNotNull(newBoard)

        // novo tabuleiro: o preto aumenta para 4 e o branco para 1
        assertEquals(4, newBoard.score()[Player.BLACK])
        assertEquals(1, newBoard.score()[Player.WHITE])

        // o tabuleiro original permanece com 2 de cada
        assertEquals(2, board.score()[Player.BLACK])
        assertEquals(2, board.score()[Player.WHITE])

        // célula específica foi virada no tabuleiro novo, permanece igual no original
        assertEquals(BLACK_SYMBOL, newBoard.getCell(3, 3))
        assertEquals(WHITE_SYMBOL, board.getCell(3, 3))
    }

    @Test
    fun `isFull false initial`() {
        assertFalse(Board.create().isFull())
    }

    @Test
    fun `create throws when size is odd or out of bounds`() {
        assertFailsWith<IllegalArgumentException> { Board.create(3) }   // ímpar
        assertFailsWith<IllegalArgumentException> { Board.create(27) }  // > 26
        assertFailsWith<IllegalArgumentException> { Board.create(2) }   // < 4
    }

    @Test
    fun `player other is reflexive`() {
        assertEquals(Player.BLACK, Player.BLACK.other().other())
        assertEquals(Player.WHITE, Player.WHITE.other().other())
    }

    @Test
    fun `player other is stable and symbol unchanged`() {
        val bOther1 = Player.BLACK.other()
        val bOther2 = Player.BLACK.other()
        assertEquals(bOther1, bOther2)
        assertEquals(Player.WHITE.symbol, bOther1.symbol)
        assertEquals(Player.BLACK.symbol, Player.BLACK.symbol)
    }
}
