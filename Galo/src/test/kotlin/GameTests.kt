import model.*
import kotlin.test.*

class GameTest {
    @Test
    fun initialGameState() {
        val game = Game()
        assertEquals(Player.X, game.first)
        assertTrue(game.board.isEmpty())
        assertTrue(game.state is Run)
        assertEquals(Player.X, (game.state as Run).turn)
        //assertEquals("{X=0, O=0, null=0}", game.score.toString())
    }

    @Test
    fun playMoveUpdatesBoardAndTurn() {
        val pos = Position.values.random()
        val game = Game().play(pos)
        assertEquals(Player.X, game.board[pos])
        assertTrue(game.state is Run)
        assertEquals(Player.O, (game.state as Run).turn)
    }
    @Test
    fun playMoveOnUsedPositionThrows() {
        val pos = Position.values.random()
        val game = Game().play(pos)
        assertFailsWith<IllegalStateException> { game.play(pos) }
    }

    private fun playMoves(vararg moves: Int): Game =
        moves.fold(Game()) { g, pos -> g.play(Position(pos)) }

    @Test
    fun winDetection() {
        val game = playMoves(1, 0, 4, 3, 7) // X wins
        assertTrue(game.state is Win)
        assertEquals(Player.X, (game.state as Win).winner)
        assertEquals(1, game.score[Player.X])
    }

    @Test
    fun drawDetection() {
        val game = playMoves(0, 1, 2, 4, 3, 5, 7, 6, 8)
        assertTrue(game.state is Draw)
        assertEquals(1, game.score[null])
    }
    @Test
    fun newGameSwitchesFirstPlayer() {
        val game = playMoves(0, 1, 2)
        assertTrue(game.state is Run)
        assertFalse(game.board.isEmpty())
        val newGame = game.new()
        assertEquals(game.first.other, newGame.first)
        assertTrue(newGame.board.isEmpty())
        assertTrue(newGame.state is Run)
        //assertEquals(0, game.score.values.sum())
        //assertEquals(1, newGame.score[game.first])
    }

    @Test
    fun tryToPlayAfterWinThrows() {
        val game = playMoves(1, 0, 4, 3, 7) // X wins
        assertTrue(game.state is Win)
        assertFailsWith<IllegalStateException> {
            val emptyPos = (Position.values - game.board.keys).random()
            game.play(emptyPos)
        }
    }
}