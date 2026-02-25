package storage

/**
 * Interface for serializing and deserializing data.
 * This interface defines the methods required to convert an object into a string format and vice versa.
 */
interface Serializer<Data> {

    /**
     * Serializes the given data into a string.
     * This method converts the object into a format that can be easily stored or transmitted.
     * @param data The object to be serialized.
     * @return A string representation of the object.
     */
    fun serialize(data: Data): String

    /**
     * Deserializes the given string into an object of type [Data].
     * This method converts a string representation back into the original object.
     * @param text The string to be deserialized.
     * @return The reconstructed object, or null if the string is invalid or cannot be deserialized.
     */
    fun deserialize(text: String): Data?
}