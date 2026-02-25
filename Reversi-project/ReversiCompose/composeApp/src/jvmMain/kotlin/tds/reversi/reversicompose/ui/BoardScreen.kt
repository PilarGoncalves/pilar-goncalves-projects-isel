package tds.reversi.reversicompose.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import tds.reversi.reversicompose.model.Game
import tds.reversi.reversicompose.ui.board.BoardView
import tds.reversi.reversicompose.ui.dialogs.GameOverDialog
import tds.reversi.reversicompose.ui.info.GameInfoPanel
import tds.reversi.reversicompose.ui.menu.GameMenu
import tds.reversi.reversicompose.ui.topbar.TopBar
import tds.reversi.reversicompose.viewmodel.ReversiViewModel

/**
 * Main screen responsible for displaying the Reversi game board and all related UI components.
 *
 * This screen composes:
 * - The top bar (navigation and menu toggle),
 * - The side game menu,
 * - The board itself,
 * - The game information panel (turn, score, options),
 * - The game over dialog when the match ends.
 *
 * UI state such as menu visibility is handled locally, while all game logic
 * and navigation actions are delegated to the [ReversiViewModel].
 *
 * @param game Current game instance containing the full game state.
 * @param viewModel ViewModel responsible for handling user actions and game logic.
 * @param onNewGame Callback invoked when the user chooses to start a new game.
 * @param onJoinGame Callback invoked when the user chooses to join an existing game.
 * @param onExit Callback invoked when the user exits the application.
 */
@Composable
fun BoardScreen(
    game: Game,
    viewModel: ReversiViewModel,
    onNewGame: () -> Unit,
    onJoinGame: () -> Unit,
    onExit: () -> Unit
) {

    var isMenuOpen by remember { mutableStateOf(true) }

    Column(Modifier.fillMaxSize().testTag(UiTags.BoardScreen)) {

        TopBar(
            game = game,
            onBack = { viewModel.exitGame() },
            onToggleMenu = { isMenuOpen = !isMenuOpen }
        )

        Row(
            Modifier
                .fillMaxSize()
                .background(Color(0xFFE4DFF1))
                .testTag(UiTags.BoardBody)
        ) {
            Box(
                modifier = Modifier
                    .testTag(UiTags.GameMenuContainer)
                    .width(260.dp)
                    .fillMaxHeight()
            ) {
                if (isMenuOpen) {
                    GameMenu(viewModel, onNewGame, onJoinGame, onExit = {
                        viewModel.exitGame()
                        onExit()
                    })
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                BoardView(game, viewModel)
            }

            GameInfoPanel(game, viewModel)
        }
    }
    if (viewModel.isGameOver()) {
        GameOverDialog(
            game = game,
            onBackToMenu = { viewModel.exitGame() }
        )
    }
}

