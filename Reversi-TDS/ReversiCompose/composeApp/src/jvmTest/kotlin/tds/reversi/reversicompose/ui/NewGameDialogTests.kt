package tds.reversi.reversicompose.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import tds.reversi.reversicompose.ui.dialogs.NewGameDialog

@OptIn(ExperimentalTestApi::class)
class NewGameDialogTests {

    @Test
    fun `start disabled until player selected`() = runComposeUiTest {
        setContent {
            NewGameDialog(onDismiss = {}, onStartGame = { _, _, _ -> })
        }

        onNodeWithTag(UiTags.NewGameStart).assertIsNotEnabled()

        onNodeWithTag(UiTags.NewGamePickBlack).performClick()
        onNodeWithTag(UiTags.NewGameStart).assertIsEnabled()
    }

    @Test
    fun `multiplayer requires game name`() = runComposeUiTest {
        setContent {
            NewGameDialog(onDismiss = {}, onStartGame = { _, _, _ -> })
        }

        onNodeWithTag(UiTags.NewGamePickWhite).performClick()
        onNodeWithTag(UiTags.NewGameMultiplayerCheck).performClick()

        onNodeWithTag(UiTags.NewGameStart).assertIsNotEnabled()

        onNodeWithTag(UiTags.NewGameNameField).performTextInput("game1")
        onNodeWithTag(UiTags.NewGameStart).assertIsEnabled()
    }

    @Test
    fun `start calls onStartGame with expected args in multiplayer`() = runComposeUiTest {
        var called = false
        var gotIsMulti: Boolean? = null
        var gotName: String? = null
        var gotPlayer: Any? = null

        setContent {
            NewGameDialog(
                onDismiss = {},
                onStartGame = { isMulti, name, player ->
                    called = true
                    gotIsMulti = isMulti
                    gotName = name
                    gotPlayer = player
                }
            )
        }

        onNodeWithTag(UiTags.NewGameMultiplayerCheck).performClick()
        onNodeWithTag(UiTags.NewGameNameField).performTextInput("  room  ")
        onNodeWithTag(UiTags.NewGamePickBlack).performClick()

        onNodeWithTag(UiTags.NewGameStart).performClick()

        assertTrue(called)
        assertEquals(true, gotIsMulti)
        assertEquals("room", gotName)
        assertTrue(gotPlayer != null)
    }

    @Test
    fun `cancel calls onDismiss`() = runComposeUiTest {
        var dismissed = false

        setContent {
            NewGameDialog(
                onDismiss = { dismissed = true },
                onStartGame = { _, _, _ -> }
            )
        }

        onNodeWithTag(UiTags.NewGameCancel).performClick()
        assertTrue(dismissed)
    }
}
