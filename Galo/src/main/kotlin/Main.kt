import console.readCommand
import model.*
import console.*
import storage.TextFileStorage
import storage.mongo.MongoDriver
import storage.mongo.MongoStorage

fun main() {

    MongoDriver(nameDb = "Galo").use { md ->
    var match = Match(MongoStorage("savedGames", md, GameSerializer))
    val commands = getCommands()
    while(true) {
        // Ler o comando
        val (name, args) = readCommand()
        // Verificar se o comando existe
        val cmd = commands[name]
        if (cmd == null) {println("Command '$name' not found.")}
        else try {

            // Executar o comando
            match = cmd.execute(args, match)
            if (cmd.isToFinish) break
            match.show()
        }
        catch (e: IllegalStateException) {
            println(e.message)
        }
        catch (e: IllegalArgumentException) {
            println(e.message)
            println("Use: $name ${cmd.argsSyntax}")
        }
    }
        }
}
