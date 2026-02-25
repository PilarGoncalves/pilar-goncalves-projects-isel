package tds.reversi.reversicompose.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tds.reversi.reversicompose.ui.animations.AnimatedBackground
import androidx.compose.ui.platform.testTag

/**
 * Composable function for the initial screen of the Reversi game.
 *
 * This screen is shown when the game starts, allowing the user to choose whether to start a new game, join an existing game, or exit.
 * It features a background animation and buttons for the available actions.
 *
 * @param onNewGame Lambda function to start a new game when the "New Game" button is pressed.
 * @param onJoinGame Lambda function to join an existing game when the "Join Game" button is pressed.
 * @param onExit Lambda function to exit the application when the "Exit" button is pressed.
 */
@Composable
fun InitialScreen(
    onNewGame: () -> Unit,
    onJoinGame: () -> Unit,
    onExit: () -> Unit
) {

    Box(Modifier.fillMaxSize()
        .testTag(UiTags.InitialScreen)
        , contentAlignment = Alignment.Center) {

        AnimatedBackground()

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "REVERSI",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 4.sp,
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.4f),
                        offset = Offset(4f, 4f),
                        blurRadius = 8f
                    )
                ),
                color = Color(0xFF5B3F95)
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = onNewGame,
                modifier = Modifier.padding(8.dp).testTag(UiTags.BtnNewGame),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6A4CA3)
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("New Game", color = Color.White)
            }

            Button(
                onClick = onJoinGame,
                modifier = Modifier.padding(8.dp).testTag(UiTags.BtnJoinGame),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6A4CA3)
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Join Game", color = Color.White)
            }

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = onExit,
                modifier = Modifier.padding(8.dp).testTag(UiTags.BtnExit),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6A4CA3)
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Exit", color = Color.White)
            }
        }
    }
}