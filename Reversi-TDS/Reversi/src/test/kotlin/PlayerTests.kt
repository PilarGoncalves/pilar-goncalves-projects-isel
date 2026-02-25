import model.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PlayerTest {

    @Test
    fun `fromSymbol returns BLACK for #`() {
        assertEquals(Player.BLACK, Player.fromSymbol(BLACK_SYMBOL))
    }

    @Test
    fun `fromSymbol returns WHITE for @`() {
        assertEquals(Player.WHITE, Player.fromSymbol(WHITE_SYMBOL))
    }

    @Test
    fun `other of BLACK is WHITE`() {
        assertEquals(Player.WHITE, Player.BLACK.other())
    }

    @Test
    fun `other of WHITE is BLACK`() {
        assertEquals(Player.BLACK, Player.WHITE.other())
    }

    @Test
    fun `fromSymbol throws on invalid symbol`() {
        assertFailsWith<IllegalArgumentException> {
            Player.fromSymbol('X')
        }
    }

    @Test
    fun `symbol property returns correct char`() {
        assertEquals(BLACK_SYMBOL, Player.BLACK.symbol)
        assertEquals(WHITE_SYMBOL, Player.WHITE.symbol)
    }

    @Test
    fun `other is involutive for both players`() {
        assertEquals(Player.BLACK, Player.BLACK.other().other())
        assertEquals(Player.WHITE, Player.WHITE.other().other())
    }
}
