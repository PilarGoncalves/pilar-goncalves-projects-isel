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
class BoardViewTests {

    @Test
    fun `Board is displayed after starting local game`() = runComposeUiTest {
        val storage = InMemoryStorage<String, GameState>()
        val vm = ReversiViewModel(storage = storage)

        setContent { ReversiApp(onExitApp = {}, viewModel = vm) }

        onNodeWithTag(UiTags.BtnNewGame).performClick()
        onNodeWithTag(UiTags.NewGamePickBlack).performClick()
        onNodeWithTag(UiTags.NewGameStart).performClick()

        waitForIdle()

        onNodeWithTag(UiTags.Board, useUnmergedTree = true).assertExists()
    }

    @Test
    fun `Cell 0_0 exists`() = runComposeUiTest {
        val storage = InMemoryStorage<String, GameState>()
        val vm = ReversiViewModel(storage = storage)

        setContent { ReversiApp(onExitApp = {}, viewModel = vm) }

        onNodeWithTag(UiTags.BtnNewGame).performClick()
        onNodeWithTag(UiTags.NewGamePickBlack).performClick()
        onNodeWithTag(UiTags.NewGameStart).performClick()

        waitForIdle()

        onNodeWithTag(UiTags.cell(0, 0), useUnmergedTree = true).assertExists()
    }

    @Test
    fun `Clicking a cell does not crash`() = runComposeUiTest {
        val storage = InMemoryStorage<String, GameState>()
        val vm = ReversiViewModel(storage = storage)

        setContent { ReversiApp(onExitApp = {}, viewModel = vm) }

        onNodeWithTag(UiTags.BtnNewGame).performClick()
        onNodeWithTag(UiTags.NewGamePickBlack).performClick()
        onNodeWithTag(UiTags.NewGameStart).performClick()

        waitForIdle()

        onNodeWithTag(UiTags.cell(2, 3), useUnmergedTree = true).performClick()

        waitForIdle()

        onNodeWithTag(UiTags.Board, useUnmergedTree = true).assertExists()
    }

    @Test
    fun `Targets appear only when switch is ON`() = runComposeUiTest {
        val storage = InMemoryStorage<String, GameState>()
        val vm = ReversiViewModel(storage = storage)

        setContent { ReversiApp(onExitApp = {}, viewModel = vm) }

        onNodeWithTag(UiTags.BtnNewGame).performClick()
        onNodeWithTag(UiTags.NewGamePickBlack).performClick()
        onNodeWithTag(UiTags.NewGameStart).performClick()

        waitForIdle()

        onNodeWithTag(UiTags.target(2, 3), useUnmergedTree = true).assertDoesNotExist()

        onNodeWithTag(UiTags.SwitchTargets).performClick()
        waitForIdle()

        onNodeWithTag(UiTags.target(2, 3), useUnmergedTree = true).assertExists()
    }
}
