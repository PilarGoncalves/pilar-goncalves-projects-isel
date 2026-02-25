package tds.reversi.reversicompose.ui.board

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import tds.reversi.reversicompose.model.BLACK_SYMBOL
import tds.reversi.reversicompose.model.Game
import tds.reversi.reversicompose.model.WHITE_SYMBOL
import tds.reversi.reversicompose.ui.UiTags
import tds.reversi.reversicompose.viewmodel.ReversiViewModel

/**
 * Composable function that renders the Reversi board grid.
 *
 * This component is responsible for:
 * - Displaying column headers (A, B, C, ...),
 * - Displaying row headers (1, 2, 3, ...),
 * - Rendering each cell with its current content (black disc, white disc, or empty),
 * - Handling user interaction by calling [ReversiViewModel.playAt] when a cell is clicked,
 * - Optionally showing target markers for valid moves when targets are enabled.
 *
 * @param game Current game instance containing the board state and UI options (e.g., show targets).
 * @param viewModel ViewModel used to trigger actions (play moves) and to validate UI behaviour.
 */
@Composable
fun BoardView(game: Game, viewModel: ReversiViewModel) {
    val board = game.board
    val size = board.size

    Column(
        modifier = Modifier.testTag(UiTags.Board),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row {
            Spacer(Modifier.width(50.dp))
            repeat(size) {
                Text(
                    ('A' + it).toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.width(85.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(Modifier.height(6.dp))

        repeat(size) { row ->
            Row {
                Text(
                    (row + 1).toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .width(50.dp)
                        .padding(4.dp)
                        .align(Alignment.CenterVertically),
                    textAlign = TextAlign.End
                )

                Spacer(Modifier.width(6.dp))

                repeat(size) { col ->
                    Box(
                        modifier = Modifier
                            .testTag(UiTags.cell(row, col))
                            .size(85.dp)
                            .border(1.dp, Color.Black)
                            .background(Color(0xFF2E7D32))
                            .clickable { viewModel.playAt(row, col) },
                        contentAlignment = Alignment.Center
                    ) {
                        when (board.getCell(row, col)) {
                            BLACK_SYMBOL -> Disc(Color.Black, 65.dp)
                            WHITE_SYMBOL -> Disc(Color.White, 65.dp)
                            else ->
                                if (
                                    game.showTargets &&
                                    viewModel.canShowTargets() &&
                                    board.isValidMove(row, col, game.currentPlayer)
                                ) {
                                    Target(modifier = Modifier.testTag(UiTags.target(row, col)))
                                }
                        }
                    }
                }
            }
        }
    }
}
