package model

data class Game(
    val first: Player = Player.X,
    val board: List<Player?> = List(BOARD_CELLS) { null },
    val state: GameState = Run(first)
)

fun Game.isWinner(p: Player): Boolean =

    // Linha
    (0..<BOARD_CELLS step BOARD_SIZE).any {
        row -> (0..<BOARD_SIZE).all { col -> board[row + col] == p }
    } ||
    // Coluna
    (0..<BOARD_SIZE).any {
                col -> (0..<BOARD_CELLS step BOARD_SIZE).all { row -> board[row + col] == p }
        } ||

    // Diagona principal
    (0..<BOARD_CELLS step (BOARD_SIZE+1)).all { board[it] == p} ||

    // Diagonal secundária
    (2..<BOARD_CELLS step (BOARD_SIZE-1)).all { board[it] == p}

fun Game.new() = Game(first = first.other)
fun Game.canPlay(pos: Position): Boolean = board[pos.index] == null
fun Game.play(pos: Position): Game = when(state) {
    is Run -> {
        check(canPlay(pos)) { "Position $pos is already used" }
        val newGame = copy(
            board = board.mapIndexed { idx, move ->
                if (idx == pos.index) state.turn else move
            }
        )
        return copy(
            board = newGame.board,
            state = when {
                newGame.isWinner(state.turn) -> Win(state.turn)
                board.all { it != null } -> Draw
                else -> Run(state.turn.other)
            }
            )
    }
    is Win, Draw -> error("Game already finished")
}