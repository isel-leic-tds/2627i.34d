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
        operator fun invoke(row: Int, col: Int): Position {
            require(row in 0..<BOARD_SIZE && col in 0..<BOARD_SIZE)
            return values[row * BOARD_SIZE + col]
        }
    }
}


fun Int.toPositionOrNull() = Position.values.getOrNull(this)
