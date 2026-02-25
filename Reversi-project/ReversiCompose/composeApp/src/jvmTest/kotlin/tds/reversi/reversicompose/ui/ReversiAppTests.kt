package tds.reversi.reversicompose.ui

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test
import kotlin.test.assertTrue
import tds.reversi.reversicompose.storage.GameState
import tds.reversi.reversicompose.storage.InMemoryStorage
import tds.reversi.reversicompose.viewmodel.ReversiViewModel

@OptIn(ExperimentalTestApi::class)
class ReversiAppTests {

    private fun makeVm(): ReversiViewModel {
        val storage = InMemoryStorage<String, GameState>()
        return ReversiViewModel(storage = storage)
    }

    @Test
    fun `initially shows InitialScreen`() = runComposeUiTest {
        val vm = makeVm()

        setContent { ReversiApp(onExitApp = {}, viewModel = vm) }

        onNodeWithTag(UiTags.InitialScreen).assertExists()
        onNodeWithTag(UiTags.BtnNewGame).assertExists()
        onNodeWithTag(UiTags.BtnJoinGame).assertExists()
        onNodeWithTag(UiTags.BtnExit).assertExists()
    }

    @Test
    fun `starting a local game navigates to BoardScreen`() = runComposeUiTest {
        val vm = makeVm()

        setContent { ReversiApp(onExitApp = {}, viewModel = vm) }

        onNodeWithTag(UiTags.BtnNewGame).performClick()
        onNodeWithTag(UiTags.NewGameDialog).assertExists()

        onNodeWithTag(UiTags.NewGamePickBlack).performClick()
        onNodeWithTag(UiTags.NewGameStart).performClick()

        waitUntil(timeoutMillis = 5_000) {
            onAllNodesWithTag(UiTags.BoardScreen, useUnmergedTree = true)
                .fetchSemanticsNodes().isNotEmpty()
        }

        onNodeWithTag(UiTags.BoardScreen, useUnmergedTree = true).assertExists()
    }

    @Test
    fun `clicking Join Game opens and closes JoinGameDialog`() = runComposeUiTest {
        val vm = makeVm()

        setContent { ReversiApp(onExitApp = {}, viewModel = vm) }

        onNodeWithTag(UiTags.BtnJoinGame).performClick()
        onNodeWithTag(UiTags.JoinGameDialog).assertExists()

        onNodeWithTag(UiTags.JoinGameCancel).performClick()

        waitUntil(timeoutMillis = 2_000) {
            onAllNodesWithTag(UiTags.JoinGameDialog, useUnmergedTree = true)
                .fetchSemanticsNodes().isEmpty()
        }

        onNodeWithTag(UiTags.InitialScreen, useUnmergedTree = true).assertExists()
    }

    @Test
    fun `clicking Exit calls onExitApp`() = runComposeUiTest {
        val vm = makeVm()
        var exited = false

        setContent { ReversiApp(onExitApp = { exited = true }, viewModel = vm) }

        onNodeWithTag(UiTags.BtnExit).performClick()
        assertTrue(exited)
    }
}
