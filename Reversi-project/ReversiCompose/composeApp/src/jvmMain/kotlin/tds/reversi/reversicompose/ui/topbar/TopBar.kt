package tds.reversi.reversicompose.ui.topbar


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import tds.reversi.reversicompose.domain.Utils.isMultiplayer
import tds.reversi.reversicompose.model.Game
import tds.reversi.reversicompose.ui.UiTags

/**
 * Composable that displays the top application bar for the board screen.
 *
 * The top bar provides:
 * - A menu button to toggle the side menu visibility,
 * - A back button to return to the initial menu / exit the current game,
 * - A centered title showing the game name in multiplayer mode, or "Reversi" for local games.
 *
 * User interactions are delegated through callbacks to keep this composable stateless.
 *
 * @param game Current game instance used to decide which title should be shown.
 * @param onBack Callback invoked when the back button is pressed.
 * @param onToggleMenu Callback invoked when the menu button is pressed.
 */
@Composable
fun TopBar(
    game: Game,
    onBack: () -> Unit,
    onToggleMenu: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(Color(0xFF24243B))
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(onClick = onToggleMenu, modifier = Modifier.testTag(UiTags.TopBarMenu)) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                tint = Color.White
            )
        }

        IconButton(onClick = onBack, modifier = Modifier.testTag(UiTags.TopBarBack)) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }

        Spacer(Modifier.weight(1f))

        Text(
            text = if (isMultiplayer(game)) game.name else "Reversi",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White
        )

        Spacer(Modifier.weight(1f))
    }
}
