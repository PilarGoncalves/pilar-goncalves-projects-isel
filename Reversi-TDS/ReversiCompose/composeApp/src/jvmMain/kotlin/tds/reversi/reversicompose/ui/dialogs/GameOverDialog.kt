package tds.reversi.reversicompose.ui.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tds.reversi.reversicompose.model.Game
import androidx.compose.ui.platform.testTag
import tds.reversi.reversicompose.ui.UiTags

/**
 * Composable function to display a game over dialog.
 *
 * This dialog is shown when the game ends, either with a winner or a tie.
 * It displays the result of the game, such as who the winner is or if it was a tie, and provides a button to
 * return to the initial menu or main screen.
 *
 * The dialog includes the game result and an option for the player to navigate back to the main menu.
 *
 * @param game The current game instance, used to get the winner message.
 *             This provides the necessary details about the game result.
 * @param onBackToMenu A lambda function that is called when the player clicks the "Back to Initial Menu" button.
 *                     It typically takes the player back to the main menu or initial screen.
 */
@Composable
fun GameOverDialog(
    game: Game,
    onBackToMenu: () -> Unit
) {
    AlertDialog(
        modifier = Modifier.testTag(UiTags.GameOverDialog),
        onDismissRequest = {},
        title = { Text("Game Over") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                Text(
                    text = game.getWinnerMessage(),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        },
        confirmButton = {
            Button(onClick = onBackToMenu,
                modifier = Modifier.testTag(UiTags.GameOverBack)) {
                Text("Back to Initial Menu")
            }
        }
    )
}

