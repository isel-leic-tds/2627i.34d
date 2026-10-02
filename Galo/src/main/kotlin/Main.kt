import console.readCommand
import model.*
import console.*

fun main() {

    var game: Game? = null
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
            game?.show()
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
