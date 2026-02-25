package tds.reversi.reversicompose.storage

import org.bson.Document

/**
 * Interface for mapping data objects to and from MongoDB `Document` objects.
 *
 * This interface provides methods for converting between the application's data types and the `Document`
 * representation used by MongoDB. It is generic, allowing it to be used for various data types.
 *
 * @param Data The type of data to be mapped (e.g., `GameState`, `Player`, etc.).
 */
interface DocumentMapper<Data> {

    /**
     * Converts a data object to a MongoDB `Document`.
     *
     * This method is used to serialize a data object into a `Document` format that can be stored in MongoDB.
     * The `id` is required to uniquely identify the document in the database.
     *
     * @param id The unique identifier for the data object (typically the game's name or ID).
     * @param data The data object to be converted to a `Document`.
     * @return The MongoDB `Document` representing the data object.
     */
    fun toDocument(id: String, data: Data): Document

    /**
     * Converts a MongoDB `Document` to a data object.
     *
     * This method is used to deserialize a `Document` from MongoDB into a data object, allowing the application
     * to work with the data in its native format.
     *
     * @param doc The `Document` to be converted into a data object.
     * @return The corresponding data object, or `null` if the document cannot be converted.
     */
    fun fromDocument(doc: Document): Data?
}
