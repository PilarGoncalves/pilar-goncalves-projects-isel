package tds.reversi.reversicompose.domain

import tds.reversi.reversicompose.model.*
import tds.reversi.reversicompose.storage.GameState
import tds.reversi.reversicompose.storage.Storage
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class PassTests {

    private class InMemoryStorage : Storage<String, GameState> {
        private val data = mutableMapOf<String, GameState>()

        override fun create(key: String, data: GameState) {
            this.data[key] = data
        }

        override fun read(key: String): GameState? = data[key]

        override fun update(key: String, data: GameState) {
            this.data[key] = data
        }

        override fun delete(key: String) {
            data.remove(key)
        }
    }

    @Test
    fun `pass succeeds when no valid moves`() {
        val storage = InMemoryStorage()

        val noMovesBoard = requireNotNull(
            Board.createFromGrid(
                size = 2,
                grid = listOf(
                    listOf(BLACK_SYMBOL, WHITE_SYMBOL),
                    listOf(WHITE_SYMBOL, BLACK_SYMBOL)
                )
            )
        )

        val state = GameState(
            board = noMovesBoard,
            currentPlayer = Player.BLACK,
            creator = Player.BLACK,
            lastTurnWasPass = false,
            endedByTwoPasses = false,
            moveCount = 1
        )

        val game = Game.fromState(
            name = "",
            myPlayer = Player.BLACK,
            state = state
        )

        val cmd = Pass(storage)
        val result = cmd.execute(
            context = CommandContext.WithGame(game),
            params = emptyList()
        ) as CommandResult.Success

        assertTrue(result.game.lastTurnWasPass)
        assertEquals(Player.WHITE, result.game.currentPlayer)
    }

    @Test
    fun `pass fails when valid moves exist`() {
        val storage = InMemoryStorage()
        val game = Game.createInitial(name = "", myPlayer = Player.BLACK)

        val cmd = Pass(storage)

        assertFailsWith<CommandException.InvalidParameters> {
            cmd.execute(
                context = CommandContext.WithGame(game),
                params = emptyList()
            )
        }
    }

    @Test
    fun `pass persists game in multiplayer`() {
        val storage = InMemoryStorage()

        val noMovesBoard = requireNotNull(
            Board.createFromGrid(
                size = 2,
                grid = listOf(
                    listOf(BLACK_SYMBOL, WHITE_SYMBOL),
                    listOf(WHITE_SYMBOL, BLACK_SYMBOL)
                )
            )
        )

        val state = GameState(
            board = noMovesBoard,
            currentPlayer = Player.BLACK,
            creator = Player.BLACK,
            lastTurnWasPass = false,
            endedByTwoPasses = false,
            moveCount = 1
        )

        val game = Game.fromState(
            name = "g1",
            myPlayer = Player.BLACK,
            state = state
        )

        val cmd = Pass(storage)
        cmd.execute(
            context = CommandContext.WithGame(game),
            params = emptyList()
        )

        assertTrue(storage.read("g1") != null)
    }
}
