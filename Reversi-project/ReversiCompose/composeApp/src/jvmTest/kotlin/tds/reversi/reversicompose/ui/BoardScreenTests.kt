package tds.reversi.reversicompose.ui

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test
import tds.reversi.reversicompose.storage.GameState
import tds.reversi.reversicompose.storage.InMemoryStorage
import tds.reversi.reversicompose.viewmodel.ReversiViewModel

@OptIn(ExperimentalTestApi::class)
class BoardScreenTests {

    @Test
    fun `BoardScreen appears after starting local game`() = runComposeUiTest {
        val storage = InMemoryStorage<String, GameState>()
        val vm = ReversiViewModel(storage = storage)

        setContent { ReversiApp(onExitApp = {}, viewModel = vm) }

        onNodeWithTag(UiTags.BtnNewGame).performClick()
        onNodeWithTag(UiTags.NewGamePickBlack).performClick()
        onNodeWithTag(UiTags.NewGameStart).performClick()

        waitForIdle()

        onNodeWithTag(UiTags.BoardScreen, useUnmergedTree = true).assertExists()
    }

    @Test
    fun `TopBarBack returns to InitialScreen`() = runComposeUiTest {
        val storage = InMemoryStorage<String, GameState>()
        val vm = ReversiViewModel(storage = storage)

        setContent { ReversiApp(onExitApp = {}, viewModel = vm) }

        onNodeWithTag(UiTags.BtnNewGame).performClick()
        onNodeWithTag(UiTags.NewGamePickBlack).performClick()
        onNodeWithTag(UiTags.NewGameStart).performClick()

        waitForIdle()
        onNodeWithTag(UiTags.BoardScreen, useUnmergedTree = true).assertExists()

        onNodeWithTag(UiTags.TopBarBack).performClick()

        waitForIdle()
        onNodeWithTag(UiTags.InitialScreen, useUnmergedTree = true).assertExists()
        onNodeWithTag(UiTags.BoardScreen, useUnmergedTree = true).assertDoesNotExist()
    }

    @Test
    fun `TopBarMenu toggles GameMenu visibility`() = runComposeUiTest {
        val storage = InMemoryStorage<String, GameState>()
        val vm = ReversiViewModel(storage = storage)

        setContent { ReversiApp(onExitApp = {}, viewModel = vm) }

        onNodeWithTag(UiTags.BtnNewGame).performClick()
        onNodeWithTag(UiTags.NewGamePickBlack).performClick()
        onNodeWithTag(UiTags.NewGameStart).performClick()

        waitForIdle()
        onNodeWithTag(UiTags.BoardScreen, useUnmergedTree = true).assertExists()

        onNodeWithTag(UiTags.GameMenu, useUnmergedTree = true).assertExists()

        onNodeWithTag(UiTags.TopBarMenu).performClick()
        waitForIdle()
        onNodeWithTag(UiTags.GameMenu, useUnmergedTree = true).assertDoesNotExist()

        onNodeWithTag(UiTags.TopBarMenu).performClick()
        waitForIdle()
        onNodeWithTag(UiTags.GameMenu, useUnmergedTree = true).assertExists()
    }

    @Test
    fun `SwitchTargets exists in local game`() = runComposeUiTest {
        val storage = InMemoryStorage<String, GameState>()
        val vm = ReversiViewModel(storage = storage)

        setContent { ReversiApp(onExitApp = {}, viewModel = vm) }

        onNodeWithTag(UiTags.BtnNewGame).performClick()
        onNodeWithTag(UiTags.NewGamePickBlack).performClick()
        onNodeWithTag(UiTags.NewGameStart).performClick()

        waitForIdle()
        onNodeWithTag(UiTags.BoardScreen, useUnmergedTree = true).assertExists()

        onNodeWithTag(UiTags.SwitchTargets, useUnmergedTree = true).assertExists()
    }

    @Test
    fun `SwitchTargets can be clicked`() = runComposeUiTest {
        val storage = InMemoryStorage<String, GameState>()
        val vm = ReversiViewModel(storage = storage)

        setContent { ReversiApp(onExitApp = {}, viewModel = vm) }

        onNodeWithTag(UiTags.BtnNewGame).performClick()
        onNodeWithTag(UiTags.NewGamePickBlack).performClick()
        onNodeWithTag(UiTags.NewGameStart).performClick()

        waitForIdle()

        onNodeWithTag(UiTags.SwitchTargets, useUnmergedTree = true).performClick()
        waitForIdle()
        onNodeWithTag(UiTags.BoardScreen, useUnmergedTree = true).assertExists()
    }
}
