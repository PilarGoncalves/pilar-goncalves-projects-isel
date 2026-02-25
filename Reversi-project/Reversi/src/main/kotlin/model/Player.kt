package model

const val BLACK_SYMBOL = '#'
const val WHITE_SYMBOL = '@'

/**
 * Represents the two possible players in the game: BLACK and WHITE.
 * Each player has a corresponding symbol used on the board.
 * The enum also provides functionality for determining the opponent player.
 *
 * @property symbol The symbol associated with the player, used on the board.
 */
enum class Player(internal val symbol: Char) {
    BLACK(BLACK_SYMBOL),
    WHITE(WHITE_SYMBOL);

    /**
     * Returns the opposite player of the current player.
     *
     * @return The player opposite to the current one (if the current player is BLACK, return WHITE, and vice versa).
     */
    fun other(): Player = if (this == BLACK) WHITE else BLACK

    companion object {
        /**
         * Converts a symbol to a Player.
         * This function is used to retrieve the corresponding `Player` enum value based on the given symbol.
         * It throws an exception if the symbol is invalid (i.e., not '# or '@').
         *
         * @param sym The symbol representing the player ('#' for BLACK, '@' for WHITE).
         * @return The corresponding `Player` object.
         * @throws IllegalArgumentException If the symbol is invalid.
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