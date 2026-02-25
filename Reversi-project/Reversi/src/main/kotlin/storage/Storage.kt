package storage

/**
 * Interface that defines storage operations for key-value pairs.
 * This interface outlines the basic CRUD (Create, Read, Update, Delete) operations for managing data in a storage system.
 *
 * @param Key The type of the key used to identify each stored value.
 * @param Value The type of the value being stored.
 */
interface Storage<Key, Value> {

    /**
     * Creates a new entry in the storage with the specified key and value.
     * @param key The key used to identify the entry.
     * @param data The value to be stored.
     */
    fun create(key: Key, data: Value)

    /**
     * Reads the value associated with the specified key.
     * @param key The key used to retrieve the value.
     * @return The value associated with the key, or null if the key does not exist.
     */
    fun read(key: Key): Value?

    /**
     * Updates an existing entry in the storage with the specified key and value.
     * @param key The key used to identify the entry.
     * @param data The updated value to be stored.
     */
    fun update(key: Key, data: Value)

    /**
     * Deletes the entry associated with the specified key from the storage.
     * @param key The key used to identify the entry to be deleted.
     */
    fun delete(key: Key)
}