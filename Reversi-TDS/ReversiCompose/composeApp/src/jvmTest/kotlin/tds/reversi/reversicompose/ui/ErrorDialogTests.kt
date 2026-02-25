package tds.reversi.reversicompose.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test
import kotlin.test.assertTrue
import tds.reversi.reversicompose.ui.dialogs.ErrorDialog

@OptIn(ExperimentalTestApi::class)
class ErrorDialogTests {

    @Test
    fun `error dialog shows message`() = runComposeUiTest {
        setContent {
            ErrorDialog(message = "Invalid move", onDismiss = {})
        }

        onNodeWithTag(UiTags.ErrorDialog).assertExists()
        onNodeWithText("Invalid move").assertExists()
    }

    @Test
    fun `ok button calls onDismiss`() = runComposeUiTest {
        var dismissed = false

        setContent {
            ErrorDialog(
                message = "Error",
                onDismiss = { dismissed = true }
            )
        }

        onNodeWithTag(UiTags.ErrorDialogOk).performClick()
        assertTrue(dismissed)
    }
}
