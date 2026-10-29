package model

import storage.Serializer

data class Game(
    val first: Player = Player.X,
    val board: Board = emptyBoard(),
    val state: GameState = Run(first),
    val score: Score = emptyScore()
)

fun Score.advance(player: Player?): Score =
    plus(player to getValue(player) + 1)

fun Board.winnerIn(pos: Position): Boolean {
    if (size < BOARD_SIZE * 2 - 1) return false
    val player = this[pos]
    val cellRange = 0..<BOARD_SIZE
    return cellRange.all { col -> this[Position(pos.row, col)] == player} || // Linha completa
            cellRange.all { row -> this[Position(row, pos.col)] == player} || // Coluna completa
            (pos.backSlash && cellRange.all { i -> this[Position(i, i)] == player}) || // Diag. principal completa
            (pos.slash && cellRange.all { i -> this[Position(i, BOARD_SIZE - 1 - i)] == player}) // Diag. secundária completa
}

fun Game.new() = Game(
    first = first.other,
    score = if (state is Run) score.advance(state.turn.other) else score
)

fun Game.canPlay(pos: Position): Boolean = board[pos] == null
fun Game.play(pos: Position): Game = when(state) {
    is Run -> {
        check(canPlay(pos)) { "Position $pos is already used" }
        val newBoard = board + Pair(pos, state.turn)
        val newState = when {
            newBoard.winnerIn(pos) -> Win(state.turn)
            newBoard.size == BOARD_CELLS -> Draw
            else -> Run(state.turn.other)
        }
        return copy(
            board = newBoard,
            state = newState,
            score = when(newState) {
                is Win -> score.advance(newState.winner)
                is Draw -> score.advance(null)
                is Run -> score
            }
            )
    }
    is Win, Draw -> error("Game already finished")
}

//X
//0:X 1:O
//Run:X
//X=0 O=0 null=0
object GameSerializer: Serializer<Game> {

    override fun serialize(data: Game): String = buildString {
        appendLine(data.first.name)
        appendLine(data.board.entries.joinToString(" ") {
                (pos, player) -> "${pos.index}:${player.name}"
        })
        appendLine(GameStateSerializer.serialize(data.state))
        appendLine(data.score.entries.joinToString(" ") {
                (player, score) -> "${player?.name}=${score}"
        })
    }

    override fun deserialize(text: String): Game {
        val (l1, l2, l3, l4) = text.split('\n')
        val first = l1.toPlayer()
        val board = l2.split(' ').map {
            it.split(':')
        }.associate {
                (index, player) -> Position(index.toInt()) to player.toPlayer()
        }
        val state = GameStateSerializer.deserialize(l3)
        val score = l4.split(' ').map {
            it.split('=')
        }.associate {
                (player, score) -> player.toPlayerOrNull() to score.toInt()
        }

        return Game(first, board, state, score)
    }
}
