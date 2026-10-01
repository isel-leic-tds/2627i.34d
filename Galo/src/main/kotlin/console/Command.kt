package console

import model.*

// Classe base, comando genérico
abstract class Command(val argsSyntax: String = "") {
    open fun execute(args: List<String>, game: Game?): Game? = game
    open val isToFinish: Boolean = false
}

// Subclasses: comandos em concreto

// Play
class Play : Command("<pos>") {

    override fun execute(args: List<String>, game: Game?): Game? {
        val arg = requireNotNull(args.firstOrNull()) { "Missing position" }
        val pos = requireNotNull(arg.toIntOrNull()) { "Invalid position" }
        return checkNotNull(game) { "Game Not started" }.play(pos)
    }
}

// New

// Exit

