package tds.reversi.reversicompose.viewmodel

import tds.reversi.reversicompose.model.Game

/**
 * Represents the UI state of the Reversi game.
 *
 * This data class holds the current state of the UI, including the game state, any error messages,
 * and flags related to automatic refresh and remote updates. It is used to manage the UI's dynamic state
 * and trigger reactivity in the app based on changes in the game state or error conditions.
 *
 * @property game The current game state, represented as a `Game` object.
 *                This property holds the details about the ongoing game, such as the board, players, and current turn.
 * @property errorMessage Any error message to be displayed in the UI. This is typically used for showing
 *                        validation errors or other issues that occur during gameplay.
 * @property autoRefreshEnabled A flag indicating whether automatic game state refresh is enabled.
 *                              This determines if the UI should periodically refresh to synchronize with remote changes.
 * @property hasRemoteUpdate A flag indicating whether there are updates to the game state that need to be reflected in the UI.
 *                           This flag is useful in multiplayer scenarios where one player's actions may affect the local game state.
 */
data class ReversiUiState(
    val game: Game? = null,
    val errorMessage: String? = null,
    val autoRefreshEnabled: Boolean = false,
    val hasRemoteUpdate: Boolean = false
)
