package tds.reversi.reversicompose.ui.board

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Composable function to display a target marker on the board.
 *
 * This function creates a small circular marker, typically used to indicate valid moves on the Reversi board.
 * The marker is displayed in yellow to make it stand out and help the player visualize where they can place their piece.
 *
 * The size of the marker is fixed to 16dp, and the shape is a circle.
 *
 * @param modifier Optional modifier applied to the target container.
 */
@Composable
fun Target(modifier: Modifier = Modifier) {
    Box(
        modifier
            .size(16.dp)
            .background(Color.Yellow, CircleShape)
    )
}


