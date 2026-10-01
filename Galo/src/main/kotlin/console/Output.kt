package console

import model.EMPTY
import model.Game
import model.isWinner
import kotlin.collections.chunked
import kotlin.collections.joinToString

fun Game.show() {

    // Imprimir o tabuleiro
    board.chunked(3).forEachIndexed { idx, row ->
        println(" ${row.joinToString(" | ")} ")
        if (idx < 2) println("---+---+---")
    }

    // Imprimir info. adicional
    println(when {
        isWinner('X') -> "Winner: X"
        isWinner('O') -> "Winner: O"
        board.all { it != EMPTY } -> "Draw"
        else -> "Turn: $turn"

    })
}

