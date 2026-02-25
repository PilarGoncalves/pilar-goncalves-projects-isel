package tds.reversi.reversicompose.domain

import tds.reversi.reversicompose.model.Game
import tds.reversi.reversicompose.storage.Storage
import tds.reversi.reversicompose.storage.GameState

/**
 * Utility functions shared across commands.
 * This object provides helper functions for various operations used by multiple commands.
 * The functions help simplify common checks and operations, such as determining the game type, turn validation, and game state persistence.
 */
object Utils {

    /**
     * Returns true when the game is local (i.e., has no persisted name).
     *
     * Local games are not saved to storage and are played without synchronization with external systems.
     * This is useful for distinguishing between multiplayer and local games.
     *
     * @param game The current game instance.
     * @return True if the game is local (doesn't have a persisted name), false if it has a persisted name (multiplayer game).
     */
    fun isLocal(game: Game): Boolean = game.name.isEmpty()

    /**
     * Returns true if the game is multiplayer (i.e., not local).
     *
     * This function is a shorthand for determining if the game is a multiplayer game, which requires storage synchronization.
     * It is the inverse of the `isLocal` function.
     *
     * @param game The current game instance.
     * @return True if the game is multiplayer, false if it is local.
     */
    fun isMultiplayer(game: Game): Boolean = !isLocal(game)

    /**
     * Returns true if it is NOT the current user's turn in a non-local game.
     *
     * This check ensures that the user can only take action when it is their turn in a multiplayer game.
     * For local games, this function always returns false since turn enforcement is not necessary.
     *
     * @param game The current game instance.
     * @return True if it's not the current user's turn in a multiplayer game, false for local games or if it's the user's turn.
     */
    fun notMyTurn(game: Game): Boolean = isMultiplayer(game) && game.currentPlayer != game.myPlayer

    /**
     * Persists the current game state when the game is not local.
     *
     * This method is used to save the current game state to storage if the game is multiplayer (not local).
     * For local games, no persistence is performed since the game is not synced with external systems.
     *
     * @param game The current game instance.
     * @param storage The storage instance where the game state should be saved.
     *               The storage system will handle the actual persistence logic.
     */
    fun saveGame(game: Game, storage: Storage<String, GameState>) {
        if (isLocal(game)) return
        storage.update(game.name, game.toGameState())
    }
}