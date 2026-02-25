package tds.reversi.reversicompose.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test
import kotlin.test.assertTrue
import tds.reversi.reversicompose.ui.dialogs.GameOverDialog
import tds.reversi.reversicompose.model.Game
import tds.reversi.reversicompose.model.Player

@OptIn(ExperimentalTestApi::class)
class GameOverDialogTests {

    @Test
    fun `game over dialog shows winner message`() = runComposeUiTest {
        val game = Game.createInitial(myPlayer = Player.BLACK)

        setContent {
            GameOverDialog(game = game, onBackToMenu = {})
        }

        onNodeWithTag(UiTags.GameOverDialog).assertExists()
        onNodeWithText(game.getWinnerMessage()).assertExists()
    }

    @Test
    fun `back button calls callback`() = runComposeUiTest {
        var called = false
        val game = Game.createInitial(myPlayer = Player.BLACK)

        setContent {
            GameOverDialog(
                game = game,
                onBackToMenu = { called = true }
            )
        }

        onNodeWithTag(UiTags.GameOverBack).performClick()
        assertTrue(called)
    }
}
