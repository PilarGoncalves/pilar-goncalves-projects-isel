package tds.reversi.reversicompose.domain

import tds.reversi.reversicompose.model.*
import tds.reversi.reversicompose.storage.GameState
import tds.reversi.reversicompose.storage.Storage
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class PlayTests {

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
    fun `play valid move updates board and switches turn`() {
        val storage = InMemoryStorage()
        val game = Game.createInitial(name = "", myPlayer = Player.BLACK)

        val cmd = Play(storage)
        val result = cmd.execute(
            context = CommandContext.WithGame(game),
            params = listOf("3D")
        ) as CommandResult.Success

        val newGame = result.game
        assertEquals(Player.WHITE, newGame.currentPlayer)
        assertTrue(newGame.board.score()[Player.BLACK]!! > 2)
    }

    @Test
    fun `play fails when not my turn in multiplayer`() {
        val storage = InMemoryStorage()

        val state = GameState(
            board = Board.create(),
            currentPlayer = Player.BLACK,
            creator = Player.BLACK,
            lastTurnWasPass = false,
            endedByTwoPasses = false,
            moveCount = 0
        )

        val game = Game.fromState(
            name = "g1",
            myPlayer = Player.WHITE,
            state = state
        )

        val cmd = Play(storage)

        assertFailsWith<CommandException.InvalidParameters> {
            cmd.execute(
                context = CommandContext.WithGame(game),
                params = listOf("3D")
            )
        }
    }

    @Test
    fun `play fails on invalid cell`() {
        val storage = InMemoryStorage()
        val game = Game.createInitial(name = "", myPlayer = Player.BLACK)

        val cmd = Play(storage)

        assertFailsWith<CommandException.InvalidParameters> {
            cmd.execute(
                context = CommandContext.WithGame(game),
                params = listOf("1A")
            )
        }
    }

    @Test
    fun `play persists game in multiplayer`() {
        val storage = InMemoryStorage()
        val game = Game.createInitial(name = "g1", myPlayer = Player.BLACK)

        val cmd = Play(storage)
        cmd.execute(
            context = CommandContext.WithGame(game),
            params = listOf("3D")
        )

        assertTrue(storage.read("g1") != null)
    }
}
