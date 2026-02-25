package tds.reversi.reversicompose.ui.dialogs

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.testTag
import tds.reversi.reversicompose.ui.UiTags
import androidx.compose.ui.Modifier

/**
 * Composable function to display an error dialog.
 *
 * This dialog is used to show error messages to the user, typically when an invalid action is performed in the game.
 * The dialog includes a message explaining the error and a confirmation button (OK) to dismiss the dialog.
 * The `message` is provided as an argument, allowing dynamic error messages to be shown.
 *
 * @param message The error message to display in the dialog.
 *                This message explains what went wrong or provides guidance to the user.
 * @param onDismiss A lambda function that will be invoked when the dialog is dismissed.
 *                  It is typically used to hide or close the dialog.
 */
@Composable
fun ErrorDialog(
    message: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        modifier = Modifier.testTag(UiTags.ErrorDialog),
        onDismissRequest = onDismiss,
        title = { Text("Invalid action") },
        text = { Text(message) },
        confirmButton = {
            Button(onClick = onDismiss, modifier = Modifier.testTag(UiTags.ErrorDialogOk)) {
                Text("OK")
            }
        }
    )
}
