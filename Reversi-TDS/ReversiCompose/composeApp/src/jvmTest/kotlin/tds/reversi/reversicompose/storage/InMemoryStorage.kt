package tds.reversi.reversicompose.storage

class InMemoryStorage<K, V> : Storage<K, V> {

    private val map = mutableMapOf<K, V>()

    override fun create(key: K, data: V) {
        map[key] = data
    }

    override fun read(key: K): V? = map[key]

    override fun update(key: K, data: V) {
        map[key] = data
    }

    override fun delete(key: K) {
        map.remove(key)
    }

    fun clear() = map.clear()
}