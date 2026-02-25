package tds.reversi.reversicompose.viewmodel

import androidx.compose.runtime.*
import tds.reversi.reversicompose.domain.Utils.isLocal
import tds.reversi.reversicompose.domain.Utils.isMultiplayer
import tds.reversi.reversicompose.domain.Utils.notMyTurn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tds.reversi.reversicompose.model.Player
import tds.reversi.reversicompose.storage.GameState
import tds.reversi.reversicompose.storage.Storage
import tds.reversi.reversicompose.domain.Command
import tds.reversi.reversicompose.domain.CommandContext
import tds.reversi.reversicompose.domain.CommandException
import tds.reversi.reversicompose.domain.CommandResult
import tds.reversi.reversicompose.domain.Join
import tds.reversi.reversicompose.domain.New
import tds.reversi.reversicompose.domain.Pass
import tds.reversi.reversicompose.domain.Play
import tds.reversi.reversicompose.domain.Refresh
import tds.reversi.reversicompose.domain.Targets
import tds.reversi.reversicompose.storage.MongoMapper
import tds.reversi.reversicompose.storage.MongoStorage

/**
 * ViewModel responsible for coordinating game logic, persistence and UI state.
 *
 * This class acts as the bridge between the UI layer (Compose screens)
 * and the domain layer (commands, game rules and persistence).
 *
 * Responsibilities:
 * - Hold and expose the current [ReversiUiState],
 * - Execute domain commands (New, Join, Play, Pass, Refresh, Targets),
 * - Manage multiplayer polling and auto-refresh,
 * - Handle persistence through [Storage],
 * - Convert domain errors into UI-friendly messages.
 */
