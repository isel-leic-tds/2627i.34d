package console

import model.*
import storage.TextFileStorage

// Classe base, comando genérico
class Command(
    val argsSyntax: String = "",
    val isToFinish: Boolean = false,
    val execute: (args: List<String>, game: Game) -> Game = { _, game -> game }
)

// Play
private val Play = Command("<pos>") { args, game ->
        val arg = requireNotNull(args.firstOrNull()) { "Missing position" }
        val pos = requireNotNull(arg.toIntOrNull()?.toPositionOrNull()) { "Invalid position" }
        game.play(pos)
}

private fun storageCommand(specific: (Game, String) -> Game) =
    Command() { args, game ->
        val name = requireNotNull(args.firstOrNull()) { "Missing name" }
        val isValidName = name.matches(Regex("[a-zA-Z][a-zA-Z0-9_]*"))
        require(isValidName) { "Invalid name $name" }
        specific(game, name)
    }

// Mapeamento entre os nomes dos comandos e os objetos
fun getCommands(st: GameStorage) = mapOf(
    "NEW" to Command() { _, game -> game.new() },
    "EXIT" to Command(isToFinish=true),
    "PLAY" to Play,
    "SCORE" to Command() { _, game -> game.apply { showScore() } },
    "LOAD" to storageCommand { game, name ->
        checkNotNull(st.read(name)) { "Game $name not found" }
    },
    "SAVE" to storageCommand { game, name ->
        game.also {st.create(name, game)}
    }
)