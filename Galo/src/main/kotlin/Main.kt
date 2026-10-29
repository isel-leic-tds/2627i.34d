import console.readCommand
import model.*
import console.*
import storage.TextFileStorage

fun main() {

    var game = Game()
    val commands = getCommands(TextFileStorage("savedGames", GameSerializer))
    while(true) {
        // Ler o comando
        val (name, args) = readCommand()
        // Verificar se o comando existe
        val cmd = commands[name]
        if (cmd == null) {println("Command '$name' not found.")}
        else try {

            // Executar o comando
            game = cmd.execute(args, game)
            if (cmd.isToFinish) break
            game.show()
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
