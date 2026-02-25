package tds.reversi.reversicompose.ui.board

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

/**
 * Composable function to display a circular disc on the board.
 *
 * This function creates a circular shape (a disc) that can be used to represent a player's piece (either black or white)
 * on the Reversi board. The disc's color and size are customizable.
 *
 * @param color The color of the disc (e.g., black or white).
 *              This determines the color of the piece on the board.
 * @param size The size of the disc, typically used to adjust the piece's visual size on the board.
 *             The size is provided in Dp (density-independent pixels).
 */
@Composable
fun Disc(color: Color, size: Dp) {
    Box(
        Modifier
            .size(size)
            .background(color, CircleShape)
    )
}
