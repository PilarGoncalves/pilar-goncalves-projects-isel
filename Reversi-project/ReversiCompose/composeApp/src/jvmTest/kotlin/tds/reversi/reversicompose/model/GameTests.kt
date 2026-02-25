package tds.reversi.reversicompose.model

import tds.reversi.reversicompose.storage.GameState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class GameTests {

    @Test
    fun `initial game has correct initial state`() {
        val game = Game.createInitial(
            name = "",
            myPlayer = Player.BLACK
        )

        assertEquals(Player.BLACK, game.myPlayer)
        assertEquals(Player.BLACK, game.currentPlayer)
        assertEquals(2, game.board.score()[Player.BLACK])
        assertEquals(2, game.board.score()[Player.WHITE])
        assertFalse(game.isGameOver())
    }

    @Test
    fun `makeMove switches current player and increments move count`() {
        val game = Game.createInitial(
            name = "",
            myPlayer = Player.BLACK
        )

        val newGame = game.makeMove(2, 3)
        assertNotNull(newGame)

        assertEquals(Player.WHITE, newGame.currentPlayer)
        assertEquals(1, newGame.moveCount)
        assertFalse(newGame.lastTurnWasPass)
    }

    @Test
    fun `passTurn sets lastTurnWasPass flag`() {
        val game = Game.createInitial(
            name = "",
            myPlayer = Player.BLACK
        )

        val newGame = game.passTurn()

        assertTrue(newGame.lastTurnWasPass)
        assertFalse(newGame.endedByTwoPasses)
        assertEquals(Player.WHITE, newGame.currentPlayer)
    }

    @Test
    fun `two consecutive passes end the game`() {
        val game = Game.createInitial(
            name = "",
            myPlayer = Player.BLACK
        )

        val afterFirstPass = game.passTurn()
        val afterSecondPass = afterFirstPass.passTurn()

        assertTrue(afterSecondPass.endedByTwoPasses)
        assertTrue(afterSecondPass.isGameOver())
    }

    @Test
    fun `canPass false when valid moves exist`() {
        val game = Game.createInitial(
            name = "",
            myPlayer = Player.BLACK
        )

        assertFalse(game.canPass())
    }

    @Test
    fun `setTargets toggles flag correctly`() {
        val game = Game.createInitial(
            name = "",
            myPlayer = Player.BLACK
        )

        val withTargets = game.setTargets(true)
        assertTrue(withTargets.showTargets)

        val withoutTargets = withTargets.setTargets(false)
        assertFalse(withoutTargets.showTargets)
    }

    @Test
    fun `toGameState preserves important fields`() {
        val game = Game.createInitial(
            name = "game1",
            myPlayer = Player.BLACK
        )

        val state = game.toGameState()

        assertEquals(game.board, state.board)
        assertEquals(game.currentPlayer, state.currentPlayer)
        assertEquals(game.creator, state.creator)
        assertEquals(game.moveCount, state.moveCount)
    }

    @Test
    fun `fromState restores game correctly`() {
        val board = Board.create()
        val state = GameState(
            board = board,
            currentPlayer = Player.WHITE,
            creator = Player.BLACK,
            lastTurnWasPass = true,
            endedByTwoPasses = false,
            moveCount = 5
        )

        val game = Game.fromState(
            name = "gameX",
            myPlayer = Player.BLACK,
            state = state
        )

        assertEquals("gameX", game.name)
        assertEquals(Player.WHITE, game.currentPlayer)
        assertEquals(Player.BLACK, game.myPlayer)
        assertTrue(game.lastTurnWasPass)
        assertEquals(5, game.moveCount)
    }
}
