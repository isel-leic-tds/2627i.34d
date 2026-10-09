package console

import model.*

// Classe base, comando genérico
abstract class Command(val argsSyntax: String = "") {
    open fun execute(args: List<String>, game: Game?): Game? = game
    open val isToFinish: Boolean = false
}

// Subclasses: comandos em concreto. Singleton

// Play
object Play : Command("<pos>") {

    override fun execute(args: List<String>, game: Game?): Game? {
        val arg = requireNotNull(args.firstOrNull()) { "Missing position" }
        val pos = requireNotNull(arg.toIntOrNull()?.toPositionOrNull()) { "Invalid position" }
        return checkNotNull(game) { "Game Not started" }.play(pos)
    }
}

// New
object New : Command() {
    override fun execute(args: List<String>, game: Game?) = game?.new() ?: Game()
}

// Exit
object Exit : Command() {
    override val isToFinish: Boolean = true
}

// Mapeamento entre os nomes dos comandos e os objetos
val commands = mapOf(
    "NEW" to New,
    "EXIT" to Exit,
    "PLAY" to Play
)