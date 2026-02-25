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
class GameMenuTests {

    private fun startLocalGameAndGoToBoard(
        t: androidx.compose.ui.test.ComposeUiTest,
        vm: ReversiViewModel
    ) {
        t.setContent { ReversiApp(onExitApp = {}, viewModel = vm) }

        t.onNodeWithTag(UiTags.BtnNewGame, useUnmergedTree = true).performClick()
        t.onNodeWithTag(UiTags.NewGamePickBlack, useUnmergedTree = true).performClick()
        t.onNodeWithTag(UiTags.NewGameStart, useUnmergedTree = true).performClick()
    }

    @Test
    fun `MenuNew opens NewGameDialog`() = runComposeUiTest {
        val storage = InMemoryStorage<String, GameState>()
        val vm = ReversiViewModel(storage = storage)

        startLocalGameAndGoToBoard(this, vm)
        waitForIdle()

        onNodeWithTag(UiTags.MenuNew, useUnmergedTree = true).performClick()
        waitForIdle()

        onNodeWithTag(UiTags.NewGameDialog, useUnmergedTree = true).assertExists()
    }

    @Test
    fun `MenuJoin opens JoinGameDialog`() = runComposeUiTest {
        val storage = InMemoryStorage<String, GameState>()
        val vm = ReversiViewModel(storage = storage)

        startLocalGameAndGoToBoard(this, vm)
        waitForIdle()

        onNodeWithTag(UiTags.MenuJoin, useUnmergedTree = true).performClick()
        waitForIdle()

        onNodeWithTag(UiTags.JoinGameDialog, useUnmergedTree = true).assertExists()
    }

    @Test
    fun `MenuExit returns to InitialScreen`() = runComposeUiTest {
        val storage = InMemoryStorage<String, GameState>()
        val vm = ReversiViewModel(storage = storage)

        startLocalGameAndGoToBoard(this, vm)
        waitForIdle()

        onNodeWithTag(UiTags.MenuExit, useUnmergedTree = true).performClick()
        waitForIdle()

        onNodeWithTag(UiTags.InitialScreen, useUnmergedTree = true).assertExists()
    }
}
