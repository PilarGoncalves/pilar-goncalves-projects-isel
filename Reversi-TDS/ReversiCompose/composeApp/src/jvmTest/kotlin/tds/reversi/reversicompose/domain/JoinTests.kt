package tds.reversi.reversicompose.domain

import tds.reversi.reversicompose.model.Player
import tds.reversi.reversicompose.model.Game
import tds.reversi.reversicompose.model.Board
import tds.reversi.reversicompose.storage.GameState
import tds.reversi.reversicompose.storage.Storage
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class JoinTests {

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
    fun `join existing game succeeds for second player`() {
        val storage = InMemoryStorage()

        val initialGame = Game.createInitial(
            name = "g1",
            myPlayer = Player.BLACK
        )
        storage.create("g1", initialGame.toGameState())

        val cmd = Join(storage)
        val result = cmd.execute(
            context = CommandContext.Empty,
            params = listOf("g1")
        ) as CommandResult.Success

        val joinedGame = result.game
        assertEquals("g1", joinedGame.name)
        assertEquals(Player.WHITE, joinedGame.myPlayer)
        assertEquals(Player.BLACK, joinedGame.creator)
    }

    @Test
    fun `join fails when game does not exist`() {
        val storage = InMemoryStorage()
        val cmd = Join(storage)

        assertFailsWith<CommandException.InvalidParameters> {
            cmd.execute(
                context = CommandContext.Empty,
                params = listOf("missing")
            )
        }
    }

    @Test
    fun `join fails when game already started`() {
        val storage = InMemoryStorage()

        val board = Board.create()
        val state = GameState(
            board = board,
            currentPlayer = Player.BLACK,
            creator = Player.BLACK,
            lastTurnWasPass = false,
            endedByTwoPasses = false,
            moveCount = 2
        )
        storage.create("g2", state)

        val cmd = Join(storage)

        assertFailsWith<CommandException.InvalidParameters> {
            cmd.execute(
                context = CommandContext.Empty,
                params = listOf("g2")
            )
        }
    }

    @Test
    fun `join does not modify stored state`() {
        val storage = InMemoryStorage()

        val game = Game.createInitial(
            name = "g3",
            myPlayer = Player.BLACK
        )
        storage.create("g3", game.toGameState())

        val cmd = Join(storage)
        cmd.execute(
            context = CommandContext.Empty,
            params = listOf("g3")
        )

        val stored = storage.read("g3")
        assertTrue(stored != null)
        assertEquals(Player.BLACK, stored.creator)
    }
}
