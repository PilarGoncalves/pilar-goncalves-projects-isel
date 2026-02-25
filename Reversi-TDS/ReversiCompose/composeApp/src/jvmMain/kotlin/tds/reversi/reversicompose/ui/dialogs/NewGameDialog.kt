package tds.reversi.reversicompose.ui.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import tds.reversi.reversicompose.model.Player
import tds.reversi.reversicompose.ui.board.Disc
import androidx.compose.ui.platform.testTag
import tds.reversi.reversicompose.ui.UiTags

/**
 * Composable function to display a dialog for starting a new game.
 *
 * This dialog allows the player to select whether they want to play multiplayer or local,
 * specify a game name if multiplayer is selected, and choose their player color (black or white).
 * Once the player has made their selections, they can confirm to start the game, or cancel to dismiss the dialog.
 *
 * The dialog includes:
 * - A checkbox for selecting multiplayer mode.
 * - A text field for entering the game name if multiplayer is selected.
 * - Radio buttons for selecting the player color.
 *
 * @param onDismiss A lambda function that is called when the dialog is dismissed (either by pressing "Cancel" or outside the dialog).
 * @param onStartGame A lambda function that is called when the player confirms the action to start a new game.
 *                    It takes the multiplayer flag, game name, and player color as parameters.
 */
@Composable
fun NewGameDialog(onDismiss: () -> Unit, onStartGame: (Boolean, String, Player) -> Unit) {
    var isMultiplayer by remember { mutableStateOf(false) }
    var gameName by remember { mutableStateOf("") }
    var playerColor by remember { mutableStateOf<Player?>(null) }

    val isStartEnabled = playerColor != null && (!isMultiplayer || gameName.trim().isNotEmpty())

    AlertDialog(
        modifier = Modifier.testTag(UiTags.NewGameDialog),
        onDismissRequest = onDismiss,
        title = { Text("New Game") },
        text = {
            Column {
                Row {
                    Checkbox(checked = isMultiplayer,
                        onCheckedChange = { isMultiplayer = it },
                        modifier = Modifier.testTag(UiTags.NewGameMultiplayerCheck))
                    Spacer(Modifier.width(8.dp))
                    Text("Multiplayer")
                }

                Spacer(Modifier.height(16.dp))

                if (isMultiplayer) {
                    Text("Enter the game name:")
                    TextField(
                        value = gameName,
                        onValueChange = { gameName = it },
                        label = { Text("Game Name") },
                        modifier = Modifier.testTag(UiTags.NewGameNameField)
                    )
                }

                Spacer(Modifier.height(16.dp))

                Text("Select Player:")

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = playerColor == Player.BLACK,
                            onClick = { playerColor = Player.BLACK },
                            modifier = Modifier.testTag(UiTags.NewGamePickBlack)
                        )
                        Spacer(Modifier.width(8.dp))
                        Disc(Color.Black, 40.dp)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = playerColor == Player.WHITE,
                            onClick = { playerColor = Player.WHITE },
                            modifier = Modifier.testTag(UiTags.NewGamePickWhite)
                        )
                        Spacer(Modifier.width(8.dp))
                        Disc(Color.White, 40.dp)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (playerColor != null) {
                        onStartGame(isMultiplayer, gameName.trim(), playerColor!!)
                    }
                    onDismiss()
                },
                enabled = isStartEnabled,
                modifier = Modifier.testTag(UiTags.NewGameStart)
            ) {
                Text("Start Game")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss,
                modifier = Modifier.testTag(UiTags.NewGameCancel)) {
                Text("Cancel")
            }
        }
    )
}
