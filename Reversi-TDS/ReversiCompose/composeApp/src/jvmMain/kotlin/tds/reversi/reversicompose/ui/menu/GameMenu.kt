package tds.reversi.reversicompose.ui.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tds.reversi.reversicompose.viewmodel.ReversiViewModel
import androidx.compose.ui.platform.testTag
import tds.reversi.reversicompose.ui.UiTags

/**
 * Composable that renders the in-game side menu.
 *
 * This menu provides access to:
 * - Game management actions (new game, join game, refresh, exit),
 * - Gameplay actions (pass turn),
 * - Configuration options (auto-refresh toggle).
 *
 * Menu items are enabled or disabled dynamically based on the current
 * game state, with all logic delegated to the [ReversiViewModel].
 *
 * @param viewModel ViewModel responsible for validating actions and handling menu events.
 * @param onNewGame Callback invoked when the user chooses to start a new game.
 * @param onJoinGame Callback invoked when the user chooses to join an existing game.
 * @param onExit Callback invoked when the user chooses to exit the application.
 */
@Composable
fun GameMenu(
    viewModel: ReversiViewModel,
    onNewGame: () -> Unit,
    onJoinGame: () -> Unit,
    onExit: () -> Unit
) {

    Column(
        modifier = Modifier
            .testTag(UiTags.GameMenu)
            .width(260.dp)
            .fillMaxHeight()
            .background(Color(0xFF343455))
            .padding(horizontal = 22.dp, vertical = 40.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            "Game",
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFF8A63EC),
            letterSpacing = 2.sp
        )

        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text(
                "New",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                modifier = Modifier.testTag(UiTags.MenuNew).clickable { onNewGame() }
            )

            Text(
                "Join",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                modifier = Modifier.testTag(UiTags.MenuJoin).clickable { onJoinGame() }
            )

            Text(
                "Refresh",
                style = MaterialTheme.typography.titleLarge,
                color = if (viewModel.canRefresh()) Color.White else Color.Gray,
                modifier = Modifier.testTag(UiTags.MenuRefresh).clickable(enabled = viewModel.canRefresh()) {
                    viewModel.refreshGame()
                }
            )
            Text("Exit",
                style = MaterialTheme.typography.titleLarge, color = Color.White,
                modifier = Modifier.testTag(UiTags.MenuExit).clickable { onExit() }
            )
        }

        HorizontalDivider(color = Color(0x33FFFFFF))

        Text(
            "Play",
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFF8A63EC),
            letterSpacing = 2.sp
        )

        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text(
                "Pass",
                style = MaterialTheme.typography.titleLarge,
                color = if (viewModel.canPass()) Color.White else Color.Gray,
                modifier = Modifier.testTag(UiTags.MenuPass).clickable(enabled = viewModel.canPass()) {
                    viewModel.passTurn()
                }
            )
        }

        HorizontalDivider(color = Color(0x33FFFFFF))

        Text(
            "Options",
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFF8A63EC),
            letterSpacing = 2.sp
        )

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

            Text(
                "Auto-refresh",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(UiTags.MenuAutoRefresh)
                    .height(44.dp)
                    .background(
                        when {
                            !viewModel.canToggleAutoRefresh() ->
                                Color(0xFF24243B).copy(alpha = 0.5f)

                            viewModel.isAutoRefreshEnabled() ->
                                Color(0xFF6A4CA3).copy(alpha = 0.25f)

                            else ->
                                Color(0xFF24243B)
                        },
                        RoundedCornerShape(8.dp)
                    )
                    .border(
                        1.dp,
                        if (!viewModel.canToggleAutoRefresh())
                            Color.Gray.copy(alpha = 0.6f)
                        else
                            Color(0xFF8A63EC),
                        RoundedCornerShape(8.dp)
                    )
                    .clickable(enabled = viewModel.canToggleAutoRefresh()) {
                        viewModel.toggleAutoRefresh()
                    }
                    .padding(horizontal = 14.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    if (viewModel.isAutoRefreshEnabled()) "On" else "Off",
                    color = when {
                        !viewModel.canToggleAutoRefresh() ->
                            Color.Gray

                        viewModel.isAutoRefreshEnabled() ->
                            Color(0xFF6A4CA3)

                        else ->
                            Color(0xFF8A63EC)
                    }
                )
            }
        }
    }
}