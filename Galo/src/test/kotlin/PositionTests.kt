import model.*
import kotlin.test.*


class PositionTests {

    @Test
    fun topLeftPosition() {
        val sut = Position(0)
        assertEquals(0, sut.index)
        assertEquals(0, sut.row)
        assertTrue(sut.backSlash)
        assertFalse(sut.slash)
    }
    @Test
    fun topRightPosition() {
        val sut = Position(BOARD_SIZE - 1)
        assertEquals(BOARD_SIZE - 1, sut.index)
        assertEquals(0, sut.row)
        assertFalse(sut.backSlash)
        assertTrue(sut.slash)
    }

    @Test
    fun bottomRightPosition() {
        val sut = Position(BOARD_CELLS - 1)
        assertEquals(BOARD_CELLS - 1, sut.index)
        assertEquals(BOARD_SIZE - 1, sut.row)
        assertTrue(sut.backSlash)
        assertFalse(sut.slash)
    }

    @Test
    fun equalsPosition() {
        val sut1 = Position(2*BOARD_SIZE + 1)
        val sut2 = Position(2, 1)
        assertEquals(sut1, sut2)
        assertFalse(sut1 != sut2)
    }

    @Test
    fun invalidPositions() {
        assertFailsWith<IndexOutOfBoundsException> { Position(-1) }
        assertFailsWith<IndexOutOfBoundsException> { Position(BOARD_CELLS) }
        assertFailsWith<IllegalArgumentException> { Position(-1, 0) }
        assertFailsWith<IllegalArgumentException> { Position(0, -1) }
        assertFailsWith<IllegalArgumentException> { Position(BOARD_SIZE, BOARD_SIZE) }
        assertNull(BOARD_CELLS.toPositionOrNull())
        assertNull((-3).toPositionOrNull())
    }
}