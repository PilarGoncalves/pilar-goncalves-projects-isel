package tds.reversi.reversicompose.ui

/**
 * Centralized collection of UI test tags used across the Compose UI.
 *
 * These tags are attached to Composables via `Modifier.testTag(...)` and are mainly used by:
 * - UI tests (to reliably locate nodes without depending on text),
 * - Debugging (to identify important UI elements in the hierarchy).
 *
 * Keeping all tags in a single place ensures consistency and avoids duplication or typos.
 */
object UiTags {
    const val InitialScreen = "InitialScreen"
    const val BtnNewGame = "BtnNewGame"
    const val BtnJoinGame = "BtnJoinGame"
    const val BtnExit = "BtnExit"

    const val NewGameDialog = "NewGameDialog"
    const val NewGameMultiplayerCheck = "NewGameMultiplayerCheck"
    const val NewGameNameField = "NewGameNameField"
    const val NewGamePickBlack = "NewGamePickBlack"
    const val NewGamePickWhite = "NewGamePickWhite"
    const val NewGameStart = "NewGameStart"
    const val NewGameCancel = "NewGameCancel"

    const val JoinGameDialog = "JoinGameDialog"
    const val JoinGameNameField = "JoinGameNameField"
    const val JoinGameConfirm = "JoinGameConfirm"
    const val JoinGameCancel = "JoinGameCancel"

    const val ErrorDialog = "ErrorDialog"
    const val ErrorDialogOk = "ErrorDialogOk"

    const val GameOverDialog = "GameOverDialog"
    const val GameOverBack = "GameOverBack"

    const val BoardScreen = "BoardScreen"
    const val TopBarMenu = "TopBarMenu"
    const val TopBarBack = "TopBarBack"

    const val MenuNew = "MenuNew"
    const val MenuJoin = "MenuJoin"
    const val MenuRefresh = "MenuRefresh"
    const val MenuExit = "MenuExit"
    const val MenuPass = "MenuPass"
    const val MenuAutoRefresh = "MenuAutoRefresh"

    const val Board = "Board"
     /**
     * Tag for a specific board cell.
     *
     * Using coordinates in the tag allows UI tests to click/select an exact cell deterministically.
     *
     * @param row Zero-based row index.
     * @param col Zero-based column index.
     * @return A unique tag identifying the cell at (row, col).
     */
    fun cell(row: Int, col: Int) = "Cell_${row}_$col"

    const val SwitchTargets = "SwitchTargets"

    const val WaitingMessage = "WaitingMessage"
    const val GameMenuContainer = "GameMenuContainer"
    const val BoardBody = "BoardBody"
    const val GameMenu = "GameMenu"
    const val TargetsPanel = "TargetsPanel"
    /**
     * Tag for a target marker rendered inside a specific cell.
     *
     * @param row Zero-based row index.
     * @param col Zero-based column index.
     * @return A unique tag identifying the target marker at (row, col).
     */
    fun target(row: Int, col: Int) = "Target_${row}_$col"
}
