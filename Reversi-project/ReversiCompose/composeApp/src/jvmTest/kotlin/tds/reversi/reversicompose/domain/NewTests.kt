package tds.reversi.reversicompose.domain

import tds.reversi.reversicompose.model.Player
import tds.reversi.reversicompose.storage.GameState
import tds.reversi.reversicompose.storage.Storage
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class NewTests {

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
    fun `new local game creates game without name`() {
        val storage = InMemoryStorage()
        val cmd = New(storage)

        val result = cmd.execute(
            context = CommandContext.Empty,
            params = listOf("#")
        ) as CommandResult.Success

        val game = result.game
        assertTrue(game.name.isEmpty())
        assertEquals(Player.BLACK, game.myPlayer)
    }

    @Test
    fun `new multiplayer game persists state`() {
        val storage = InMemoryStorage()
        val cmd = New(storage)

        val result = cmd.execute(
            context = CommandContext.Empty,
            params = listOf("#", "game1")
        ) as CommandResult.Success

        assertTrue(storage.read("game1") != null)
        assertEquals("game1", result.game.name)
    }

    @Test
    fun `new throws when game name already exists`() {
        val storage = InMemoryStorage()
        val cmd = New(storage)

        cmd.execute(
            context = CommandContext.Empty,
            params = listOf("#", "dup")
        )

        assertFailsWith<CommandException.InvalidParameters> {
            cmd.execute(
                context = CommandContext.Empty,
                params = listOf("@", "dup")
            )
        }
    }
}
