package tds.reversi.reversicompose.model

const val BLACK_SYMBOL = '#'
const val WHITE_SYMBOL = '@'

/**
 * Represents the two possible players in the game: BLACK and WHITE.
 * Each player has a corresponding symbol used on the board.
 * The enum also provides functionality for determining the opponent player.
 *
 * The `Player` enum represents the two players in the game: one with the `BLACK_SYMBOL` (`#`) and the other with the `WHITE_SYMBOL` (`@`).
 * It includes methods to retrieve the opposite player and convert symbols to players.
 *
 * @property symbol The symbol associated with the player, used on the board.
 */
enum class Player(internal val symbol: Char) {
    BLACK(BLACK_SYMBOL),
    WHITE(WHITE_SYMBOL);

    /**
     * Returns the opposite player of the current player.
     *
     * This function is used to switch turns between players. If the current player is BLACK,
     * it returns WHITE, and vice versa.
     *
     * @return The player opposite to the current one (if the current player is BLACK, return WHITE, and vice versa).
     */
    fun other(): Player = if (this == BLACK) WHITE else BLACK

    companion object {
        /**
         * Converts a symbol to a Player.
         * This function is used to retrieve the corresponding `Player` enum value based on the given symbol.
         * It throws an exception if the symbol is invalid (i.e., not `#` or `@`).
         *
         * @param sym The symbol representing the player (`#` for BLACK, `@` for WHITE).
         * @return The corresponding `Player` object.
         * @throws IllegalArgumentException If the symbol is invalid.
         *         This exception is thrown when an invalid symbol is provided (i.e., something other than `#` or `@`).
         */
        fun fromSymbol(sym: Char): Player {
            return when (sym) {
                BLACK_SYMBOL -> BLACK
                WHITE_SYMBOL -> WHITE
                else -> throw IllegalArgumentException("Invalid symbol: $sym. Use $BLACK_SYMBOL or $WHITE_SYMBOL.")
            }
        }
    }
}
