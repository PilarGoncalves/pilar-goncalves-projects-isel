import controller.*
import model.*
import storage.GameState
import storage.Storage
import kotlin.test.*

class UtilsTests {

    private val fakeStorage = object : Storage<String, GameState> {
        override fun create(key: String, data: GameState) {}
        override fun read(key: String): GameState? = null
        override fun update(key: String, data: GameState) {}
        override fun delete(key: String) {}
    }

    @Test
    fun `isLocal returns true when name is empty`() {
        val board = requireNotNull(
            value = Board.createFromGrid(
                size = 2,
                grid = listOf(
                    listOf(BLACK_SYMBOL, WHITE_SYMBOL),
                    listOf(WHITE_SYMBOL, BLACK_SYMBOL)
                )
            )
        ) { "Falha ao criar board" }

        val state = GameState(
            board,
            currentPlayer = Player.BLACK,
            creator = Player.BLACK
        )
        val game = Game.fromState(
            name = "",
            myPlayer = Player.BLACK,
            state = state
        )
        assertTrue(Utils.isLocal(game))
    }

    @Test
    fun `notMyTurn returns true when currentPlayer is different`() {
        val board = requireNotNull(
            value = Board.createFromGrid(
                size = 2,
                grid = listOf(
                    listOf(BLACK_SYMBOL, WHITE_SYMBOL),
                    listOf(WHITE_SYMBOL, BLACK_SYMBOL)
                )
            )
        ) { "Falha ao criar board" }

        val state = GameState(
            board,
            currentPlayer = Player.BLACK,
            creator = Player.BLACK
        )
        val game = Game.fromState(
            name = "test",
            myPlayer = Player.WHITE,
            state = state
        )
        assertTrue(Utils.notMyTurn(game))
    }

    @Test
    fun `getPlayerInfo returns correct string`() {
        val board = requireNotNull(
            value = Board.createFromGrid(
                size = 2,
                grid = listOf(
                    listOf(BLACK_SYMBOL, WHITE_SYMBOL),
                    listOf(WHITE_SYMBOL, BLACK_SYMBOL)
                )
            )
        ) { "Falha ao criar board" }

        val state = GameState(
            board,
            currentPlayer = Player.BLACK,
            creator = Player.BLACK
        )
        val game = Game.fromState(
            name = "game1",
            myPlayer = Player.BLACK,
            state = state
        )
        val info = Utils.getPlayerInfo(game)
        assertEquals("You are player # in game game1", info)
    }

    @Test
    fun `saveGame does not crash when local`() {
        val board = requireNotNull(
            value = Board.createFromGrid(
                size = 2,
                grid = listOf(
                    listOf(BLACK_SYMBOL, WHITE_SYMBOL),
                    listOf(WHITE_SYMBOL, BLACK_SYMBOL)
                )
            )
        ) { "Falha ao criar board" }

        val state = GameState(
            board,
            currentPlayer = Player.BLACK,
            creator = Player.BLACK
        )
        val localGame = Game.fromState(
            name = "",
            myPlayer = Player.BLACK,
            state = state
        )
        Utils.saveGame(localGame, fakeStorage)
    }
}
