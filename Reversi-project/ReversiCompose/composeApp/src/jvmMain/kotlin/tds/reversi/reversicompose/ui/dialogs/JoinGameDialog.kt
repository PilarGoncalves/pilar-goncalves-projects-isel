package tds.reversi.reversicompose.ui.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.testTag
import tds.reversi.reversicompose.ui.UiTags

/**
 * Composable function to display a dialog for joining an existing game.
 *
 * This dialog allows the player to enter the name of a game they wish to join.
 * The player can then either confirm the action to join the game or cancel and close the dialog.
 *
 * The game name input is validated to ensure it is not empty, and the "Join Game" button is only enabled when a valid game name is entered.
 *
 * @param onDismiss A lambda function that is called when the dialog is dismissed (either by pressing "Cancel" or outside the dialog).
 * @param onJoinGame A lambda function that is called when the player confirms the action to join a game.
 *                   It takes the game name as a parameter and triggers the joining process.
 */
@Composable
fun JoinGameDialog(onDismiss: () -> Unit, onJoinGame: (String) -> Unit) {
    var gameName by remember { mutableStateOf("") }

    val isJoinEnabled = gameName.trim().isNotEmpty()

    AlertDialog(
        modifier = Modifier.testTag(UiTags.JoinGameDialog),
        onDismissRequest = onDismiss,
        title = { Text("Join Game") },
        text = {
            Column {
                Text("Enter the game name to join:")

                Spacer(Modifier.height(16.dp))

                TextField(
                    value = gameName,
                    onValueChange = { gameName = it },
                    label = { Text("Game Name") },
                    modifier = Modifier.fillMaxWidth().testTag(UiTags.JoinGameNameField)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (isJoinEnabled) {
                        onJoinGame(gameName.trim())
                    }
                    onDismiss()
                },
                enabled = isJoinEnabled,
                modifier = Modifier.testTag(UiTags.JoinGameConfirm)
            ) {
                Text("Join Game")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, modifier = Modifier.testTag(UiTags.JoinGameCancel)) {
                Text("Cancel")
            }
        }
    )
}
