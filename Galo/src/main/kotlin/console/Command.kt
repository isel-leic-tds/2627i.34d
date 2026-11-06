package console

import model.*
import storage.TextFileStorage

// Classe base, comando genérico
class Command(
    val argsSyntax: String = "",
    val isToFinish: Boolean = false,
    val execute: (args: List<String>, match: Match) -> Match = { _, match -> match }
)

// Play
private val Play = Command("<pos>") { args, match ->
        val arg = requireNotNull(args.firstOrNull()) { "Missing position" }
        val pos = requireNotNull(arg.toIntOrNull()?.toPositionOrNull()) { "Invalid position" }
        match.play(pos)
}


// Mapeamento entre os nomes dos comandos e os objetos
fun getCommands() = mapOf(
    "NEW" to Command() { _, match -> match.new() },
    "EXIT" to Command(isToFinish=true),
    "PLAY" to Play,
    "SCORE" to Command() { _, match -> match.apply { showScore() } },
    "START" to Command("<name>") { args, match ->
        val arg = requireNotNull(args.firstOrNull()) { "Missing position" }
        match.start(Name(arg))
    },
    "JOIN" to Command("<name>") { args, match ->
        val arg = requireNotNull(args.firstOrNull()) { "Missing position" }
        match.join(Name(arg))
    },
    "REFRESH" to Command() { _, match -> match.refresh() },
)