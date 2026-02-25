package tds.reversi.reversicompose.domain

import tds.reversi.reversicompose.model.Board
import tds.reversi.reversicompose.model.Game
import tds.reversi.reversicompose.model.Player
import tds.reversi.reversicompose.storage.GameState
import tds.reversi.reversicompose.storage.Storage
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UtilsTests {

    private class InMemoryStorage : Storage<String, GameState> {
        private val data = mutableMapOf<String, GameState>()
        var updateCalls = 0

        override fun create(key: String, data: GameState) {
            this.data[key] = data
        }

        override fun read(key: String): GameState? = data[key]

        override fun update(key: String, data: GameState) {
            updateCalls++
            this.data[key] = data
        }

        override fun delete(key: String) {
            data.remove(key)
        }
    }

    @Test
    fun `isLocal returns true when name is empty`() {
        val game = Game.createInitial(name = "", myPlayer = Player.BLACK)
        assertTrue(Utils.isLocal(game))
        assertFalse(Utils.isMultiplayer(game))
    }

    @Test
    fun `isLocal returns false when name is not empty`() {
        val game = Game.createInitial(name = "g1", myPlayer = Player.BLACK)
        assertFalse(Utils.isLocal(game))
        assertTrue(Utils.isMultiplayer(game))
    }

    @Test
    fun `notMyTurn is false on local games`() {
        val game = Game.createInitial(name = "", myPlayer = Player.BLACK)
        assertFalse(Utils.notMyTurn(game))
    }

    @Test
    fun `notMyTurn is true on multiplayer when currentPlayer differs from myPlayer`() {
        val state = GameState(
            board = Board.create(),
            currentPlayer = Player.WHITE,
            creator = Player.BLACK,
            lastTurnWasPass = false,
            endedByTwoPasses = false,
            moveCount = 0
        )
        val game = Game.fromState(name = "g1", myPlayer = Player.BLACK, state = state)
        assertTrue(Utils.notMyTurn(game))
    }

    @Test
    fun `notMyTurn is false on multiplayer when currentPlayer equals myPlayer`() {
        val state = GameState(
            board = Board.create(),
            currentPlayer = Player.BLACK,
            creator = Player.BLACK,
            lastTurnWasPass = false,
            endedByTwoPasses = false,
            moveCount = 0
        )
        val game = Game.fromState(name = "g1", myPlayer = Player.BLACK, state = state)
        assertFalse(Utils.notMyTurn(game))
    }

    @Test
    fun `saveGame does nothing for local games`() {
        val storage = InMemoryStorage()
        val game = Game.createInitial(name = "", myPlayer = Player.BLACK)

        Utils.saveGame(game, storage)

        assertTrue(storage.updateCalls == 0)
    }

    @Test
    fun `saveGame updates storage for multiplayer games`() {
        val storage = InMemoryStorage()
        val game = Game.createInitial(name = "g1", myPlayer = Player.BLACK)

        Utils.saveGame(game, storage)

        assertTrue(storage.updateCalls == 1)
        assertTrue(storage.read("g1") != null)
    }
}