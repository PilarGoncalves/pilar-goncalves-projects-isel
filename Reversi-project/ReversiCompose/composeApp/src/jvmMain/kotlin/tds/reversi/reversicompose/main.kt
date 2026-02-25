package tds.reversi.reversicompose

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import tds.reversi.reversicompose.ui.ReversiApp

/**
 * The main entry point for the Reversi application.
 *
 * This function sets up and launches the application window. It uses the Jetpack Compose `application` function
 * to initialize the UI and display the game window. The window is configured to be maximized by default,
 * and the application title and icon are set.
 *
 * @return The application window that displays the Reversi game.
 */
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Reversi",
        icon = painterResource("icon.png"),
        state = rememberWindowState(
            placement = WindowPlacement.Maximized
        )
    ) {
        ReversiApp(onExitApp = ::exitApplication)
    }
}
