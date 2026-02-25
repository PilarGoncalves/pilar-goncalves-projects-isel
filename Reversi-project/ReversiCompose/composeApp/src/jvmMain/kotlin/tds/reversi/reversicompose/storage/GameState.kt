package tds.reversi.reversicompose.storage

import tds.reversi.reversicompose.model.Board
import tds.reversi.reversicompose.model.Player

/**
 * Represents the state of the game.
 * This data class holds the current state of the board and the players involved in the game.
 * It is used for saving or restoring the game's progress, typically in a database like MongoDB.
 *
 * The `GameState` object is a snapshot of the game's current status, including the board layout,
 * the players' turns, and other important game state details.
 *
 * @property board The board representing the current game state, including the positions of all pieces.
 * @property currentPlayer The player who is currently taking their turn. This determines whose turn it is.
 * @property creator The player who created the game. This is used to identify the game creator.
 * @property lastTurnWasPass A flag indicating whether the last turn was a pass. If both players pass consecutively, the game ends.
 * @property endedByTwoPasses A flag indicating whether the game ended due to two consecutive passes.
 * @property moveCount The number of moves that have been played in the game so far. This helps track the game's progress.
 */
data class GameState(
    val board: Board,
    val currentPlayer: Player,
    val creator: Player,
    val lastTurnWasPass: Boolean,
    val endedByTwoPasses: Boolean,
    val moveCount: Int
)