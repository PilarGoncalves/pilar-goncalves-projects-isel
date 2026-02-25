package tds.reversi.reversicompose.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import tds.reversi.reversicompose.ui.dialogs.JoinGameDialog

@OptIn(ExperimentalTestApi::class)
class JoinGameDialogTests {

    @Test
    fun `join disabled when name is empty`() = runComposeUiTest {
        setContent {
            JoinGameDialog(onDismiss = {}, onJoinGame = {})
        }

        onNodeWithTag(UiTags.JoinGameConfirm).assertIsNotEnabled()
    }

    @Test
    fun `join enabled when name is not blank`() = runComposeUiTest {
        setContent {
            JoinGameDialog(onDismiss = {}, onJoinGame = {})
        }

        onNodeWithTag(UiTags.JoinGameNameField).performTextInput("game1")
        onNodeWithTag(UiTags.JoinGameConfirm).assertIsEnabled()
    }

    @Test
    fun `join trims name and calls callback`() = runComposeUiTest {
        var received: String? = null

        setContent {
            JoinGameDialog(
                onDismiss = {},
                onJoinGame = { received = it }
            )
        }

        onNodeWithTag(UiTags.JoinGameNameField)
            .performTextInput("  myGame  ")

        onNodeWithTag(UiTags.JoinGameConfirm).performClick()

        assertEquals("myGame", received)
    }

    @Test
    fun `cancel calls onDismiss`() = runComposeUiTest {
        var dismissed = false

        setContent {
            JoinGameDialog(
                onDismiss = { dismissed = true },
                onJoinGame = {}
            )
        }

        onNodeWithTag(UiTags.JoinGameCancel).performClick()

        assertTrue(dismissed)
    }
}
