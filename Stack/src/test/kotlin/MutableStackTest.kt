import kotlin.test.*

class MutableStackTest {

    @Test fun `empty stack conditions`() {
        val sut = MutableStack<Int>()
        assertTrue(sut.isEmpty())
        assertFailsWith<NoSuchElementException> { sut.top }
        assertFailsWith<NoSuchElementException> { sut.pop() }
    }

    @Test fun `not empty stack conditions`() {
        val sut = MutableStack<String>()
        sut.push("A")
        assertFalse(sut.isEmpty())
        assertEquals("A", sut.top)
        assertEquals("A", sut.pop())
        assertTrue(sut.isEmpty())
    }
}