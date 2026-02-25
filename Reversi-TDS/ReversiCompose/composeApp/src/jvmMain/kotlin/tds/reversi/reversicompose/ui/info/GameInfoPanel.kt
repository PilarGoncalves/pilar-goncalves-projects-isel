package tds.reversi.reversicompose.ui.info

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import tds.reversi.reversicompose.domain.Utils.isMultiplayer
import tds.reversi.reversicompose.model.Game
import tds.reversi.reversicompose.model.Player
import tds.reversi.reversicompose.ui.UiTags
import tds.reversi.reversicompose.ui.board.Disc
import tds.reversi.reversicompose.viewmodel.ReversiViewModel

/**
 * Composable that displays game-related information alongside the board.
 *
 * This panel shows:
 * - The player's color in multiplayer games,
 * - Whose turn it is,
 * - A waiting message when it is the opponent’s turn,
 * - The current score for both players,
 * - A toggle to enable or disable the visualization of valid move targets.
 *
 * The panel reacts to changes in the [Game] state and delegates user actions
 * (such as toggling targets) to the [ReversiViewModel].
 *
 * @param game Current game instance containing all relevant game state.
 * @param viewModel ViewModel used to handle UI actions and game logic.
 */
@Composable
fun GameInfoPanel(game: Game, viewModel: ReversiViewModel) {
    Column(
        modifier = Modifier
            .testTag(UiTags.TargetsPanel)
            .width(320.dp)
            .padding(top = 40.dp, end = 40.dp)
            .border(2.dp, Color(0xFF6A4CA3), shape = RoundedCornerShape(16.dp))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(28.dp),
        horizontalAlignment = Alignment.Start
    ) {
        if (isMultiplayer(game)) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "You are",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFF6A4CA3)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        if (game.myPlayer == Player.BLACK) "Black" else "White",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(Modifier.width(12.dp))
                    Disc(
                        if (game.myPlayer == Player.BLACK) Color.Black else Color.White,
                        size = 40.dp
                    )
                }
            }

            HorizontalDivider(color = Color(0x336A4CA3))
        }
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                "Turn",
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFF6A4CA3)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    if (game.currentPlayer == Player.BLACK) "Black" else "White",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(Modifier.width(12.dp))
                Disc(
                    if (game.currentPlayer == Player.BLACK) Color.Black else Color.White,
                    size = 40.dp
                )
            }
        }
        if (viewModel.shouldShowWaitingMessage()) {
            Text(
                "Waiting for opponent…",
                modifier = Modifier.testTag(UiTags.WaitingMessage),
                color = Color(0xFF8A63EC),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        HorizontalDivider(color = Color(0x336A4CA3))

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                "Score",
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFF6A4CA3)
            )

            val score = game.board.score()

            Row(verticalAlignment = Alignment.CenterVertically) {
                Disc(Color.Black, size = 35.dp)
                Spacer(Modifier.width(10.dp))
                Text("${score[Player.BLACK]}", style = MaterialTheme.typography.bodyLarge)
            }

            Spacer(Modifier.height(1.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Disc(Color.White, size = 35.dp)
                Spacer(Modifier.width(8.dp))
                Text("${score[Player.WHITE]}", style = MaterialTheme.typography.bodyLarge)
            }
        }

        HorizontalDivider(color = Color(0x336A4CA3))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Show targets")
            Spacer(Modifier.width(12.dp))
            Switch(
                checked = game.showTargets,
                onCheckedChange = { viewModel.toggleTargets() },
                modifier = Modifier.testTag(UiTags.SwitchTargets),
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(0xFF6A4CA3),
                    checkedTrackColor = Color(0xFF6A4CA3).copy(alpha = 0.5f)
                )
            )
        }
    }
}