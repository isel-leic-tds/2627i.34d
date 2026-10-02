import model.*
import kotlin.test.*

class PlayerTests {

    @Test
    fun testOther() {
        assertEquals(Player.O, Player.X.other)
        assertEquals(Player.X, Player.O.other)
    }
    @Test
    fun toPlayerOrNull_Valid() {
        assertEquals(Player.X, "X".toPlayerOrNull())
        assertEquals(Player.O, "O".toPlayerOrNull())
    }
    @Test
    fun toPlayerOrNull_Invalid() {
        assertEquals(null, "A".toPlayerOrNull())
        assertEquals(null, "".toPlayerOrNull())
    }
    @Test
    fun toPlayer_Valid() {
        assertEquals(Player.X, "X".toPlayer())
        assertEquals(Player.O, "O".toPlayer())
    }
    @Test
    fun toPlayer_Invalid() {
        assertFailsWith<IllegalArgumentException> { "A".toPlayer() }
    }
}