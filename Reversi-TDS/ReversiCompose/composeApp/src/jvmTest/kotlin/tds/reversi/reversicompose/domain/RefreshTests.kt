package tds.reversi.reversicompose.domain

import tds.reversi.reversicompose.model.*
import tds.reversi.reversicompose.storage.GameState
import tds.reversi.reversicompose.storage.Storage
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class RefreshTests {

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
    fun `refresh fails when game not found`() {
        val storage = InMemoryStorage()

        val base = Game.createInitial(name = "g1", myPlayer = Player.BLACK)
        val cmd = Refresh(storage)

        assertFailsWith<CommandException.InvalidParameters> {
            cmd.execute(CommandContext.WithGame(base), emptyList())
        }
    }

    @Test
    fun `refresh loads state from storage and keeps myPlayer when on opponent turn`() {
        val storage = InMemoryStorage()

        val updatedBoard = requireNotNull(Board.create().applyMove(2, 3, Player.BLACK))
        val remoteState = GameState(
            board = updatedBoard,
            currentPlayer = Player.WHITE,
            creator = Player.BLACK,
            lastTurnWasPass = false,
            endedByTwoPasses = false,
            moveCount = 1
        )
        storage.create("g1", remoteState)

        val localGame = Game.fromState(
            name = "g1",
            myPlayer = Player.BLACK,
            state = remoteState.copy(currentPlayer = Player.WHITE)
        )

        val cmd = Refresh(storage)
        val result = cmd.execute(
            context = CommandContext.WithGame(localGame),
            params = emptyList()
        ) as CommandResult.Success

        val refreshed = result.game
        assertEquals(Player.BLACK, refreshed.myPlayer)
        assertEquals(Player.WHITE, refreshed.currentPlayer)
        assertEquals(updatedBoard.grid, refreshed.board.grid)
    }

    @Test
    fun `refresh returns winner message when game is over and on opponent turn`() {
        val storage = InMemoryStorage()

        val fullBoard = requireNotNull(
            Board.createFromGrid(
                size = 2,
                grid = listOf(
                    listOf(BLACK_SYMBOL, WHITE_SYMBOL),
                    listOf(WHITE_SYMBOL, BLACK_SYMBOL)
                )
            )
        )

        val remoteState = GameState(
            board = fullBoard,
            currentPlayer = Player.BLACK,
            creator = Player.BLACK,
            lastTurnWasPass = true,
            endedByTwoPasses = true,
            moveCount = 2
        )
        storage.create("g1", remoteState)

        val localGame = Game.fromState(
            name = "g1",
            myPlayer = Player.WHITE,
            state = remoteState
        )

        val cmd = Refresh(storage)
        val result = cmd.execute(
            context = CommandContext.WithGame(localGame),
            params = emptyList()
        ) as CommandResult.Success

        assertTrue(result.message.isNotBlank())
    }
}
