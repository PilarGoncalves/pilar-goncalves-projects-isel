import model.*
import storage.GameState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class GameTest {

    @Test
    fun `initial game has correct state`() {
        val game = Game.createInitial(
            myPlayer = Player.BLACK
        )

        assertEquals(Player.BLACK, game.myPlayer)
        assertEquals(2, game.board.score()[Player.BLACK])
        assertFalse(game.isGameOver())
    }

    @Test
    fun `makeMove switches player and resets pass flag`() {
        val game = Game.createInitial(
            myPlayer = Player.BLACK
        )

        val newGame = game.makeMove(2, 3)
        assertNotNull(newGame)
        assertEquals(Player.WHITE, newGame.currentPlayer)
        assertFalse(newGame.lastTurnWasPass)
    }

    @Test
    fun `passTurn sets flag and ends on consecutive`() {
        val game1 = Game.createInitial(
            myPlayer = Player.BLACK
        )
        val (newGame1, ended1) = game1.passTurn()
        assertTrue(newGame1.lastTurnWasPass)
        assertFalse(ended1)

        val (newGame2, ended2) = newGame1.passTurn()
        assertTrue(ended2)
    }

    @Test
    fun `canPass true if no moves`() {
        val game = Game.createInitial(
            myPlayer = Player.BLACK
        )
        assertFalse(game.canPass())

        val noMovesBoard = requireNotNull(
            Board.createFromGrid(
                size = 2,
                grid = listOf(
                    listOf(BLACK_SYMBOL, WHITE_SYMBOL),
                    listOf(WHITE_SYMBOL, BLACK_SYMBOL)
                )
            )
        ) { "Falha ao criar o tabuleiro" }

        val state = GameState(
            noMovesBoard,
            currentPlayer = Player.BLACK,
            creator = Player.BLACK
        )
        val noMovesGame = Game.fromState(
            name = "",
            myPlayer = Player.BLACK,
            state = state
        )
        assertTrue(noMovesGame.canPass())
    }

    @Test
    fun `setTargets toggles correctly`() {
        val game = Game.createInitial(
            myPlayer = Player.BLACK
        )
        val newGame = game.setTargets(true)
        assertTrue(newGame.showTargets)

        val toggledGame = newGame.setTargets(false)
        assertFalse(toggledGame.showTargets)
    }


    @Test
    fun `isGameOver true if full`() {
        val fullBoard = requireNotNull(
            Board.createFromGrid(
                size = 2,
                grid = listOf(
                    listOf(BLACK_SYMBOL, WHITE_SYMBOL),
                    listOf(WHITE_SYMBOL, BLACK_SYMBOL)
                )
            )
        ) { "Falha ao criar o tabuleiro" }

        val state = GameState(
            fullBoard,
            currentPlayer = Player.BLACK,
            creator = Player.BLACK
        )
        val game = Game.fromState(
            name = "",
            myPlayer = Player.BLACK,
            state = state
        )
        assertTrue(game.isGameOver())
    }

    @Test
    fun `toGameState preserves current player and creator`() {
        val game = Game.createInitial(
            myPlayer = Player.BLACK
        )
        val state = game.toGameState()
        assertEquals(game.currentPlayer, state.currentPlayer)
        assertEquals(game.creator, state.creator)
    }
}