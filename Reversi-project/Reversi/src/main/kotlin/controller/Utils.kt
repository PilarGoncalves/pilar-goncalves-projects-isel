package controller

import model.Game
import storage.Storage
import storage.GameState

/**
 * Utility functions shared across commands.
 * This object provides helper functions for various operations used by multiple commands.
 */
object Utils {

    /**
     * Returns true when the game is local (i.e., has no persisted name).
     * Local games are not saved to storage and are played without synchronization with external systems.
     * @param game The current game instance.
     * @return True if the game is local, false if it has a persisted name.
     */
    fun isLocal(game: Game): Boolean = game.name.isEmpty()

    /**
     * Returns true if it is NOT the current user's turn in a non-local game.
     * For local games, this always returns false, as turn enforcement is only relevant in persisted games.
     * @param game The current game instance.
     * @return True if it's not the current user's turn in a non-local game.
     */
    fun notMyTurn(game: Game): Boolean = !isLocal(game) && game.currentPlayer != game.myPlayer

    /**
     * Builds a string with the player information for non-local games, or returns null for local games.
     * This string can be used to display the player's context (e.g., the current player and the game name).
     * @param game The current game instance.
     * @return A string containing player info, or null if the game is local.
     */
    fun getPlayerInfo(game: Game): String? {
        if (game.name.isEmpty()) return null
        return "You are player ${game.myPlayer.symbol} in game ${game.name}"
    }

    /**
     * Persists the current game state when the game is not local.
     * For local games, no persistence is performed.
     * @param game The current game instance.
     * @param storage The storage instance where the game state should be saved.
     */
    fun saveGame(game: Game, storage: Storage<String, GameState>) {
        if (isLocal(game)) return
        storage.update(game.name, game.toGameState())
    }
}