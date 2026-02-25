package tds.reversi.reversicompose.storage

/**
 * Interface that defines storage operations for key-value pairs.
 *
 * This interface outlines the basic CRUD (Create, Read, Update, Delete) operations for managing data in a storage system.
 * It provides a contract for any storage solution (e.g., MongoDB, local storage) to implement, allowing the system to store,
 * retrieve, update, and delete data in a key-value format.
 *
 * @param Key The type of the key used to identify each stored value. It acts as the unique identifier for each entry.
 * @param Value The type of the value being stored. It represents the data associated with each key.
 */
interface Storage<Key, Value> {

    /**
     * Creates a new entry in the storage with the specified key and value.
     *
     * This method is used to add a new item to the storage. The key is used to uniquely identify the entry,
     * and the value represents the data to be stored.
     *
     * @param key The key used to identify the entry.
     * @param data The value to be stored.
     */
    fun create(key: Key, data: Value)

    /**
     * Reads the value associated with the specified key.
     *
     * This method retrieves the value stored in the storage for the given key. If the key does not exist,
     * it returns `null`.
     *
     * @param key The key used to retrieve the value.
     * @return The value associated with the key, or null if the key does not exist.
     */
    fun read(key: Key): Value?

    /**
     * Updates an existing entry in the storage with the specified key and value.
     *
     * This method is used to update the value associated with an existing key in the storage.
     * If the key does not exist, the behavior of this method is typically defined by the implementation
     * (e.g., it might create a new entry or throw an error).
     *
     * @param key The key used to identify the entry.
     * @param data The updated value to be stored.
     */
    fun update(key: Key, data: Value)

    /**
     * Deletes the entry associated with the specified key from the storage.
     *
     * This method removes the entry from the storage based on the provided key.
     * Once deleted, the key-value pair is no longer available in the storage.
     *
     * @param key The key used to identify the entry to be deleted.
     */
    fun delete(key: Key)
}
