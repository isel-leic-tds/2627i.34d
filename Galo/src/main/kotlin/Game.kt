private const val EMPTY = ' '

data class Game(
    val first: Char = 'X',
    val turn: Char = first,
    val board: List<Char> = listOf(
        EMPTY, EMPTY, EMPTY,
        EMPTY, EMPTY, EMPTY,
        EMPTY, EMPTY, EMPTY
    )
)

private fun Char.otherPlayer() = if (this == 'X') 'O' else 'X'
private fun Game.isWinner(p: Char): Boolean =

    // Linha
    (0..6 step 3).any {
        row -> (0..2).all { col -> board[row + col] == p }
    } ||
    // Coluna
    (0..2).any {
                col -> (0..6 step 3).all { row -> board[row + col] == p }
        } ||

    // Diagona principal
    (0..8 step 4).all { board[it] == p} ||

    // Diagonal secundária
    (2..6 step 2).all { board[it] == p}

fun Game.new() = Game(first = first.otherPlayer())
fun Game.canPlay(pos: Int): Boolean = board[pos] == EMPTY
fun Game.play(pos: Int) = copy(
    turn = turn.otherPlayer(),
    board = board.mapIndexed {
        idx, move -> if (idx == pos) turn else move
    })

