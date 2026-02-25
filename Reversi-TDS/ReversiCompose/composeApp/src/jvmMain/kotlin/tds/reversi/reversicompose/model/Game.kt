package tds.reversi.reversicompose.model

import tds.reversi.reversicompose.storage.GameState

/**
 * Represents a game of Reversi, including game state and logic for managing turns, moves, and board display.
 *
 * The `Game` data class holds the current state of the game, such as the board, players, current turn, and whether
 * targets (valid move indicators) are shown. It also includes methods for performing game actions like making a move,
 * passing a turn, and checking if the game is over.
 *
 * @property name The name of the game (used for multiplayer games).
 * @property board The current state of the game board, represented by a `Board` object.
 * @property myPlayer The player who is controlling the game instance.
 * @property currentPlayer The player whose turn it is to play.
 * @property creator The player who created the game.
 * @property showTargets A flag indicating whether valid target positions should be shown.
 * @property lastTurnWasPass A flag indicating whether the last turn was a pass.
 * @property endedByTwoPasses A flag indicating whether the game ended due to both players passing consecutively.
 * @property moveCount The number of moves made in the game so far.
 */
@ConsistentCopyVisibility
data class Game private constructor(
    val name: String = "",
    val board: Board = Board.create(BOARD_SIDE),
    val myPlayer: Player,
    val currentPlayer: Player,
    val creator: Player,
    val showTargets: Boolean = false,
    val lastTurnWasPass: Boolean = false,
    val endedByTwoPasses: Boolean = false,
    val moveCount: Int = 0
) {

    /**
     * Returns the opponent player of the current player.
     * This is used to switch turns after each valid move.
     */
    private fun switchedPlayer() = currentPlayer.other()

    /**
     * Converts a board position (row and column) to a human-readable format (e.g., "1A").
     * This format is often used when displaying moves and the board.
     *
     * @param row Row index (0-based).
     * @param col Column index (0-based).
     * @return A string representing the board position (e.g., "1A").
     */
    fun formatCell(row: Int, col: Int): String = "${row + 1}${('A' + col)}"

    /**
     * Makes a move for the current player at the specified board coordinates.
     * If the move is valid, it updates the board and switches the turn to the other player.
     *
     * @param row The row index where the move will be made.
     * @param col The column index where the move will be made.
     * @return A new `Game` instance with the updated board and player turn, or `null` if the move is invalid.
     */
    fun makeMove(row: Int, col: Int): Game? {
        val newBoard = board.applyMove(row, col, currentPlayer) ?: return null
        return copy(
            board = newBoard,
            currentPlayer = switchedPlayer(),
            lastTurnWasPass = false,
            endedByTwoPasses = false,
            moveCount = moveCount + 1
        )
    }

    /**
     * Passes the current player's turn. If the last turn was also a pass, the game is considered over.
     *
     * @return A new `Game` instance with the updated turn and flags indicating that two passes have occurred.
     */
    fun passTurn(): Game =
        if (lastTurnWasPass)
            copy(
                currentPlayer = switchedPlayer(),
                lastTurnWasPass = true,
                endedByTwoPasses = true
            )
        else
            copy(
                currentPlayer = switchedPlayer(),
                lastTurnWasPass = true
            )

    /**
     * Checks if the current player has any valid moves available.
     *
     * @return `true` if the current player has no valid moves, meaning they cannot play and must pass.
     */
    fun canPass(): Boolean = board.getValidMoves(currentPlayer).isEmpty()

    /**
     * Sets whether the valid target positions for the current player should be displayed.
     *
     * @param on A boolean flag indicating whether targets should be shown (`true`) or not (`false`).
     * @return A new `Game` instance with the updated `showTargets` value.
     */
    fun setTargets(on: Boolean): Game = copy(showTargets = on)

    /**
     * Checks if the game is over. A game ends if:
     * - The board is full, or
     * - Both players have no valid moves left.
     *
     * @return `true` if the game is over, `false` otherwise.
     */
    fun isGameOver(): Boolean =
        board.isFull() || endedByTwoPasses

    /**
     * Converts the current game state into a `GameState` object, which can be used for saving or restoring the game.
     *
     * @return A `GameState` object representing the current game.
     */
    fun toGameState() = GameState(board, currentPlayer, creator, lastTurnWasPass, endedByTwoPasses, moveCount)

    /**
     * Returns a message with the result of the game (who won or if it's a tie).
     *
     * @return A string indicating the result of the game.
     */
    fun getWinnerMessage(): String {
        val score = board.score()
        val blackScore = score[Player.BLACK] ?: 0
        val whiteScore = score[Player.WHITE] ?: 0
        return when {
            blackScore > whiteScore -> "Game over! The winner is ${Player.BLACK} with $blackScore pieces"
            whiteScore > blackScore -> "Game over! The winner is ${Player.WHITE} with $whiteScore pieces"
            else -> "Game over! It's a tie with $blackScore pieces each"
        }
    }

    companion object {
        /**
         * Factory method to create an initial game, typically used for local or new games.
         *
         * @param name The name of the game (optional).
         * @param myPlayer The player who is starting the game.
         * @return A new `Game` instance with the given initial settings.
         */
        fun createInitial(name: String = "", myPlayer: Player): Game = Game(
            name = name,
            myPlayer = myPlayer,
            currentPlayer = myPlayer,
            creator = myPlayer
        )

        /**
         * Factory method for loading an existing game state (used for joining or refreshing a game).
         *
         * @param name The name of the game.
         * @param myPlayer The player joining the game.
         * @param state The `GameState` object representing the current game state.
         * @param showTargets A flag to determine if targets should be displayed.
         * @param lastTurnWasPass A flag to determine if the last turn was a pass.
         * @return A new `Game` instance with the loaded state.
         */
        fun fromState(name: String, myPlayer: Player, state: GameState, showTargets: Boolean = false): Game = Game(
            name = name,
            board = state.board,
            myPlayer = myPlayer,
            currentPlayer = state.currentPlayer,
            creator = state.creator,
            showTargets = showTargets,
            lastTurnWasPass = state.lastTurnWasPass,
            endedByTwoPasses = state.endedByTwoPasses,
            moveCount = state.moveCount
        )

         /**
         * Factory method to create a new game instance from an existing one after a move has been made.
         * This is used in the "play" command to update the game state.
         *
         * @param game The current game state.
         * @param row The row index of the move.
         * @param col The column index of the move.
         * @return A new `Game` instance with the updated board and player turn, or `null` if the move is invalid.
         */
        fun updateFromMove(game: Game, row: Int, col: Int): Game? = game.makeMove(row, col)
    }
}