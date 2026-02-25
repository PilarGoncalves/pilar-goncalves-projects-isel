package model

const val BOARD_SIDE = 8
const val EMPTY = '.'
const val TARGET = '*'

/**
 * Immutable representation of a Reversi board.
 *
 * The board stores its current state as a 2D grid of characters. Valid cell values are:
 * - `BLACK_SYMBOL` for black pieces,
 * - `WHITE_SYMBOL` for white pieces,
 * - `EMPTY` for empty cells.
 *
 * Instances are created via factory methods in the companion object to enforce invariants.
 *
 * @property size The side length of the square board.
 * @property grid The immutable 2D list representing the board cells.
 */
@ConsistentCopyVisibility
data class Board private constructor(val size: Int = BOARD_SIDE, val grid: List<List<Char>>) {

    companion object {
        /**
         * Creates a new board with the standard initial Reversi setup:
         * two black and two white pieces in the center in a diagonal arrangement.
         *
         * @param size Desired board side length. Must be even and between 4 and 26 (inclusive).
         * @return A `Board` instance with the initial setup.
         * @throws IllegalArgumentException If `size` is odd or out of the allowed range.
         */
        fun create(size: Int = BOARD_SIDE): Board {
            require(size % 2 == 0 && size in 4..26) { "The size must be even and between 4 and 26" }
            val mid = size / 2 - 1
            val initialGrid = List(size) { MutableList(size) { EMPTY } }
            initialGrid[mid][mid] = WHITE_SYMBOL
            initialGrid[mid][mid + 1] = BLACK_SYMBOL
            initialGrid[mid + 1][mid] = BLACK_SYMBOL
            initialGrid[mid + 1][mid + 1] = WHITE_SYMBOL
            return Board(size, initialGrid.map { it.toList() })
        }

        /**
         * Creates a board from an existing grid, validating dimensions and symbols.
         *
         * The provided grid must be square (`size x size`) and contain only the allowed symbols:
         * `Player.BLACK.symbol`, `Player.WHITE.symbol`, or `EMPTY`.
         *
         * @param size The expected side length for the grid.
         * @param grid The cell matrix to wrap.
         * @return A `Board` if the grid is valid; otherwise `null`.
         */
        fun createFromGrid(size: Int = BOARD_SIDE, grid: List<List<Char>>): Board? {
            if (size != grid.size || grid.any { it.size != size } || !grid.flatten().all { it in listOf((Player.BLACK.symbol), Player.WHITE.symbol, EMPTY) }) {
                return null
            }
            return Board(size, grid.map { it.toList() })
        }
    }

    /**
     * All 8 straight directions used to scan the board (rows, columns, and diagonals).
     */
    private val directions = listOf(
        -1 to -1, -1 to 0, -1 to 1,
        0 to -1,          0 to 1,
        1 to -1,  1 to 0, 1 to 1
    )

    /**
     * Checks whether a position lies inside the board boundaries.
     *
     * @param row Row index (0-based).
     * @param col Column index (0-based).
     * @return `true` if `(row, col)` is within `[0, size)`, `false` otherwise.
     */
    private fun inBounds(row: Int, col: Int) = row in 0 until size && col in 0 until size

    /**
     * Returns the character stored at a given position.
     *
     * @param row Row index (0-based).
     * @param col Column index (0-based).
     * @return The cell value at `(row, col)`.
     * @throws IllegalArgumentException If the position is out of bounds.
     */
    fun getCell(row: Int, col: Int): Char {
        require(inBounds(row, col)) { "Out of bounds: ($row, $col)" }
        return grid[row][col]
    }

    /**
     * Checks if a cell is empty.
     *
     * @param row Row index (0-based).
     * @param col Column index (0-based).
     * @return `true` if the cell contains `EMPTY`, `false` otherwise.
     */
    fun isEmpty(row: Int, col: Int): Boolean = getCell(row, col) == EMPTY

    /**
     * Computes the count of pieces on the board for each player.
     *
     * @return A map with the number of black and white pieces.
     */
    fun score(): Map<Player, Int> {
        var blacks = 0
        var whites = 0
        for (r in 0 until size) for (c in 0 until size) {
            when (grid[r][c]) {
                BLACK_SYMBOL -> blacks++
                WHITE_SYMBOL -> whites++
            }
        }
        return mapOf(Player.BLACK to blacks, Player.WHITE to whites)
    }

    /**
     * Tests whether the board has no empty cells left.
     *
     * @return `true` if all cells are filled; `false` otherwise.
     */
    fun isFull(): Boolean = grid.all { row -> row.none { it == EMPTY } }

    /**
     * Validates whether placing `player`'s piece at `(row, col)` is a legal Reversi move.
     *
     * A move is valid if:
     * - The target cell is empty and within bounds, and
     * - In at least one direction, there is a contiguous run of opponent pieces
     *   immediately followed by a `player` piece.
     *
     * @param row Target row index (0-based).
     * @param col Target column index (0-based).
     * @param player The player attempting the move.
     * @return `true` if the move is legal; `false` otherwise.
     */
    fun isValidMove(row: Int, col: Int, player: Player): Boolean {
        if (!inBounds(row, col) || !isEmpty(row, col)) return false
        for ((dr, dc) in directions) {
            var r = row + dr
            var c = col + dc
            var foundOpponent = false
            while (inBounds(r, c)) {
                val cell = getCell(r, c)
                if (cell == player.other().symbol) {
                    foundOpponent = true
                } else if (cell == player.symbol) {
                    if (foundOpponent) return true
                    break
                } else {
                    break
                }
                r += dr
                c += dc
            }
        }
        return false
    }

    /**
     * Applies a legal move and returns a new board state with flipped pieces.
     *
     * If the move is invalid, `null` is returned and no changes are applied.
     * On success, the returned board:
     * - Sets `(row, col)` to `player.symbol`,
     * - Flips all opponent pieces that are bracketed in any valid direction.
     *
     * @param row Target row index (0-based).
     * @param col Target column index (0-based).
     * @param player The player making the move.
     * @return A new `Board` reflecting the move, or `null` if the move is illegal.
     */
    fun applyMove(row: Int, col: Int, player: Player): Board? {
        if (!isValidMove(row, col, player)) return null
        val newGrid = grid.map { it.toMutableList() }.toMutableList()
        newGrid[row][col] = player.symbol
        val allFlips = mutableSetOf<Pair<Int, Int>>()
        for ((dr, dc) in directions) {
            val flips = mutableListOf<Pair<Int, Int>>()
            var r = row + dr
            var c = col + dc
            while (inBounds(r, c) && getCell(r, c) == player.other().symbol) {
                flips.add(r to c)
                r += dr
                c += dc
            }
            if (inBounds(r, c) && getCell(r, c) == player.symbol && flips.isNotEmpty()) {
                allFlips.addAll(flips)
            }
        }
        allFlips.forEach { (fr, fc) -> newGrid[fr][fc] = player.symbol }
        return copy(grid = newGrid.map { it.toList() })
    }

    /**
     * Enumerates all legal moves for `player` on the current board.
     *
     * @param player The player for whom valid moves are searched.
     * @return A list of `(row, col)` pairs representing valid target cells.
     */
    fun getValidMoves(player: Player): List<Pair<Int, Int>> {
        val moves = mutableListOf<Pair<Int, Int>>()
        for (r in 0 until size) for (c in 0 until size) {
            if (isValidMove(r, c, player)) moves.add(r to c)
        }
        return moves
    }
}