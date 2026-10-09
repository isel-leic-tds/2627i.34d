package console

import model.*
import kotlin.collections.chunked
import kotlin.collections.joinToString

private val separator = "---" + "+---".repeat(BOARD_SIZE-1)
fun Game.show() {

    // Imprimir o tabuleiro
    board.chunked(BOARD_SIZE).forEachIndexed { idx, row ->
        val plays = row.map { it ?: " "}
        println(" ${plays.joinToString(" | ")} ")
        if (idx < BOARD_SIZE-1) println(separator)
    }

    // Imprimir info. adicional
    println(when (state) {
        is Win -> "Winner: ${state.winner}"
        is Draw -> "Draw"
        is Run -> "Turn: $state.turn"
    })
}
