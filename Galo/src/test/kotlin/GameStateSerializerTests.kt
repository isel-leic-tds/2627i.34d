import kotlin.test.*
import model.*

class GameStateSerializerTests {

    @Test
    fun `Serialize GameState`() {

        val run = Run(Player.X)
        val win = Win(Player.O)
        assertEquals("Run:X", GameStateSerializer.serialize(run))
        assertEquals("Win:O", GameStateSerializer.serialize(win))
        assertEquals("Draw:", GameStateSerializer.serialize(Draw))
        assertEquals(run, GameStateSerializer.deserialize("Run:X"))
        assertEquals(win, GameStateSerializer.deserialize("Win:O"))
        assertEquals(Draw, GameStateSerializer.deserialize("Draw:"))
    }
}