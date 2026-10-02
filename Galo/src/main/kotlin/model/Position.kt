package model

const val BOARD_SIZE = 3
const val BOARD_CELLS = BOARD_SIZE * BOARD_SIZE

@JvmInline
value class Position private constructor (val index: Int) {
    init {
        require(index in 0..<BOARD_CELLS) { "Invalid position index: $index" }
    }

    val row: Int get() = index / BOARD_SIZE
    val col: Int get() = index % BOARD_SIZE
    val backSlash: Boolean get() = row == col
    val slash: Boolean get() = row + col == BOARD_SIZE - 1

    override fun toString() = "$index"

    companion object {
        val values = List(BOARD_CELLS) { Position(it) }
        operator fun invoke(index: Int) = values[index]
        operator fun invoke(row: Int, col: Int) = values[row * BOARD_SIZE + col]
    }
}


fun main() {

    val p1 = Position(1)
    val p2 = Position(1)
    val p3 = Position.values[3]
    val p4 = Position(4)
    val p5 = Position(1, 2)
}