import kotlin.test.*
import model.*
import storage.*
import okio.*
import okio.Path.Companion.toPath

const val TEST_DIR_PATH="test"

class MatchTests {
    private val storage = TextFileStorage<Name,Game>(
        TEST_DIR_PATH,
        GameSerializer)
    @AfterTest fun after() {
        // delete the test folder
        FileSystem.SYSTEM.delete(TEST_DIR_PATH.toPath())
    }
    @Test fun `use a match not started`() {
        val match = Match(storage)
        assertIsNot<MatchRun>(match)
        assertFailsWith<IllegalStateException> { match.play(Position(0)) }
        assertFailsWith<IllegalStateException> { match.new() }
        assertFailsWith<IllegalStateException> { match.refresh() }
    }
    @Test fun `start a match`() {
        val name = Name("test1")
        val match = Match(storage).start(name)
        assertIs<MatchRun>(match)
        assertEquals(name, match.id)
        assertEquals(Player.X, match.localPlayer)
        assertEquals(Player.X, match.game.first)
        val game = storage.read(name)
        assertNotNull(game)
        assertIs<Run>(game.state)
        assertEquals(Player.X, game.state.turn)
        match.deleteIfIsOwner() // is the owner
        assertNull(storage.read(name))
    }
    @Test fun `join a match`() {
        val name = Name("test2")
        Match(storage).start(name)
        val match = Match(storage).join(name)
        assertIs<MatchRun>(match)
        assertEquals(Player.O, match.localPlayer)
        match.deleteIfIsOwner() // is not the owner
        storage.delete(name)
    }
    @Test fun `play a match`() {
        val name = Name("test3")
        Match(storage).start(name).play(Position(0))
        val game = storage.read(name)
        assertNotNull(game)
        val state = game.state
        assertIs<Run>(state)
        assertEquals(Player.O, state.turn)
        assertEquals(Player.X, game.board[Position(0)])
        storage.delete(name)
    }
}