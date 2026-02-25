package tds.reversi.reversicompose.ui

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class InitialScreenTests {

    @Test
    fun `initial screen shows buttons`() = runComposeUiTest {
        setContent {
            InitialScreen(onNewGame = {}, onJoinGame = {}, onExit = {})
        }

        onNodeWithTag(UiTags.InitialScreen).assertExists()
        onNodeWithTag(UiTags.BtnNewGame).assertExists()
        onNodeWithTag(UiTags.BtnJoinGame).assertExists()
        onNodeWithTag(UiTags.BtnExit).assertExists()
    }

    @Test
    fun `clicking new game calls callback`() = runComposeUiTest {
        var called = false
        setContent {
            InitialScreen(onNewGame = { called = true }, onJoinGame = {}, onExit = {})
        }

        onNodeWithTag(UiTags.BtnNewGame).performClick()
        assertTrue(called)
    }

    @Test
    fun `clicking join game calls callback`() = runComposeUiTest {
        var called = false
        setContent {
            InitialScreen(onNewGame = {}, onJoinGame = { called = true }, onExit = {})
        }

        onNodeWithTag(UiTags.BtnJoinGame).performClick()
        assertTrue(called)
    }

    @Test
    fun `clicking exit calls callback`() = runComposeUiTest {
        var called = false
        setContent {
            InitialScreen(onNewGame = {}, onJoinGame = {}, onExit = { called = true })
        }

        onNodeWithTag(UiTags.BtnExit).performClick()
        assertTrue(called)
    }
}
