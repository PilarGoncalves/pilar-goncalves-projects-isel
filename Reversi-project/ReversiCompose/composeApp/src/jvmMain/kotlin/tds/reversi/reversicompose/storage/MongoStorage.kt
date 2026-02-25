package tds.reversi.reversicompose.storage

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.ReplaceOptions
import org.bson.Document

/**
 * MongoStorage class implements the [Storage] interface for storing data in MongoDB.
 * This class provides CRUD operations (Create, Read, Update, Delete) to interact with a MongoDB collection.
 * It uses a [DocumentMapper] to convert between the application's data objects and MongoDB `Document` format.
 *
 * @param mapper The [DocumentMapper] used to convert data objects to and from MongoDB `Document`.
 * @param connectionString The connection string to the MongoDB instance.
 * @param dbName The name of the MongoDB database (default is "reversi").
 * @param collectionName The name of the MongoDB collection (default is "games").
 */
class MongoStorage<T>(
    private val mapper: DocumentMapper<T>,
    connectionString: String,
    dbName: String = "reversi",
    collectionName: String = "games"
) : Storage<String, T> {
    private val client: MongoClient = MongoClients.create(connectionString)
    private val collection: MongoCollection<Document> =
        client.getDatabase(dbName).getCollection(collectionName)

    /**
     * Creates a new entry in the MongoDB collection or updates an existing one.
     * This method performs an "upsert" (insert or update) operation.
     *
     * @param key The key used to identify the entry in the collection (e.g., game name).
     * @param data The data to be stored in the collection.
     */
    override fun create(key: String, data: T) {
        upsert(key, data)
    }

    /**
     * Reads the data associated with the specified key from the MongoDB collection.
     * This method retrieves the `Document` from MongoDB and converts it into the appropriate data type using the `mapper`.
     *
     * @param key The key used to retrieve the entry from the collection.
     * @return The data object associated with the key, or `null` if no data is found.
     */
    override fun read(key: String): T? {
        val doc = collection.find(eq("_id", key)).first() ?: return null
        return mapper.fromDocument(doc)
    }

    /**
     * Updates an existing entry in the MongoDB collection.
     * This method performs an "upsert" operation (insert or update).
     *
     * @param key The key used to identify the entry in the collection.
     * @param data The updated data to be stored in the collection.
     */
    override fun update(key: String, data: T) {
        upsert(key, data)
    }

    /**
     * Deletes the entry associated with the specified key from the MongoDB collection.
     *
     * @param key The key used to identify the entry to be deleted.
     */
    override fun delete(key: String) {
        collection.deleteOne(eq("_id", key))
    }

    /**
     * Performs an "upsert" operation (insert or update) to save data to the MongoDB collection.
     * If the document with the specified key already exists, it will be replaced; otherwise, it will be inserted.
     *
     * @param key The key used to identify the entry in the collection.
     * @param data The data to be saved to the collection.
     */
    private fun upsert(key: String, data: T) {
        val doc = mapper.toDocument(key, data)
        collection.replaceOne(
            eq("_id", key),
            doc,
            ReplaceOptions().upsert(true)
        )
    }

    /**
     * Closes the MongoDB client connection.
     * This should be called when the storage is no longer needed to release resources.
     */
    fun close() {
        client.close()
    }
}
