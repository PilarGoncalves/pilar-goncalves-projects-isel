import controller.*
import model.*
import storage.GameState
import storage.Storage
import kotlin.test.*

/**
 * Tests basic local command behavior without external storage.
 */
class CommandLocalTests {

    private val fakeStorage = object : Storage<String, GameState> {
        override fun create(key: String, data: GameState) {}
        override fun read(key: String): GameState? = null
        override fun update(key: String, data: GameState) {}
        override fun delete(key: String) {}
    }

    @Test
    fun `new local initializes correctly`() {
        val command = New(fakeStorage)
        val result = command.execute(CommandContext.Empty, listOf("#"))
        assertTrue(result is CommandResult.Success)
        val game = result.game
        assertEquals(Player.BLACK, game.myPlayer)
        assertTrue(game.name.isEmpty())
    }

    @Test
    fun `play valid move flips pieces and switches turn`() {
        val game = Game.createInitial(
            name = "",
            myPlayer = Player.BLACK
        )
        val context = CommandContext.WithGame(game)
        val command = Play(fakeStorage)
        val result = command.execute(context, listOf("3d"))
        assertTrue(result is CommandResult.Success)
        val newGame = result.game
        assertEquals(Player.WHITE, newGame.currentPlayer)
        assertTrue(newGame.board.score()[Player.BLACK]!! > 2)
    }

    @Test
    fun `pass cannot happen if moves are available`() {
        val game = Game.createInitial(
            name = "",
            myPlayer = Player.BLACK
        )
        val context = CommandContext.WithGame(game)
        val command = Pass(fakeStorage)
        assertFailsWith<CommandException.InvalidParameters> {
            command.execute(context, emptyList())
        }
    }

    @Test
    fun `show command displays current board`() {
        val game = Game.createInitial(
            name = "",
            myPlayer = Player.BLACK
        )
        val context = CommandContext.WithGame(game)
        val command = Show()
        val result = command.execute(context, emptyList())
        assertTrue(result is CommandResult.Success)
        assertEquals(game, result.game)
    }

    @Test
    fun `exit returns Exit result`() {
        val command = Exit()
        val result = command.execute(CommandContext.Empty, emptyList())
        assertTrue(result is CommandResult.Exit)
    }
}
