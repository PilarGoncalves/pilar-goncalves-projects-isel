package storage

import model.Board
import model.Player

/**
 * Represents the state of the game.
 * This data class holds the current state of the board and the players involved in the game.
 */
data class GameState(
    /**
     * The board representing the current game state, including the positions of all pieces.
     */
    val board: Board,

    /**
     * The player who is currently taking their turn.
     */
    val currentPlayer: Player,

    /**
     * The player who created the game.
     */
    val creator: Player
)