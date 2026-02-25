package storage

import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath

/**
 * TextFileStorage handles the reading and writing of game data to text files.
 * It uses the provided [GameSerializer] to serialize and deserialize [model.Game] objects.
 * This class implements the [Storage] interface for managing game data in text files.
 */
class TextFileStorage<T>(private val serializer: Serializer<T>) : Storage<String, T> {

    /**
     * Generates the file path for a given key (game name).
     * This method constructs the file path where the game data will be stored or retrieved.
     *
     * @param key The name of the game.
     * @return The file path for the game data.
     */
    private fun filePath(key: String): Path {
        val baseDir = "games".toPath()
        FileSystem.SYSTEM.createDirectories(baseDir)
        return baseDir / "$key.txt"
    }

    /**
     * Helper function to update or insert data into the file.
     * This method writes the serialized game data into the corresponding file.
     *
     * @param key The name of the game.
     * @param data The game data to be saved.
     */
    private fun upSert(key: String, data: T) {
        val file = filePath(key)
        val content = serializer.serialize(data)
        FileSystem.SYSTEM.write(file) {
            writeUtf8(content)
        }
    }

    /**
     * Creates a new file with the provided data.
     * This method is used to store new game data in a file.
     *
     * @param key The name of the game.
     * @param data The game data to be saved.
     */
    override fun create(key: String, data: T) {
        upSert(key, data)
    }

    /**
     * Reads the game data from a file.
     * This method deserializes the data from the file and returns the game state.
     *
     * @param key The name of the game.
     * @return The deserialized game data, or null if the file does not exist.
     */
    override fun read(key: String): T? {
        val file = filePath(key)
        val fs = FileSystem.SYSTEM
        return try {
            if (fs.exists(file)) {
                fs.read(file) {
                    val content = readUtf8()
                    serializer.deserialize(content)
                }
            } else null
        } catch (e: Exception) {
            println("Error reading game from file: ${e.message}")
            null
        }
    }

    /**
     * Updates an existing file with new data.
     * This method overwrites the current game data with the updated data.
     *
     * @param key The name of the game.
     * @param data The updated game data.
     */
    override fun update(key: String, data: T) {
        upSert(key, data)
    }

    /**
     * Deletes a game file.
     * This method removes the file containing the game data from storage.
     *
     * @param key The name of the game.
     */
    override fun delete(key: String) {
        val file = filePath(key)
        val fs = FileSystem.SYSTEM
        if (fs.exists(file)) fs.delete(file)
    }
}
