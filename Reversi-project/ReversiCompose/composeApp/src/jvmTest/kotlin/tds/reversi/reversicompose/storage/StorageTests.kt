package tds.reversi.reversicompose.storage

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class StorageTests {

    @Test
    fun `read returns null for missing key`() {
        val sut = InMemoryStorage<String, Int>()
        assertNull(sut.read("missing"))
    }

    @Test
    fun `create then read returns stored value`() {
        val sut = InMemoryStorage<String, Int>()
        sut.create("k", 1)
        assertEquals(1, sut.read("k"))
    }

    @Test
    fun `update overwrites existing value`() {
        val sut = InMemoryStorage<String, Int>()
        sut.create("k", 1)
        sut.update("k", 2)
        assertEquals(2, sut.read("k"))
    }

    @Test
    fun `delete removes key`() {
        val sut = InMemoryStorage<String, Int>()
        sut.create("k", 1)
        sut.delete("k")
        assertNull(sut.read("k"))
    }
}