class ReversiViewModel(
    private val storage: Storage<String, GameState> = MongoStorage(
        mapper = MongoMapper(),
        connectionString = "mongodb+srv://pilar:YBnCcBMbsVCmzgBy@cluster0.n4rcqkv.mongodb.net/?appName=Cluster0"
    )
) {

    /** Observable UI state exposed to the Compose layer. */
    var uiState by mutableStateOf(ReversiUiState())
        private set

    /** Coroutine scope tied to the ViewModel lifecycle. */
    private val viewModelScope = CoroutineScope(
        SupervisorJob() + Dispatchers.Main
    )

    /** Background job used to poll remote game updates in multiplayer mode. */
    private var pollingJob: Job? = null

    /**
     * Starts periodic polling for remote updates in multiplayer mode.
     *
     * Polling runs every second and:
     * - checks for remote updates;
     * - auto-refreshes the game if enabled and allowed.
     */
    private fun startPolling() {
        if (pollingJob != null) return

        pollingJob = viewModelScope.launch {
            while (isActive) {
                delay(1000)
                checkRemoteUpdate()

                if (uiState.autoRefreshEnabled && canAutoRefresh()) {
                    refreshGame()
                }
            }
        }
    }

    /**
     * Stops the polling job if it is currently active.
     */
    private fun stopPolling() {
        pollingJob?.cancel()
        pollingJob = null
    }

    /**
     * Toggles the auto-refresh feature on or off.
     */
    fun toggleAutoRefresh() {
        uiState = uiState.copy(autoRefreshEnabled = !uiState.autoRefreshEnabled)
    }

    /**
     * Indicates whether auto-refresh is currently enabled.
     */
    fun isAutoRefreshEnabled(): Boolean = uiState.autoRefreshEnabled

    /**
     * Determines whether auto-refresh can be toggled.
     *
     * Auto-refresh is only available in multiplayer games.
     */
    fun canToggleAutoRefresh(): Boolean {
        val game = uiState.game
        return game != null && isMultiplayer(game)
    }

    /**
     * Starts a new local game.
     *
     * @param player Player chosen by the user.
     */
    fun startLocalGame(player: Player) {
        viewModelScope.launch {
            dispatch(New(storage), listOf(player.symbol.toString()))
        }
    }

    /**
     * Starts a new multiplayer game.
     *
     * @param gameName Name of the multiplayer game.
     * @param player Player chosen by the user.
     */
    fun startMultiplayerGame(gameName: String, player: Player) {
        viewModelScope.launch {
            dispatch(New(storage), listOf(player.symbol.toString(), gameName))
        }
    }

    /**
     * Joins an existing multiplayer game.
     *
     * @param gameName Name of the game to join.
     */
    fun joinGame(gameName: String) {
        viewModelScope.launch {
            dispatch(Join(storage), listOf(gameName))
        }
    }

    /**
     * Attempts to play a move at the specified board position.
     *
     * @param row Board row (0-based).
     * @param col Board column (0-based).
     */
    fun playAt(row: Int, col: Int) {
        val game = uiState.game ?: return
        viewModelScope.launch {
            dispatch(Play(storage), listOf(game.formatCell(row, col)))
        }
    }

    /**
     * Passes the current player's turn if allowed.
     */
    fun passTurn() {
        viewModelScope.launch {
            dispatch(Pass(storage))
        }
    }

    /**
     * Refreshes the game state from remote storage.
     */
    fun refreshGame() {
        viewModelScope.launch {
            dispatch(Refresh(storage))
            uiState = uiState.copy(hasRemoteUpdate = false)
        }
    }

    /**
     * Toggles the visualization of valid move targets.
     */
    fun toggleTargets() {
        val game = uiState.game ?: return
        val arg = if (game.showTargets) "off" else "on"
        viewModelScope.launch {
            dispatch(Targets(), listOf(arg))
        }
    }

    /**
     * Determines whether target markers can be shown.
     */
    fun canShowTargets(): Boolean {
        val game = uiState.game
        return game != null && (isLocal(game) || game.currentPlayer == game.myPlayer)
    }

    /**
     * Exits the current game and performs cleanup.
     *
     * In multiplayer mode, the game may be removed from storage
     * depending on ownership and game progress.
     */
    fun exitGame() {
        stopPolling()

        val game = uiState.game
        val gameName = game?.name ?: run {
            uiState = uiState.copy(game = null)
            return
        }

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (game != null && isMultiplayer(game)) {
                    if (isGameOver()) storage.delete(gameName)
                    else if (game.creator == game.myPlayer) storage.delete(gameName)
                    else if (game.myPlayer != game.creator && game.moveCount > 1) storage.delete(gameName)
                }
            }
            uiState = uiState.copy(game = null)
        }
    }

    /**
     * Clears the current error message from the UI state.
     */
    fun clearError() {
        uiState = uiState.copy(errorMessage = null)
    }

    /**
     * Indicates whether the current player is allowed to pass.
     */
    fun canPass(): Boolean {
        val game = uiState.game
        return game != null && game.canPass() && !notMyTurn(game)
    }

    /**
     * Indicates whether a manual refresh can be performed.
     */
    fun canRefresh(): Boolean {
        val game = uiState.game
        return game != null &&
                isMultiplayer(game) &&
                !uiState.autoRefreshEnabled &&
                notMyTurn(game) &&
                uiState.hasRemoteUpdate
    }

    /**
     * Indicates whether auto-refresh can occur.
     */
    fun canAutoRefresh(): Boolean {
        val game = uiState.game
        return game != null &&
                isMultiplayer(game) &&
                notMyTurn(game) &&
                uiState.hasRemoteUpdate
    }

    /**
     * Determines whether the waiting-for-opponent message should be displayed.
     */
    fun shouldShowWaitingMessage(): Boolean {
        val game = uiState.game
        return game != null && isMultiplayer(game) && notMyTurn(game) && !canRefresh()
    }

    /**
     * Checks if the remote game state differs from the local one.
     */
    private suspend fun computeHasRemoteUpdate(): Boolean = withContext(Dispatchers.IO) {
        val game = uiState.game ?: return@withContext false
        if (isLocal(game)) return@withContext false

        val state = storage.read(game.name) ?: return@withContext false
        state.board.grid != game.board.grid || state.currentPlayer != game.currentPlayer
    }

    /**
     * Updates the UI state if a remote update is detected.
     */
    private suspend fun checkRemoteUpdate() {
        val updated = computeHasRemoteUpdate()
        if (uiState.hasRemoteUpdate != updated) {
            uiState = uiState.copy(hasRemoteUpdate = updated)
        }
    }

    /**
     * Indicates whether the current game is over.
     */
    fun isGameOver() = uiState.game?.isGameOver() ?: false

    /**
     * Central dispatcher for domain commands.
     *
     * Builds the appropriate [CommandContext], executes the command
     * and updates the UI state accordingly.
     */
    private suspend fun dispatch(
        command: Command<*>,
        params: List<String> = emptyList()
    ) {
        try {
            val gameSnapshot = uiState.game

            val context: CommandContext = when (command) {
                is New, is Join -> CommandContext.Empty
                else -> {
                    val g = gameSnapshot
                        ?: throw CommandException.InvalidParameters(command, "No active game")
                    CommandContext.WithGame(g)
                }
            }

            val result = withContext(Dispatchers.IO) {
                @Suppress("UNCHECKED_CAST")
                (command as Command<CommandContext>).execute(context, params)
            }

            when (result) {
                is CommandResult.Success -> {
                    uiState = uiState.copy(game = result.game)

                    if (isMultiplayer(result.game)) startPolling()
                    else stopPolling()
                }
                is CommandResult.Exit -> exitGame()
            }

        } catch (e: Exception) {
            handleError(e)
        }
    }

    /**
     * Converts domain and unexpected errors into UI-friendly messages.
     */
    private fun handleError(e: Exception) {
        val errorMessage = when (e) {
            is CommandException.InvalidParameters -> e.message?.ifBlank { e.command.help() }
            else -> e.message ?: "Unexpected error"
        }
        uiState = uiState.copy(errorMessage = errorMessage)
    }

    /**
     * Releases resources and closes the storage connection.
     */
    fun close() {
        stopPolling()
        (storage as? MongoStorage<*>)?.close()
    }
}