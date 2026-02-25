package tds.reversi.reversicompose.ui

import androidx.compose.material3.*
import androidx.compose.runtime.*
import tds.reversi.reversicompose.ui.dialogs.JoinGameDialog
import tds.reversi.reversicompose.ui.dialogs.NewGameDialog
import tds.reversi.reversicompose.ui.dialogs.ErrorDialog
import tds.reversi.reversicompose.viewmodel.ReversiViewModel

/**
 * Root composable of the Reversi application.
 *
 * This composable acts as the main entry point for the UI layer and is responsible for:
 * - Holding a single instance of [ReversiViewModel],
 * - Observing the UI state exposed by the ViewModel,
 * - Deciding which screen to display (initial screen or board screen),
 * - Displaying modal dialogs (errors, new game, join game),
 * - Delegating all game actions to the ViewModel.
 *
 * Navigation in the application is state-based: the presence or absence of a [Game]
 * in the UI state determines which screen is rendered.
 *
 * @param onExitApp Callback invoked when the user chooses to exit the application.
 * @param viewModel ViewModel instance used to manage game logic and UI state.
 *                  A default instance is created and remembered if not provided.
 */
@Composable
fun ReversiApp(onExitApp: () -> Unit,
               viewModel: ReversiViewModel = remember { ReversiViewModel() }
) {
    val state = viewModel.uiState
    val game = state.game
    val error = state.errorMessage

    if (error != null) {
        ErrorDialog(
            message = error,
            onDismiss = { viewModel.clearError() }
        )
    }

    var showNewGameDialog by remember { mutableStateOf(false) }
    var showJoinGameDialog by remember { mutableStateOf(false) }

    MaterialTheme {
        if (game == null) {
            InitialScreen(
                onNewGame = { showNewGameDialog = true },
                onJoinGame = { showJoinGameDialog = true },
                onExit = onExitApp
            )
        } else {
            BoardScreen(
                game = game,
                viewModel = viewModel,
                onNewGame = { showNewGameDialog = true },
                onJoinGame = { showJoinGameDialog = true },
                onExit = onExitApp
            )
        }

        if (showNewGameDialog) {
            NewGameDialog(
                onDismiss = { showNewGameDialog = false },
                onStartGame = { isMulti, name, player ->
                    if (isMulti) viewModel.startMultiplayerGame(name, player)
                    else viewModel.startLocalGame(player)
                }
            )
        }

        if (showJoinGameDialog) {
            JoinGameDialog(
                onDismiss = { showJoinGameDialog = false },
                onJoinGame = { viewModel.joinGame(it) }
            )
        }
    }
}