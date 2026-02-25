package view

import model.Board
import model.Player
import model.BOARD_SIDE
import model.TARGET
import model.WHITE_SYMBOL
import model.BLACK_SYMBOL

/**
 * Displays the board in a formatted string representation.
 * Optionally shows the valid moves as '*' for the current player.
 *
 * @param board the current game board to display.
 * @param showTargets true if valid moves should be shown, false otherwise.
 * @param player the current player (used to highlight valid moves).
 */
fun display(board: Board, showTargets: Boolean = false, player: Player? = null) {
    val header = ('A'..'Z').take(BOARD_SIDE).joinToString(" ")
    val sb = StringBuilder()
    sb.append("   ").append(header).append('\n')
    for (r in 0 until BOARD_SIDE) {
        sb.append(String.format("%2d ", r + 1))
        for (c in 0 until BOARD_SIDE) {
            val cell = if (showTargets && player != null && board.isEmpty(r, c) && board.isValidMove(r, c, player)) {
                TARGET
            } else {
                board.getCell(r, c)
            }
            sb.append(cell).append(' ')
        }
        sb.append('\n')
    }
    println(sb.toString().trimEnd())
    val s = board.score()
    println("$BLACK_SYMBOL = ${s[Player.BLACK]} | $WHITE_SYMBOL = ${s[Player.WHITE]}")
    println("Turn: ${player?.symbol ?: "N/A"}")
}